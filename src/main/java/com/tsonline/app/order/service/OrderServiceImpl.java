package com.tsonline.app.order.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsonline.app.address.entity.Address;
import com.tsonline.app.address.repository.AddressRepository;
import com.tsonline.app.cart.entity.Cart;
import com.tsonline.app.cart.entity.CartItem;
import com.tsonline.app.cart.repository.CartRepository;
import com.tsonline.app.cart.service.CartService;
import com.tsonline.app.common.exception.ResourceNotFoundException;
import com.tsonline.app.common.util.Mapper;
import com.tsonline.app.order.dto.OrderItemResponseDto;
import com.tsonline.app.order.dto.OrderRequestDto;
import com.tsonline.app.order.dto.OrderResponseDto;
import com.tsonline.app.order.entity.Order;
import com.tsonline.app.order.entity.OrderItem;
import com.tsonline.app.order.entity.OrderStatus;
import com.tsonline.app.order.repository.OrderItemRepository;
import com.tsonline.app.order.repository.OrderRepository;
import com.tsonline.app.payment.entity.Payment;
import com.tsonline.app.payment.repository.PaymentRepository;
import com.tsonline.app.product.entity.Product;
import com.tsonline.app.product.repository.ProductRepository;
import com.tsonline.app.user.entity.User;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private Mapper mapper;
	
	@Override
	@Transactional
	public OrderResponseDto placeOrder(User user, OrderRequestDto orderRequest) {
		logger.info("#OrderServiceImpl#placeOrder");
		String userEmail = user.getEmail();
		
		// Getting user cart
		Cart userCart = cartRepository.findCartByEmail(userEmail);
		if (userCart == null) {
			throw new ResourceNotFoundException("Cart not found for the user "+ userEmail);
		}
		
		Address address = addressRepository.findById(orderRequest.getAddressId())
								.orElseThrow(() -> new ResourceNotFoundException("Address", "address ID", orderRequest.getAddressId()));
		
		// Create new order with payment info
		Order order = new Order();
		order.setEmail(userEmail);
		order.setOrderDate(LocalDate.now());
		order.setTotalAmount(userCart.getTotalPrice());
		order.setOrderStatus(OrderStatus.ORDER_ACCEPTED);
		order.setAddress(address);
		
		Payment payment = new Payment(
				orderRequest.getPaymentMethod(),
				orderRequest.getPgPaymentId(),
				orderRequest.getPgStatus(),
				orderRequest.getPgResponseMessage(),
				orderRequest.getPgName()
				);
		payment.setOrder(order);
		payment = paymentRepository.save(payment);
		
		order.setPayment(payment);
		
		Order savedOrder = orderRepository.save(order);
		
		// Get items from the cart into order items
		List<CartItem> cartItems = userCart.getCartItems();
		if(cartItems == null) {
			throw new ResourceNotFoundException("There is no items in the cart");
		}
		
		List<OrderItem> orderItems = new ArrayList<>();
		for(CartItem cartItem: cartItems) {
			OrderItem orderItem = new OrderItem();
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setDiscount(cartItem.getDiscount());
			orderItem.setOrderedProductPrice(cartItem.getProductPrice());
			orderItem.setOrder(savedOrder);
			orderItems.add(orderItem);
		}
		
		orderItems = orderItemRepository.saveAll(orderItems);
		
		// Update product stock
		userCart.getCartItems().forEach(item -> {
			int quantity = item.getQuantity();
			Product product = item.getProduct();
			product.setQuantity(product.getQuantity() - quantity);
			productRepository.save(product);
			
			// Clear the cart
			cartService.deleteProductFromCart(userCart.getCartId(), item.getProduct().getProductId(), user);
		});
		
		// Send back the order summary
		OrderResponseDto responseDto = mapper.orderEntityToDto(savedOrder);
		List<OrderItemResponseDto> orderItemResponses = orderItems.stream()
				.map(mapper::orderItemResponseEntityToDto).toList();
		responseDto.setOrderItems(orderItemResponses);
		return responseDto;
	}

}
