package com.tsonline.app.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDtoRequest {
	
	@NotBlank(message="{common.required}")
	@Size(min=5, max=50, message="{common.size.range}")
	private String categoryName;
}
