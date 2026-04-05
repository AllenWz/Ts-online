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
	
	@NotBlank(message = "Product Name is required")
	@Size(min = 5, max = 50, message = "Product Name must be between 5 and 50 characters")
	private String productName;

	@Size(max = 255, message = "Description can't be more than 255 characters")
	private String description;
	
	@Max(value = 50, message = "Image Name can't be more than 50 character")
	private String imageName;
	
	@NotNull(message = "Quantity is required")
	@Min(value = 0, message = "Quantity cannot be negative")
    @Max(value = 9999, message = "Quantity can't be more than 9999")
	private Integer quantity;

	@NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be negative")
	@Digits(integer = 10, fraction = 2, message = "Price must be a valid format (e.g., 12.50)")
	private Double price;

	@Digits(integer = 10, fraction = 2, message = "Discount must be a valid format (e.g., 12.50)")
	private Double discount;

	@NotNull(message = "Category ID is required")
	private Long categoryId;
}
