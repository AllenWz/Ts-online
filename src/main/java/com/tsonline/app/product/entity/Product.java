package com.tsonline.app.product.entity;

import java.util.ArrayList;
import java.util.List;

import com.tsonline.app.cart.entity.CartItem;
import com.tsonline.app.category.entity.Category;
import com.tsonline.app.common.entity.BaseEntity;
import com.tsonline.app.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;

	private String productName;

	private String description;

	private String image;

	private Integer quantity;

	private double price;

	private double discount;

	private double specialPrice;
	
	private boolean deleted = false;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	@ManyToOne
	@JoinColumn(name = "seller_id")
	private User user;
	
	@OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
	private List<CartItem> cartItems = new ArrayList<>();
}
