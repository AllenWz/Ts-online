package com.tsonline.app.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {
	private Long paymentId;
	
	private String paymentMethod;
	
	private String pgPaymentId;
	
	private String pgStatus;
	
	private String pgResponseMessage;
	
	private String pgName;
}
