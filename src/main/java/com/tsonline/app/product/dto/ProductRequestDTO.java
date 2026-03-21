package com.tsonline.app.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {
	private String productName;

	private String description;

	private String image;

	private Integer quantity;

	private double price;

	private double discount;

	private Long categoryId;
}
