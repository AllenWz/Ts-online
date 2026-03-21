package com.tsonline.app.product.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tsonline.app.category.entity.Category;
import com.tsonline.app.category.repository.CategoryRespository;
import com.tsonline.app.common.exception.ResourceNotFoundException;
import com.tsonline.app.common.util.FileUtil;
import com.tsonline.app.common.util.Mapper;
import com.tsonline.app.product.dto.ProductListResponse;
import com.tsonline.app.product.dto.ProductRequestDTO;
import com.tsonline.app.product.dto.ProductResponseDTO;
import com.tsonline.app.product.entity.Product;
import com.tsonline.app.product.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	ProductRepository productRepo;
	CategoryRespository categoryRepo;
	Mapper mapper;
	FileUtil fileUtil;

	public ProductServiceImpl(ProductRepository productRepo, CategoryRespository categoryRepo, Mapper mapper, FileUtil fileUtil) {
		this.productRepo = productRepo;
		this.categoryRepo = categoryRepo;
		this.mapper = mapper;
		this.fileUtil = fileUtil;
	}

	@Override
	public ProductResponseDTO addProduct(ProductRequestDTO product, MultipartFile image) {
		Category category = categoryRepo.findById(product.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Category", "category ID", product.getCategoryId()));
		
		String fileName = null;
		try {
			fileName = fileUtil.uploadFileName(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		product.setImage(fileName);
		
		Product productEntity = mapper.productDtoToEntity(product);
		productEntity.setSpecialPrice(calculateDiscount(product));
		productEntity.setCreatedBy("admin");
		productEntity.setCreatedAt(LocalDateTime.now());
		productEntity.setCategory(category);

		Product savedProduct = productRepo.save(productEntity);
		ProductResponseDTO response = mapper.productEntityToDto(savedProduct);
		return response;
	}

	@Override
	public ProductListResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy) {
		Sort sort = sortOrder.equalsIgnoreCase("asc") 
						? Sort.by(sortBy).ascending() 
						: Sort.by(sortBy).descending();
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> productPage = productRepo.findAll(pageable);
		List<ProductResponseDTO> list = productPage.stream()
				.map((product) -> mapper.productEntityToDto(product)).toList();

		ProductListResponse response = new ProductListResponse();
		response.setContent(list);
		response.setPageNumber(productPage.getNumber());
		response.setPageSize(productPage.getSize());
		response.setTotalElements(productPage.getTotalElements());
		response.setTotalPages(productPage.getTotalPages());
		response.setLastPage(productPage.isLast());
		return response;
	}

	@Override
	public ProductListResponse findByCategory(Integer pageNumber, Integer pageSize, 
			String sortOrder, String sortBy, Long categoryId) {
		Category category = categoryRepo.findById(categoryId)
								.orElseThrow(() -> new ResourceNotFoundException("Category", "category ID", categoryId));
		
		Sort sort = sortOrder.equalsIgnoreCase("asc") 
				? Sort.by(sortBy).ascending() 
				: Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<Product> productPage = productRepo.findByCategory(category, pageable);
		List<ProductResponseDTO> list = productPage.stream()
				.map(product -> mapper.productEntityToDto(product))
				.toList();
		
		ProductListResponse response = new ProductListResponse();
		response.setContent(list);
		response.setPageNumber(productPage.getNumber());
		response.setPageSize(productPage.getSize());
		response.setTotalElements(productPage.getTotalElements());
		response.setTotalPages(productPage.getTotalPages());
		response.setLastPage(productPage.isLast());
		return response;
	}

	@Override
	public ProductListResponse findProductByKeyword(Integer pageNumber, Integer pageSize, 
			String sortOrder, String sortBy, String keyword) {
		
		Sort sort = sortOrder.equalsIgnoreCase("asc") 
				? Sort.by(sortBy).ascending() 
				: Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<Product> productPage = productRepo.findByProductNameLikeIgnoreCase("%" + keyword + "%", pageable);
		List<ProductResponseDTO> list = productPage.stream()
				.map(product -> mapper.productEntityToDto(product))
				.toList();
		
		ProductListResponse response = new ProductListResponse();
		response.setContent(list);
		response.setPageNumber(productPage.getNumber());
		response.setPageSize(productPage.getSize());
		response.setTotalElements(productPage.getTotalElements());
		response.setTotalPages(productPage.getTotalPages());
		response.setLastPage(productPage.isLast());
		return response;
	}

	@Override
	public ProductResponseDTO updateProduct(Long productId, ProductRequestDTO dto, MultipartFile image) {
		Product product = productRepo.findById(productId)
							.orElseThrow(() -> new ResourceNotFoundException("Product", "Product ID", productId));
		
		Category category = categoryRepo.findById(dto.getCategoryId())
								.orElseThrow(() -> new ResourceNotFoundException("Category", "Category ID", dto.getCategoryId()));
		
		if (image != null && !image.isEmpty()) {
			String fileName = null;
			try {
				fileName = fileUtil.uploadFileName(image);
			} catch (IOException e) {
				e.printStackTrace();
			}
			product.setImage(fileName);
		} else {
			product.setImage(dto.getImage());
		}
		
		product.setProductName(dto.getProductName());
		product.setDescription(dto.getDescription());
		product.setPrice(dto.getPrice());
		product.setDiscount(dto.getDiscount());
		product.setSpecialPrice(calculateDiscount(dto));
		product.setCategory(category);
		product.setUpdatedAt(LocalDateTime.now());
		product.setUpdatedBy("admin");
		
		Product savedProduct = productRepo.save(product);
		ProductResponseDTO response = mapper.productEntityToDto(savedProduct);
		return response;
	}
	
	@Override
	public void deleteProduct(Long productId) {
		Product product = productRepo.findById(productId)
								.orElseThrow(() -> new ResourceNotFoundException());
		
		productRepo.delete(product);	
	}

	private Double calculateDiscount(ProductRequestDTO dto) {
		Double originalPrice = dto.getPrice();
		Double discountRate = dto.getDiscount();
		Double discountPrice = originalPrice - ((discountRate/100)*originalPrice);
		return discountPrice;
	}

}
