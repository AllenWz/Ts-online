package com.tsonline.app.category.service;

import com.tsonline.app.category.dto.CategoryDtoRequest;
import com.tsonline.app.category.dto.CategoryDtoResponse;
import com.tsonline.app.category.dto.CategoryListResponse;

public interface CategoryService {
	CategoryListResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);
	
	CategoryDtoResponse registerCategory(CategoryDtoRequest categoryToRegister);
	
	CategoryDtoResponse updateCategory(Long idToUpdate, CategoryDtoRequest categoryToUpdate);
	
	String deleteCategory(Long idToDelete);
}
