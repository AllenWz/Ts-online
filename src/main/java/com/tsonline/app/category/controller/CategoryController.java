package com.tsonline.app.category.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.tsonline.app.category.dto.CategoryDtoRequest;
import com.tsonline.app.category.dto.CategoryDtoResponse;
import com.tsonline.app.category.dto.CategoryListResponse;
import com.tsonline.app.category.service.CategoryService;
import com.tsonline.app.config.AppConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CategoryController {
	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

	private final CategoryService service;

	public CategoryController(CategoryService service) {
		this.service = service;
	}
	

	@Operation(summary = "Get All Categories", description = "API to get all registered categories")
	@ApiResponses({
		@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
		@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
	})
	@GetMapping("/public/categories")
	public ResponseEntity<CategoryListResponse> getAllCategories(
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_CATEGORY_BY, required = false) String sortBy,
			@RequestParam(defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder) {	
		
		logger.info("#CategoryController#getAllCategories");
		CategoryListResponse categoryList = service.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
		return new ResponseEntity<>(categoryList, HttpStatus.OK);
	}

	
	@Operation(summary = "Register New Category", description = "API to register new category. Only login user with seller or admin authority can register new category.")
	@PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_ADMIN')")
	@PostMapping("/categories")
	public ResponseEntity<CategoryDtoResponse> registerCategory(@Valid @RequestBody CategoryDtoRequest category) {
		logger.info("#CategoryController#registerCategory");
		CategoryDtoResponse body = service.registerCategory(category);
		return new ResponseEntity<>(body, HttpStatus.CREATED);
	}

	
	@Operation(summary = "Update Category", description = "API to update selected category. Only login user with seller or admin authority can update category.")
	@PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_ADMIN')")
	@PutMapping("/categories/{id}")
	public ResponseEntity<CategoryDtoResponse> updateCategory(@Valid @RequestBody CategoryDtoRequest category,
			@PathVariable Long id) {
		logger.info("#CategoryController#updateCategory");
		CategoryDtoResponse body = service.updateCategory(id, category);
		return new ResponseEntity<>(body, HttpStatus.OK);
	}

	
	@Operation(summary = "Delete Category", description = "API to delete selected category. Only login user with admin authority can delete category.")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/categories/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
		logger.info("#CategoryController#deleteCategory");
		service.deleteCategory(id);
		return new ResponseEntity<>("Category with id " + id + " deleted successfully!", HttpStatus.OK);
	}
}