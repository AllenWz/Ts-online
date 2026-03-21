package com.tsonline.app.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
	private Long productId;
	
	private String productName;
	
	private String description;

	private String image;

	private double price;

	private double discount;

	private double specialPrice;
	
	private Long categoryId;
}
