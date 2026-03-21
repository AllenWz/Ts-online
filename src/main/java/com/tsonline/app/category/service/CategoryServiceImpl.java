package com.tsonline.app.category.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tsonline.app.category.dto.CategoryDtoRequest;
import com.tsonline.app.category.dto.CategoryDtoResponse;
import com.tsonline.app.category.dto.CategoryListResponse;
import com.tsonline.app.category.entity.Category;
import com.tsonline.app.category.repository.CategoryRespository;
import com.tsonline.app.common.exception.DuplicateEntryException;
import com.tsonline.app.common.exception.ResourceNotFoundException;
import com.tsonline.app.common.util.Mapper;

@Service
public class CategoryServiceImpl implements CategoryService {

	CategoryRespository repo;
	Mapper mapper;

	public CategoryServiceImpl(CategoryRespository repo, Mapper mapper) {
		this.repo = repo;
		this.mapper = mapper;
	}

	/**
	 * Retrieves a paginated list of categories converted to DTOs. * @param
	 * pageNumber the zero-based page index
	 * 
	 * @param pageSize  the size of the page to be returned
	 * @param sortBy    the field by which to sort the results
	 * @param sortOrder the direction of the sort ("asc" or "desc")
	 * @return a {@link CategoryListResponse} containing paginated data and metadata
	 */
	@Override
	public CategoryListResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy,
			String sortOrder) {
		Sort sort = sortOrder.equalsIgnoreCase("asc") 
						? Sort.by(sortBy).ascending() 
						: Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Category> categoryPage = repo.findAll(pageable);

		List<CategoryDtoResponse> dtoList = categoryPage.getContent().stream()
				.map(cat -> mapper.categoryEntityToDto(cat)).toList();

		CategoryListResponse categoryResponse = new CategoryListResponse();
		categoryResponse.setContent(dtoList);
		categoryResponse.setPageNumber(categoryPage.getNumber());
		categoryResponse.setPageSize(categoryPage.getSize());
		categoryResponse.setTotalElements(categoryPage.getTotalElements());
		categoryResponse.setTotalPages(categoryPage.getTotalPages());
		categoryResponse.setLastPage(categoryPage.isLast());

		return categoryResponse;
	}

	/**
	 * Creates a new category. Checks for duplicates and assigns audit information
	 * (creator and timestamp). * @param categoryToRegister the DTO containing the
	 * category name
	 * 
	 * @return the saved {@link CategoryDtoResponse}
	 * @throws DuplicateEntryException if a category with the same name already
	 *                                 exists
	 */
	@Override
	public CategoryDtoResponse registerCategory(CategoryDtoRequest categoryToRegister) {
		Category foundCategory = repo.findByCategoryName(categoryToRegister.getCategoryName());

		if (foundCategory != null)
			throw new DuplicateEntryException(
					"Cateogory with name " + categoryToRegister.getCategoryName() + " already exists");

		Category entity = mapper.categoryDtoToEntity(categoryToRegister);
		entity.setCreatedBy("admin");
		entity.setCreatedAt(LocalDateTime.now());
		Category savedCategory = repo.save(entity);

		return mapper.categoryEntityToDto(savedCategory);
	}

	/**
	 * Updates an existing category's name and audit metadata. * @param idToUpdate
	 * the ID of the category to modify
	 * 
	 * @param categoryToUpdate the DTO containing the new category details
	 * @return the updated {@link CategoryDtoResponse}
	 * @throws ResourceNotFoundException if the category ID does not exist
	 */
	@Override
	public CategoryDtoResponse updateCategory(Long idToUpdate, CategoryDtoRequest categoryToUpdate) {
		Category foundCategory = repo.findById(idToUpdate)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", idToUpdate));

		foundCategory.setCategoryName(categoryToUpdate.getCategoryName());
		foundCategory.setUpdatedBy("admin");
		foundCategory.setUpdatedAt(LocalDateTime.now());

		Category savedCategory = repo.save(foundCategory);
		return mapper.categoryEntityToDto(savedCategory);
	}

	/**
	 * Deletes a category by its unique identifier. * @param idToDelete the ID of
	 * the category to be removed
	 * 
	 * @return a success message or a "not found" message
	 */
	@Override
	public String deleteCategory(Long idToDelete) {
		Category foundCategory = repo.findById(idToDelete).orElse(null);

		if (foundCategory != null) {
			repo.deleteById(idToDelete);
			return "Category with id " + idToDelete + " deleted successfully!";
		}

		return "not found id " + idToDelete;
	}
}