package com.tsonline.app.product.service;

import org.springframework.web.multipart.MultipartFile;

import com.tsonline.app.product.dto.ProductListResponse;
import com.tsonline.app.product.dto.ProductRequestDTO;
import com.tsonline.app.product.dto.ProductResponseDTO;

public interface ProductService {
	ProductResponseDTO addProduct(ProductRequestDTO product, MultipartFile image);

	ProductListResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy);

	ProductListResponse findByCategory(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy, Long categoryId);

	ProductListResponse findProductByKeyword(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy, String keyword);

	ProductResponseDTO updateProduct(Long productId, ProductRequestDTO product, MultipartFile image);

	void deleteProduct(Long productId);
}
