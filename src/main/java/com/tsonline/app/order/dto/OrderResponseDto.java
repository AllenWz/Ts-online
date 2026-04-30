package com.tsonline.app.order.dto;

import java.time.LocalDate;
import java.util.List;

import com.tsonline.app.order.entity.OrderStatus;
import com.tsonline.app.payment.dto.PaymentResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
	private Long orderId;
	
	private String email;
	
	private List<OrderItemResponseDto> orderItems;
	
	private LocalDate orderDate;
	
	private PaymentResponseDto payment;
	
	private Double totalAmount;
	
	private OrderStatus orderStatus;
	
	private Long addressId;
}
