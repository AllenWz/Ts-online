package com.tsonline.app.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDtoResponse {
	private Long categoryId;
	
	private String categoryName;
}
