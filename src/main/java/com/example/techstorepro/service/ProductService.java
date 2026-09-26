package com.example.techstorepro.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.techstorepro.dto.request.ProductRequest;
import com.example.techstorepro.dto.response.ProductResponse;
import com.example.techstorepro.dto.update.UpdateProductRequest;
import com.example.techstorepro.entity.Category;
import com.example.techstorepro.entity.Product;
import com.example.techstorepro.exception.BadRequestException;
import com.example.techstorepro.exception.ResourceNotFoundException;
import com.example.techstorepro.mapper.ProductMapper;
import com.example.techstorepro.repository.ProductRepository;
import com.example.techstorepro.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final CloudinaryService cloudinaryService;
    private final ReviewRepository reviewRepository;

    public Page<ProductResponse> findAll(UUID categoryId, String name, Boolean onlyActive, Pageable pageable) {
        boolean activeFilter = (onlyActive == null) || onlyActive;
        String cleanName = (name != null && !name.isBlank()) ? name.trim() : null;

        return productRepository.findByFilters(categoryId, cleanName, activeFilter, pageable)
                .map(this::toResponseWithRating);
    }

    public ProductResponse findById(UUID id) {
        return toResponseWithRating(getProduct(id));
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Category category = categoryService.getCategory(request.getCategoryId());
        Product product = ProductMapper.toEntity(request, category);

        Map<String, Object> result = cloudinaryService.upload(request.getImage());
        product.setImageUrl((String) result.get("secure_url"));
        product.setPublicId((String) result.get("public_id"));

        return toResponseWithRating(productRepository.save(product));
    }

    @Transactional
    public ProductResponse update(UUID id, UpdateProductRequest request) {
        Product product = getProduct(id);
        Category category = categoryService.getCategory(request.getCategoryId());
        ProductMapper.updateEntity(product, request, category);

        if (request.getImage() == null || request.getImage().isEmpty()) {
            return toResponseWithRating(productRepository.save(product));
        }

        String oldPublicId = product.getPublicId();
        Map<String, Object> result = cloudinaryService.upload(request.getImage());
        product.setImageUrl((String) result.get("secure_url"));
        product.setPublicId((String) result.get("public_id"));

        Product saved = productRepository.save(product);
        if (oldPublicId != null && !oldPublicId.equals(saved.getPublicId())) {
            try {
                cloudinaryService.delete(oldPublicId);
            } catch (BadRequestException e) {
            }
        }
        return toResponseWithRating(saved);
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

    private ProductResponse toResponseWithRating(Product product) {
        ProductResponse response = ProductMapper.toResponse(product);
        response.setAverageRating(reviewRepository.findAverageRatingByProductId(product.getId()));
        response.setReviewCount(reviewRepository.countByProduct_Id(product.getId()));
        return response;
    }
}
