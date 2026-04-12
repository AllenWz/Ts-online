package com.tsonline.app.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tsonline.app.config.AppConstants;
import com.tsonline.app.product.dto.ProductListResponse;
import com.tsonline.app.product.dto.ProductRequestDTO;
import com.tsonline.app.product.dto.ProductResponseDTO;
import com.tsonline.app.product.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {

	ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping("public/products")
	public ResponseEntity<ProductListResponse> getAllProducts(
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder,
			@RequestParam(defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy) {
		ProductListResponse response = productService.getAllProducts(pageNumber,pageSize,sortOrder,sortBy);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("public/category/{categoryId}/products")
	public ResponseEntity<ProductListResponse> getProductsByCategory(
			@PathVariable Long categoryId,
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder,
			@RequestParam(defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy) {
		ProductListResponse response = productService.findByCategory(pageNumber,pageSize,sortOrder,sortBy,categoryId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("public/products/keyword/{keyword}")
	public ResponseEntity<ProductListResponse> findProductByKeyword(
			@PathVariable String keyword,
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder,
			@RequestParam(defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy) {
		ProductListResponse response = productService.findProductByKeyword(pageNumber,pageSize,sortOrder,sortBy,keyword);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_ADMIN')")
	@PostMapping("/product")
	public ResponseEntity<ProductResponseDTO> addProduct(@RequestPart ProductRequestDTO product,
															@RequestParam MultipartFile image) {
		ProductResponseDTO body = productService.addProduct(product, image);
		return new ResponseEntity<>(body, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_ADMIN')")
	@PutMapping("/product/{productId}")
	public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long productId, 
															@RequestPart ProductRequestDTO dto,
															@RequestParam MultipartFile image) {
		ProductResponseDTO response = productService.updateProduct(productId, dto, image);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_ADMIN')")
	@DeleteMapping("/product/{productId}")
	public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
		productService.deleteProduct(productId);
		return new ResponseEntity<>("Product Deleted", HttpStatus.OK);
	}
}
