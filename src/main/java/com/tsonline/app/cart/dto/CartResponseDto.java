package com.tsonline.app.cart.dto;

import java.util.List;

import com.tsonline.app.product.dto.ProductResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDto {
	private Long cartId;
	
	private Double totalPrice = 0.0;
	
	private List<ProductResponseDTO> products;
	
}
