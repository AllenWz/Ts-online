package com.tsonline.app.common.util;

import org.springframework.stereotype.Component;

import com.tsonline.app.category.dto.CategoryDtoRequest;
import com.tsonline.app.category.dto.CategoryDtoResponse;
import com.tsonline.app.category.entity.Category;
import com.tsonline.app.product.dto.ProductRequestDTO;
import com.tsonline.app.product.dto.ProductResponseDTO;
import com.tsonline.app.product.entity.Product;

@Component
public class Mapper {
	public CategoryDtoResponse categoryEntityToDto(Category category) {
		return new CategoryDtoResponse(category.getCategoryId(), category.getCategoryName());
	}

	public Category categoryDtoToEntity(CategoryDtoRequest dto) {
		Category category = new Category();
		category.setCategoryName(dto.getCategoryName());
		return category;
	}

	public Product productDtoToEntity(ProductRequestDTO dto) {
		Product product = new Product();
		product.setProductName(dto.getProductName());
		product.setDescription(dto.getDescription());
		product.setImage(dto.getImage());
		product.setQuantity(dto.getQuantity());
		product.setPrice(dto.getPrice());
		product.setDiscount(dto.getDiscount());
		return product;
	}

	public ProductResponseDTO productEntityToDto(Product entity) {
		ProductResponseDTO dto = new ProductResponseDTO();
		dto.setProductId(entity.getProductId());
		dto.setProductName(entity.getProductName());
		dto.setDescription(entity.getDescription());
		dto.setImage(entity.getImage());
		dto.setPrice(entity.getPrice());
		dto.setDiscount(entity.getDiscount());
		dto.setSpecialPrice(entity.getSpecialPrice());
		dto.setCategoryId(entity.getCategory().getCategoryId());	
		return dto;
	}
}
