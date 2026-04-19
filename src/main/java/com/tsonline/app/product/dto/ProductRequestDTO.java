package com.tsonline.app.product.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {
	
	@NotBlank(message = "{common.required}")
	@Size(min = 5, max = 50, message = "{common.size.range}")
	private String productName;

	@Size(max = 255, message = "{common.size.max}")
	private String description;
	
	@Max(value = 50, message = "{common.size.max}")
	private String imageName;
	
	@NotNull(message = "{common.required}")
	@Min(value = 0, message = "{product.value.min}")
    @Max(value = 9999, message = "{product.value.max}")
	private Integer quantity;

	@NotNull(message = "{common.required}")
    @Min(value = 0, message = "{product.value.min}")
	@Digits(integer = 10, fraction = 2, message = "{common.digits}")
	private Double price;

	@Digits(integer = 10, fraction = 2, message = "{common.digits}")
	private Double discount;

	@NotNull(message = "{common.required}")
	private Long categoryId;
}
