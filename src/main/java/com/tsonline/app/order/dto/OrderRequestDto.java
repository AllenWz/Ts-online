package com.tsonline.app.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
	private Long addressId;
	
	private String paymentMethod;
	
	private String pgName;
	
	private String pgPaymentId;
	
	private String pgStatus;
	
	private String pgResponseMessage;
}
