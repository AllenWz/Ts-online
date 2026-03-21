package com.tsonline.app.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tsonline.app.category.entity.Category;
import com.tsonline.app.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	Page<Product> findByCategory(Category category, Pageable pageable);

	Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageable);
}
