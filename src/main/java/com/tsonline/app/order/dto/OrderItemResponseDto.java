package com.tsonline.app.order.dto;

import com.tsonline.app.product.dto.ProductResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDto {
	private Long orderItemId;
	
	private ProductResponseDTO product;
	
	private Integer quantity;
	
	private Double discount;
	
	private Double orderedProductPrice;
}
