package com.tsonline.app.cart.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tsonline.app.cart.dto.CartResponseDto;
import com.tsonline.app.cart.entity.Cart;
import com.tsonline.app.cart.entity.CartItem;
import com.tsonline.app.cart.repository.CartItemRepository;
import com.tsonline.app.cart.repository.CartRepository;
import com.tsonline.app.common.exception.BusinessRuleException;
import com.tsonline.app.common.exception.DuplicateEntryException;
import com.tsonline.app.common.exception.ResourceNotFoundException;
import com.tsonline.app.common.util.AuthUtil;
import com.tsonline.app.common.util.Mapper;
import com.tsonline.app.product.dto.ProductResponseDTO;
import com.tsonline.app.product.entity.Product;
import com.tsonline.app.product.repository.ProductRepository;
import com.tsonline.app.user.entity.User;
import com.tsonline.app.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService{
	private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	CartItemRepository cartItemRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Mapper mapper;

	@Override
	@Transactional
	public CartResponseDto addProductToCart(User user, Long productId, Integer quantity) {
		logger.info("#CartServiceImpl#addProductToCart");
		if (quantity <= 0) {
			throw new BusinessRuleException("Quantity must be greater than zero", HttpStatus.BAD_REQUEST);
		}
		
		Product product = productRepository.findById(productId)
							.orElseThrow(() -> new ResourceNotFoundException("Product", "Product ID", productId));
		if (product.isDeleted()) {
			throw new BusinessRuleException("This product " + product.getProductName() + " is no longer available!", HttpStatus.GONE);
		}
		
		Cart cart = createOrGetCart(user);
		
		CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, cart.getCartId());
		if(cartItem != null) {
			throw new DuplicateEntryException("Product " + product.getProductName() + " already exists in the cart!");
		}
		if(product.getQuantity() == 0) {
			String message = String.format("There is no stock left for product %s", product.getProductName());
			throw new BusinessRuleException(message, HttpStatus.CONFLICT);
		}
		if(product.getQuantity() < quantity) {
			String message = product.getProductName() + " have only " + product.getQuantity() + " stock left";
			throw new BusinessRuleException(message, HttpStatus.CONFLICT);
		}
		
		CartItem newCartItem = new CartItem();
		newCartItem.setProduct(product);
		newCartItem.setCart(cart);
		newCartItem.setQuantity(quantity);
		newCartItem.setDiscount(product.getDiscount());
		newCartItem.setProductPrice(product.getSpecialPrice());
		
		cartItemRepository.save(newCartItem);
		cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
		cartRepository.save(cart);
		
		CartResponseDto cartResponseDto = mapper.cartEntityToDto(cart);
		List<CartItem> cartItems = cart.getCartItems();
		Stream<ProductResponseDTO> productResponseDtoStream = cartItems.stream().map(item -> {
			ProductResponseDTO productDto = mapper.productEntityToDto(item.getProduct());
			productDto.setQuantity(item.getQuantity());
			return productDto;
		});
		
		cartResponseDto.setProducts(productResponseDtoStream.toList());
		return cartResponseDto;
	}

	@Override
	public List<CartResponseDto> getAllCarts() {
		logger.info("#CartServiceImpl#getAllCarts");
		
		List<Cart> carts = cartRepository.findAll();
		if (carts.isEmpty()) {
			throw new ResourceNotFoundException("No cart exists");
		}
		
		List<CartResponseDto> cartDtos = carts.stream()
				.map(cart -> {
					CartResponseDto cartDto = mapper.cartEntityToDto(cart);
					
					List<ProductResponseDTO> products = cart.getCartItems()
															.stream()
															.map((item) -> {
																ProductResponseDTO dto = mapper.productEntityToDto(item.getProduct());
																dto.setQuantity(item.getQuantity());
																return dto;
															})
															.collect(Collectors.toList());
					cartDto.setProducts(products);
					return cartDto;
				})
				.collect(Collectors.toList());
		return cartDtos;
	}
	
	@Override
	public CartResponseDto getCart(String email, Long cartId) {
		logger.info("#CartServiceImpl#getCart");
		Cart cart = cartRepository.findCartByEmailAndCartId(email, cartId);
		
		if(cart == null) {
			throw new ResourceNotFoundException("Cart", "Cart Id", cartId);
		}
		
		CartResponseDto cartDto = mapper.cartEntityToDto(cart);
		List<ProductResponseDTO> products = cart.getCartItems()
												.stream()
												.map(item -> {
													ProductResponseDTO dto = mapper.productEntityToDto(item.getProduct());
													dto.setQuantity(item.getQuantity());
													return dto;
												})
												.collect(Collectors.toList());
		cartDto.setProducts(products);
		return cartDto;
	}
	
	@Override
	@Transactional
	public CartResponseDto updateProductQuantityInCart(Long productId, Integer quantity, User user) {
		logger.info("#CartServiceImpl#updateProductQuantityInCart");
		// Invalid quantity check
		if (quantity == null || quantity < 0) {
			throw new BusinessRuleException("Invalid quantity", HttpStatus.BAD_REQUEST);
		}
		
		Cart cart = cartRepository.findCartByEmail(user.getEmail());
		// No cart for login user found
		if (cart == null) {
			throw new ResourceNotFoundException("Cart", "user ", user.getEmail());
		}
				
		Product product = productRepository.findById(productId)
							.orElseThrow(() -> new ResourceNotFoundException("Proudct", "product ID", productId));
		if(product.isDeleted() && quantity > 0) {
			throw new BusinessRuleException("This product " + product.getProductName() + " is no longer available!", HttpStatus.GONE);
		}
		// Product out of stock in inventory
		if(product.getQuantity() == 0) {
			String message = String.format("There is no stock left for product %s", product.getProductName());
			throw new BusinessRuleException(message, HttpStatus.CONFLICT);
		}
		
		CartItem cartItemToUpdate = cart.getCartItems().stream()
										.filter(item -> item.getProduct().getProductId().equals(productId))
										.findFirst()
										.orElseThrow(() -> new ResourceNotFoundException("Product with ID " + productId + " not found in cart"));
		// quantity 0 means that item is removed from the cart
		if (quantity == 0) {
			cartItemRepository.delete(cartItemToUpdate);
			cart.getCartItems().remove(cartItemToUpdate);
		} else {
			// Request quantity greater than remaining stock scenario
			if (product.getQuantity() < quantity) {
				String message = product.getProductName() + " have only " + product.getQuantity() + " stock left";
				throw new BusinessRuleException(message, HttpStatus.CONFLICT);
			}
			
			cartItemToUpdate.setQuantity(quantity);
			cartItemToUpdate.setProductPrice(product.getSpecialPrice());
		}
		
		cart.setTotalPrice(calculateCartTotalPrice(cart));
		Cart updatedCart = cartRepository.save(cart);
		
		CartResponseDto cartDto = mapper.cartEntityToDto(updatedCart);
		List<ProductResponseDTO> products = updatedCart.getCartItems()
														.stream()
														.map(item -> {
															// Set the quantity of selected product to cart item quantity not the stock quantity
															ProductResponseDTO productDto = mapper.productEntityToDto(item.getProduct());
															productDto.setQuantity(item.getQuantity());
															return productDto;
														}).collect(Collectors.toList());
		cartDto.setProducts(products);
		return cartDto;
	}
	
	@Override
	@Transactional
	public CartResponseDto deleteProductFromCart(Long cartId, Long productId, User user) {
		logger.info("#CartServiceImpl#deleteProductFromCart");
		
		Cart cart = cartRepository.findCartByEmailAndCartId(user.getEmail(), cartId);
		if (cart == null) {
			throw new ResourceNotFoundException("Cart", "user ", user.getEmail());
		}
		
		CartItem cartItem = cart.getCartItems().stream()
								.filter(item -> item.getProduct().getProductId().equals(productId))
								.findFirst()
								.orElseThrow(() -> new ResourceNotFoundException("Product", "product ID", productId));
		
		cartItemRepository.deleteCartItemByCartIdAndProductId(cartId, productId);
		cart.getCartItems().remove(cartItem);
		
		cart.setTotalPrice(calculateCartTotalPrice(cart));
		Cart updatedCart = cartRepository.save(cart);
		
		CartResponseDto cartDto = mapper.cartEntityToDto(updatedCart);
		List<ProductResponseDTO> products = updatedCart.getCartItems()
														.stream()
														.map(item -> {
															// Set the quantity of selected product to cart item quantity not the stock quantity
															ProductResponseDTO productDto = mapper.productEntityToDto(item.getProduct());
															productDto.setQuantity(item.getQuantity());
															return productDto;
														}).collect(Collectors.toList());
		cartDto.setProducts(products);
		return cartDto;
	}
	
	@Override
	@Transactional
	public void updateProductInCarts(List<Cart> carts, Product product) {
		logger.info("#CartServiceImpl#updateProductInCarts");
		
		for (Cart cart: carts) {
			cart.getCartItems().stream()
				.filter(item -> item.getProduct().getProductId().equals(product.getProductId()))
				.findFirst()
				.ifPresent(item -> {
					item.setProductPrice(product.getSpecialPrice());
					item.setDiscount(product.getDiscount());
					
					Double newTotal = cart.getCartItems().stream()
							.mapToDouble(i -> i.getProductPrice() * i.getQuantity())
							.sum();
					cart.setTotalPrice(newTotal);
				});
		}
		
		cartRepository.saveAll(carts);
	}

	private Cart createOrGetCart(User user) {
		logger.info("#CartServiceImpl#createCart");
		Cart userCart = cartRepository.findCartByEmail(user.getEmail());	
		if(userCart != null) {
			return userCart;
		}
		
		Cart cart = new Cart();
		cart.setTotalPrice(0.00);
		cart.setUser(user);
		Cart newCart = cartRepository.save(cart);
		return newCart;
	}
	
	private Double calculateCartTotalPrice(Cart cart) {
		logger.info("#CartServiceImpl#calculateCartTotalPrice");
		Double total = cart.getCartItems().stream()
							.mapToDouble(item -> item.getProductPrice() * item.getQuantity())
							.sum();		
		return total;
	}
}
