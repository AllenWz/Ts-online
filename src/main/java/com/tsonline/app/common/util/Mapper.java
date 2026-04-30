package com.tsonline.app.common.util;

import org.springframework.stereotype.Component;

import com.tsonline.app.address.dto.AddressRequestDto;
import com.tsonline.app.address.dto.AddressResponseDto;
import com.tsonline.app.address.entity.Address;
import com.tsonline.app.cart.dto.CartResponseDto;
import com.tsonline.app.cart.entity.Cart;
import com.tsonline.app.category.dto.CategoryDtoRequest;
import com.tsonline.app.category.dto.CategoryDtoResponse;
import com.tsonline.app.category.entity.Category;
import com.tsonline.app.order.dto.OrderItemResponseDto;
import com.tsonline.app.order.dto.OrderResponseDto;
import com.tsonline.app.order.entity.Order;
import com.tsonline.app.order.entity.OrderItem;
import com.tsonline.app.payment.dto.PaymentResponseDto;
import com.tsonline.app.payment.entity.Payment;
import com.tsonline.app.product.dto.ProductRequestDTO;
import com.tsonline.app.product.dto.ProductResponseDTO;
import com.tsonline.app.product.entity.Product;

@Component
public class Mapper {
	public CategoryDtoResponse categoryEntityToDto(Category category) {
		return new CategoryDtoResponse(category.getCategoryId(), category.getCategoryName());
	}

	public Category categoryDtoToEntity(CategoryDtoRequest dto) {
		Category category = new Category();
		category.setCategoryName(dto.getCategoryName());
		return category;
	}

	public Product productDtoToEntity(ProductRequestDTO dto) {
		Product product = new Product();
		product.setProductName(dto.getProductName());
		product.setDescription(dto.getDescription());
		product.setImage(dto.getImageName());
		product.setQuantity(dto.getQuantity());
		product.setPrice(dto.getPrice());
		product.setDiscount(dto.getDiscount());
		return product;
	}

	public ProductResponseDTO productEntityToDto(Product entity) {
		ProductResponseDTO dto = new ProductResponseDTO();
		dto.setProductId(entity.getProductId());
		dto.setProductName(entity.getProductName());
		dto.setDescription(entity.getDescription());
		dto.setImageName(entity.getImage());
		dto.setQuantity(entity.getQuantity());
		dto.setPrice(entity.getPrice());
		dto.setDiscount(entity.getDiscount());
		dto.setSpecialPrice(entity.getSpecialPrice());
		dto.setCategoryId(entity.getCategory().getCategoryId());
		dto.setDeleted(entity.isDeleted());
		return dto;
	}
	
	public CartResponseDto cartEntityToDto(Cart cart) {
		CartResponseDto dto = new CartResponseDto();
		dto.setCartId(cart.getCartId());
		dto.setTotalPrice(cart.getTotalPrice());
		return dto;
	}
	
	public Address addressDtoToEntity(AddressRequestDto dto) {
		Address address = new Address();
		address.setStreet(dto.getStreet());
		address.setBuildingName(dto.getBuildingName());
		address.setCity(dto.getCity());
		address.setState(dto.getState());
		address.setCountry(dto.getCountry());
		address.setPincode(dto.getPincode());
		return address;
	}
	
	public AddressResponseDto addressEntityToDto(Address address) {
		AddressResponseDto dto = new AddressResponseDto();
		dto.setAddressId(address.getAddressId());
		dto.setStreet(address.getStreet());
		dto.setBuildingName(address.getBuildingName());
		dto.setCity(address.getCity());
		dto.setState(address.getState());
		dto.setCountry(address.getCountry());
		dto.setPincode(address.getPincode());
		return dto;
	}
	
	public OrderResponseDto orderEntityToDto(Order order) {
		OrderResponseDto dto = new OrderResponseDto();
		dto.setAddressId(order.getAddress().getAddressId());
		dto.setEmail(order.getEmail());
		dto.setOrderDate(order.getOrderDate());
		dto.setOrderId(order.getOrderId());
		dto.setOrderStatus(order.getOrderStatus());
		dto.setPayment(paymentEntityToDto(order.getPayment()));
		dto.setTotalAmount(order.getTotalAmount());
		return dto;
	}
	
	public OrderItemResponseDto orderItemResponseEntityToDto(OrderItem orderItem) {
		OrderItemResponseDto dto = new OrderItemResponseDto();
		dto.setOrderItemId(orderItem.getOrderItemId());
		dto.setDiscount(orderItem.getDiscount());
		dto.setOrderedProductPrice(orderItem.getOrderedProductPrice());
		dto.setProduct(productEntityToDto(orderItem.getProduct()));
		dto.setQuantity(orderItem.getQuantity());
		return dto;
	}
	
	public PaymentResponseDto paymentEntityToDto(Payment payment) {
		PaymentResponseDto dto = new PaymentResponseDto();
		dto.setPaymentId(payment.getPaymentId());
		dto.setPaymentMethod(payment.getPaymentMethod());
		dto.setPgName(payment.getPgName());
		dto.setPgPaymentId(payment.getPgPaymentId());
		dto.setPgResponseMessage(payment.getPgResponseMessage());
		dto.setPgStatus(payment.getPgStatus());
		return dto;
	}
}
