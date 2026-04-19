package com.tsonline.app.product.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tsonline.app.cart.dto.CartResponseDto;
import com.tsonline.app.cart.entity.Cart;
import com.tsonline.app.cart.repository.CartRepository;
import com.tsonline.app.cart.service.CartService;
import com.tsonline.app.cart.service.CartServiceImpl;
import com.tsonline.app.category.entity.Category;
import com.tsonline.app.category.repository.CategoryRespository;
import com.tsonline.app.common.exception.ResourceNotFoundException;
import com.tsonline.app.common.util.FileService;
import com.tsonline.app.common.util.Mapper;
import com.tsonline.app.product.dto.ProductListResponse;
import com.tsonline.app.product.dto.ProductRequestDTO;
import com.tsonline.app.product.dto.ProductResponseDTO;
import com.tsonline.app.product.entity.Product;
import com.tsonline.app.product.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {
	private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
	
	Mapper mapper;
	
	FileService fileService;

	CartService cartService;
	
	ProductRepository productRepo;
	
	CategoryRespository categoryRepo;
	
	CartRepository cartRepository;

	public ProductServiceImpl(ProductRepository productRepo, CategoryRespository categoryRepo, 
			Mapper mapper, FileService fileUtil, CartService cartService, CartRepository cartRepository) {
		this.productRepo = productRepo;
		this.categoryRepo = categoryRepo;
		this.mapper = mapper;
		this.fileService = fileUtil;
		this.cartService = cartService;
		this.cartRepository = cartRepository;
	}

	@Override
	public ProductResponseDTO addProduct(ProductRequestDTO product, MultipartFile image) {
		logger.info("#ProductServiceImpl#addProduct");
		Category category = categoryRepo.findById(product.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Category", "category ID", product.getCategoryId()));
		
		String fileName = null;
		try {
			fileName = fileService.uploadFileName(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		product.setImageName(fileName);
		
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
		logger.info("#ProductServiceImpl#getAllProducts");
		Sort sort = sortOrder.equalsIgnoreCase("asc") 
						? Sort.by(sortBy).ascending() 
						: Sort.by(sortBy).descending();
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> productPage = productRepo.findAll(pageable);
		List<ProductResponseDTO> list = productPage.stream()
				.map((product) -> {
					ProductResponseDTO dto = mapper.productEntityToDto(product);
					// Presign url setting
					String url = fileService.getPresignedUrl(dto.getImageName());
					dto.setImageUrl(url);
					return dto;
				}).toList();

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
		logger.info("#ProductServiceImpl#findByCategory");
		Category category = categoryRepo.findById(categoryId)
								.orElseThrow(() -> new ResourceNotFoundException("Category", "category ID", categoryId));
		
		Sort sort = sortOrder.equalsIgnoreCase("asc") 
				? Sort.by(sortBy).ascending() 
				: Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<Product> productPage = productRepo.findByCategory(category, pageable);
		List<ProductResponseDTO> productList = productPage.stream()
				.map(product -> {
					ProductResponseDTO dto = mapper.productEntityToDto(product);
					// Presign url setting
					String url = fileService.getPresignedUrl(dto.getImageName());
					dto.setImageUrl(url);
					return dto;
				}).toList();
		
		ProductListResponse response = new ProductListResponse();
		response.setContent(productList);
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
		logger.info("#ProductServiceImpl#findProductByKeyword");
		Sort sort = sortOrder.equalsIgnoreCase("asc") 
				? Sort.by(sortBy).ascending() 
				: Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<Product> productPage = productRepo.findByProductNameLikeIgnoreCase("%" + keyword + "%", pageable);
		List<ProductResponseDTO> productList = productPage.stream()
				.map(product -> {
					ProductResponseDTO dto = mapper.productEntityToDto(product);
					// Presign url setting
					String url = fileService.getPresignedUrl(dto.getImageName());
					dto.setImageUrl(url);
					return dto;
				}).toList();
		
		ProductListResponse response = new ProductListResponse();
		response.setContent(productList);
		response.setPageNumber(productPage.getNumber());
		response.setPageSize(productPage.getSize());
		response.setTotalElements(productPage.getTotalElements());
		response.setTotalPages(productPage.getTotalPages());
		response.setLastPage(productPage.isLast());
		return response;
	}

	@Transactional
	@Override
	public ProductResponseDTO updateProduct(Long productId, ProductRequestDTO dto, MultipartFile image) {
		logger.info("#ProductServiceImpl#updateProduct");
		Product product = productRepo.findById(productId)
							.orElseThrow(() -> new ResourceNotFoundException("Product", "Product ID", productId));
		
		Category category = categoryRepo.findById(dto.getCategoryId())
								.orElseThrow(() -> new ResourceNotFoundException("Category", "Category ID", dto.getCategoryId()));
		
		if (image != null && !image.isEmpty()) {
			String fileName = null;
			try {
				fileName = fileService.uploadFileName(image);
			} catch (IOException e) {
				e.printStackTrace();
			}
			product.setImage(fileName);
		} else {
			product.setImage(dto.getImageName());
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
		
		List<Cart> carts = cartRepository.findCartsByProductId(savedProduct.getProductId());
		cartService.updateProductInCarts(carts, savedProduct);
		
		ProductResponseDTO response = mapper.productEntityToDto(savedProduct);
		return response;
	}
	
	@Override
	public void deleteProduct(Long productId) {
		logger.info("#ProductServiceImpl#deleteProduct");
		Product product = productRepo.findById(productId)
								.orElseThrow(() -> new ResourceNotFoundException());
		product.setDeleted(true);	
		productRepo.save(product);	
	}

	private Double calculateDiscount(ProductRequestDTO dto) {
		logger.info("#ProductServiceImpl#calculateDiscount");
		Double originalPrice = dto.getPrice();
		Double discountRate = dto.getDiscount();
		Double discountPrice = originalPrice - ((discountRate/100)*originalPrice);
		return discountPrice;
	}

}
