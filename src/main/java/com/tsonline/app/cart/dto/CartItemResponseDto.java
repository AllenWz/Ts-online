package com.tsonline.app.cart.dto;

import com.tsonline.app.product.dto.ProductRequestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDto {
	private CartResponseDto cart;
	
	private ProductRequestDTO product;
	
	private Integer quantity;
	
	private Double discount;
	
	private Double productPrice;
}
