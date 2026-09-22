package com.example.techstorepro.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.techstorepro.dto.request.ProductRequest;
import com.example.techstorepro.dto.response.ProductResponse;
import com.example.techstorepro.entity.Category;
import com.example.techstorepro.entity.Product;
import com.example.techstorepro.exception.ResourceNotFoundException;
import com.example.techstorepro.mapper.ProductMapper;
import com.example.techstorepro.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public Page<ProductResponse> findAll(UUID categoryId, String name, Boolean onlyActive, Pageable pageable) {
        boolean activeFilter = (onlyActive == null) || onlyActive;
        String cleanName = (name != null && !name.isBlank()) ? name.trim() : null;

        return productRepository.findByFilters(categoryId, cleanName, activeFilter, pageable)
                .map(p -> ProductMapper.toResponse(p));
    }

    public ProductResponse findById(UUID id) {
        return ProductMapper.toResponse(getProduct(id));
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Category category = categoryService.getCategory(request.getCategoryId());
        Product product = ProductMapper.toEntity(request, category);
        return ProductMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse update(UUID id, ProductRequest request) {
        Product product = getProduct(id);
        Category category = categoryService.getCategory(request.getCategoryId());
        ProductMapper.updateEntity(product, request, category);
        return ProductMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public void deleteById(UUID id) {
        Product product = getProduct(id);
        product.setActive(false);
        productRepository.save(product);
    }

    private Product getProduct(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }
}
