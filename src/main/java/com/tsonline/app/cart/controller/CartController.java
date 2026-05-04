package com.tsonline.app.cart.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsonline.app.cart.dto.CartResponseDto;
import com.tsonline.app.cart.service.CartService;
import com.tsonline.app.common.util.AuthUtil;
import com.tsonline.app.user.entity.User;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api")
public class CartController {
	private static final Logger logger = LoggerFactory.getLogger(CartController.class);
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	AuthUtil authUtil;

	
	@Operation(summary = "Add Product to cart", description = "API to add product to cart with selected quantity")
	@PostMapping("/carts/products/{productId}/quantity/{quantity}")
	public ResponseEntity<CartResponseDto> addProductToCart(@PathVariable Long productId,
															@PathVariable Integer quantity) {
		logger.info("#CartController#addProductToCart");
		User user = authUtil.getLoggedInUser();
		CartResponseDto response = cartService.addProductToCart(user, productId, quantity);
		return new ResponseEntity<CartResponseDto>(response, HttpStatus.CREATED);
	}
	
	
	@Operation(summary = "Get all carts", description = "API to get all carts. Only user with admin role have access to this API.")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/carts")
	public ResponseEntity<List<CartResponseDto>> getAllCarts() {
		logger.info("#CartController#getAllCarts");
		List<CartResponseDto> cartDtos = cartService.getAllCarts();
		return new ResponseEntity<List<CartResponseDto>>(cartDtos, HttpStatus.OK);
	}
	
	
	@Operation(summary = "Get cart of login user", description = "API to get cart of current login user")
	@GetMapping("/carts/user/cart/{cartId}")
	public ResponseEntity<CartResponseDto> getCartById(@PathVariable Long cartId) {
		logger.info("#CartController#getCartById");
		String email = authUtil.getLoggedInEmail();
		CartResponseDto response = cartService.getCart(email, cartId);
		return new ResponseEntity<CartResponseDto>(response, HttpStatus.OK);
	}
	
	
	@Operation(summary = "Update the cart", description = "API to update selected product in the cart")
	@PutMapping("/cart/products/{productId}/quantity/{quantity}")
	public ResponseEntity<CartResponseDto> updateCartProduct(@PathVariable Long productId, 
															@PathVariable Integer quantity) {
		logger.info("#CartController#updateCartProduct");
		User user = authUtil.getLoggedInUser();
		CartResponseDto response =  cartService.updateProductQuantityInCart(productId, quantity, user);
		return new ResponseEntity<CartResponseDto>(response, HttpStatus.OK);
	}
	
	
	@Operation(summary = "Delete product from cart", description = "API to delete selected product from cart")
	@DeleteMapping("/carts/{cartId}/product/{productId}")
	public ResponseEntity<CartResponseDto> deleteProductFromCart(@PathVariable Long cartId, 
														@PathVariable Long productId) {
		logger.info("#CartController#deleteProductFromCart");
		User user = authUtil.getLoggedInUser();
		CartResponseDto response = cartService.deleteProductFromCart(cartId, productId, user);
		return new ResponseEntity<CartResponseDto>(response, HttpStatus.OK);
	}
}
