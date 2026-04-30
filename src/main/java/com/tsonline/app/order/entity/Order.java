package com.tsonline.app.order.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.tsonline.app.address.entity.Address;
import com.tsonline.app.common.entity.BaseEntity;
import com.tsonline.app.payment.entity.Payment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;
	
	@Column(nullable = false)
	private String email;
	
	@OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<OrderItem> orderItems = new ArrayList<>();
	
	private LocalDate orderDate;
	
	private Double totalAmount;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	
	@OneToOne
	@JoinColumn(name = "payment_id")
	private Payment payment;
	
	@ManyToOne
	@JoinColumn(name = "address_id")
	private Address address;
}
