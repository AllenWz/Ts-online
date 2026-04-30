package com.tsonline.app.payment.entity;

import com.tsonline.app.common.entity.BaseEntity;
import com.tsonline.app.order.entity.Order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;
	
	@OneToOne(mappedBy = "payment", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Order order;
	
	private String paymentMethod;
	
	private String pgPaymentId;
	
	private String pgName;
	
	private String pgStatus;
	
	private String pgResponseMessage;
	
	public Payment(String paymentMethod, String pgPaymentId, String pgStatus, String pgResponseMessage, String pgName) {
		this.paymentMethod = paymentMethod;
		this.pgPaymentId = pgPaymentId;
		this.pgStatus = pgStatus;
		this.pgResponseMessage = pgResponseMessage;
		this.pgName = pgName;
	}
}
