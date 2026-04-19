package com.tsonline.app.cart.entity;

import com.tsonline.app.product.entity.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_items")
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cartItemId;
	
	@ManyToOne
	@JoinColumn(name = "cart_Id")
	private Cart cart;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	private Integer quantity;
	
	private Double discount;
	
	private Double productPrice;
}
