package com.tsonline.app.category.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

	CategoryRespository repo;
	Mapper mapper;

	public CategoryServiceImpl(CategoryRespository repo, Mapper mapper) {
		this.repo = repo;
		this.mapper = mapper;
	}


	@Override
	public CategoryListResponse getAllCategories(Integer pageNumber, Integer pageSize, 
												String sortBy, String sortOrder) {
		logger.info("#CategoryServiceImpl#getAllCategories");
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

	
	@Override
	public CategoryDtoResponse registerCategory(CategoryDtoRequest categoryToRegister) {
		logger.info("#CategoryServiceImpl#registerCategory");
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

	
	@Override
	public CategoryDtoResponse updateCategory(Long idToUpdate, CategoryDtoRequest categoryToUpdate) {
		logger.info("#CategoryServiceImpl#updateCategory");
		Category foundCategory = repo.findById(idToUpdate)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", idToUpdate));

		foundCategory.setCategoryName(categoryToUpdate.getCategoryName());
		foundCategory.setUpdatedBy("admin");
		foundCategory.setUpdatedAt(LocalDateTime.now());

		Category savedCategory = repo.save(foundCategory);
		return mapper.categoryEntityToDto(savedCategory);
	}

	
	@Override
	public void deleteCategory(Long idToDelete) {
		logger.info("#CategoryServiceImpl#deleteCategory");
		Category foundCategory = repo.findById(idToDelete)
										.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", idToDelete));
		foundCategory.setDeleted(true);
		repo.save(foundCategory);
	}
}