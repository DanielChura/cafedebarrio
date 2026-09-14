package com.example.cafedebarrio.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cafedebarrio.dto.request.ProductRequest;
import com.example.cafedebarrio.dto.response.ProductResponse;
import com.example.cafedebarrio.entity.Category;
import com.example.cafedebarrio.entity.Product;
import com.example.cafedebarrio.exception.ResourceNotFoundException;
import com.example.cafedebarrio.mapper.ProductMapper;
import com.example.cafedebarrio.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public List<ProductResponse> findAll(UUID categoryId, String name, Boolean onlyActive) {
        boolean activeFilter = (onlyActive == null) || onlyActive;
        String cleanName = (name != null && !name.isBlank()) ? name.trim() : null;

        return productRepository.findByFilters(categoryId, cleanName, activeFilter)
                .stream()
                .map(p -> ProductMapper.toResponse(p))
                .toList();
    }

    public ProductResponse findById(UUID id) {
        return ProductMapper.toResponse(findProductByIdOrThrow(id));
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Category category = categoryService.findCategoryByIdOrThrow(request.getCategoryId());
        Product product = ProductMapper.toEntity(request, category);
        return ProductMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse update(UUID id, ProductRequest request) {
        Product product = findProductByIdOrThrow(id);
        Category category = categoryService.findCategoryByIdOrThrow(request.getCategoryId());
        ProductMapper.updateEntity(product, request, category);
        return ProductMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public void deleteById(UUID id) {
        Product product = findProductByIdOrThrow(id);
        product.setIsActive(false);
        productRepository.save(product);
    }

    private Product findProductByIdOrThrow(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }
}
