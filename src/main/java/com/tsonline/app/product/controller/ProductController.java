package com.tsonline.app.product.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ProductController {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	

	@Operation(summary = "Get All Products", description = "API to get all registered products with selected pagnation")
	@GetMapping("public/products")
	public ResponseEntity<ProductListResponse> getAllProducts(
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder,
			@RequestParam(defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy) {
		
		logger.info("#ProductController#getAllProducts");
		ProductListResponse response = productService.getAllProducts(pageNumber,pageSize,sortOrder,sortBy);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@Operation(summary = "Get Products by category", description = "API to get products of selected category and selected pagnation")
	@GetMapping("public/category/{categoryId}/products")
	public ResponseEntity<ProductListResponse> getProductsByCategory(
			@PathVariable Long categoryId,
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder,
			@RequestParam(defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy) {
		
		logger.info("#ProductController#getProductsByCategory");
		ProductListResponse response = productService.findByCategory(pageNumber,pageSize,sortOrder,sortBy,categoryId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@Operation(summary = "Get Products by keyword", description = "API to get products of searched keyword and selected pagnation")
	@GetMapping("public/products/keyword/{keyword}")
	public ResponseEntity<ProductListResponse> findProductByKeyword(
			@PathVariable String keyword,
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder,
			@RequestParam(defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy) {
		
		logger.info("#ProductController#findProductByKeyword");
		ProductListResponse response = productService.findProductByKeyword(pageNumber,pageSize,sortOrder,sortBy,keyword);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@Operation(summary = "Register new product", description = "API to register new product and upload the product image")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
	    content = @Content(
	        encoding = @Encoding(name = "product", contentType = "application/json")
	    )
	)
	@PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_ADMIN')")
	@PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ProductResponseDTO> addProduct(@Valid @RequestPart("product") ProductRequestDTO product,
															@RequestPart("image") MultipartFile image) {
		
		logger.info("#ProductController#addProduct");
		ProductResponseDTO body = productService.addProduct(product, image);
		return new ResponseEntity<>(body, HttpStatus.CREATED);
	}
	
	
	@Operation(summary = "Update product", description = "API to update selected product and update the product image")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
	    content = @Content(
	        encoding = @Encoding(name = "product", contentType = "application/json")
	    )
	)
	@PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_ADMIN')")
	@PutMapping(value="/product/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ProductResponseDTO> updateProduct(@Valid @RequestPart("product") ProductRequestDTO product, 
															@PathVariable Long productId,
															@RequestPart("image") MultipartFile image) {
		
		logger.info("#ProductController#updateProduct");
		ProductResponseDTO response = productService.updateProduct(productId, product, image);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@Operation(summary = "Delete product", description = "API to delete selected product")
	@PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_ADMIN')")
	@DeleteMapping("/product/{productId}")
	public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
		
		logger.info("#ProductController#deleteProduct");
		productService.deleteProduct(productId);
		return new ResponseEntity<>("Product Deleted", HttpStatus.OK);
	}
}
