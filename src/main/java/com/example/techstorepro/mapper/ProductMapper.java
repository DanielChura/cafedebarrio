package com.example.techstorepro.mapper;

import com.example.techstorepro.dto.request.ProductRequest;
import com.example.techstorepro.dto.response.ProductResponse;
import com.example.techstorepro.entity.Category;
import com.example.techstorepro.entity.Product;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static Product toEntity(ProductRequest request, Category category) {
        Product product = new Product();
        updateEntity(product, request, category);
        return product;
    }

    public static void updateEntity(Product product, ProductRequest request, Category category) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);
    }

    public static ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setImageUrl(product.getImageUrl());
        response.setActive(product.getActive());
        if (product.getCategory() != null) {
            response.setCategoryId(product.getCategory().getId());
            response.setCategoryName(product.getCategory().getName());
        }
        return response;
    }
}
