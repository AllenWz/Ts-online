package com.tsonline.app.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsonline.app.category.entity.Category;

public interface CategoryRespository extends JpaRepository<Category, Long>{
	Category findByCategoryName(String categoryName);
}
