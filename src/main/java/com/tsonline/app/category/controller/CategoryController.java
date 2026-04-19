package com.tsonline.app.category.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.tsonline.app.category.dto.CategoryDtoRequest;
import com.tsonline.app.category.dto.CategoryDtoResponse;
import com.tsonline.app.category.dto.CategoryListResponse;
import com.tsonline.app.category.service.CategoryService;
import com.tsonline.app.config.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CategoryController {

	private final CategoryService service;

	public CategoryController(CategoryService service) {
		this.service = service;
	}

	/**
	 * Retrieves a paginated and sorted list of all categories.
	 * 
	 * @param pageNumber the page index to fetch (defaults to
	 *                   {@link AppConstants#PAGE_NUMBER})
	 * @param pageSize   the number of records per page (defaults to
	 *                   {@link AppConstants#PAGE_SIZE})
	 * @param sortBy     the field by which to sort the results (defaults to
	 *                   {@link AppConstants#SORT_CATEGORY_BY})
	 * @param sortOrder  the direction of sorting, e.g., "asc" or "desc" (defaults
	 *                   to {@link AppConstants#SORT_ORDER})
	 * @return a {@link ResponseEntity} containing the {@link CategoryListResponse}
	 *         and HTTP status 200 (OK)
	 */
	@GetMapping("/public/categories")
	public ResponseEntity<CategoryListResponse> getAllCategories(
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_CATEGORY_BY, required = false) String sortBy,
			@RequestParam(defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder) {
		CategoryListResponse categoryList = service.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
		return new ResponseEntity<>(categoryList, HttpStatus.OK);
	}

	/**
	 * Registers a new category in the system.
	 * 
	 * @param category the DTO containing category details to be registered
	 * @return a {@link ResponseEntity} containing the created
	 *         {@link CategoryDtoResponse} and HTTP status 201 (Created)
	 */
	@PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_ADMIN')")
	@PostMapping("/categories")
	public ResponseEntity<CategoryDtoResponse> registerCategory(@Valid @RequestBody CategoryDtoRequest category) {
		CategoryDtoResponse body = service.registerCategory(category);
		return new ResponseEntity<>(body, HttpStatus.CREATED);
	}

	/**
	 * Updates an existing category identified by its ID.
	 * 
	 * @param id       the unique identifier of the category to be updated
	 * @param category the DTO containing the updated category information
	 * @return a {@link ResponseEntity} containing the updated
	 *         {@link CategoryDtoResponse} and HTTP status 200 (OK)
	 */
	@PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_ADMIN')")
	@PutMapping("/categories/{id}")
	public ResponseEntity<CategoryDtoResponse> updateCategory(@Valid @RequestBody CategoryDtoRequest category,
			@PathVariable Long id) {
		CategoryDtoResponse body = service.updateCategory(id, category);
		return new ResponseEntity<>(body, HttpStatus.OK);
	}

	/**
	 * Deletes a category from the system based on the provided ID.
	 * 
	 * @param id the unique identifier of the category to be deleted
	 * @return a {@link ResponseEntity} containing a success message string and HTTP
	 *         status 200 (OK)
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/categories/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
		String body = service.deleteCategory(id);
		return new ResponseEntity<>(body, HttpStatus.OK);
	}
}