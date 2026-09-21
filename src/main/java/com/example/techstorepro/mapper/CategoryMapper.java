package com.example.techstorepro.mapper;

import com.example.techstorepro.dto.request.CategoryRequest;
import com.example.techstorepro.dto.response.CategoryResponse;
import com.example.techstorepro.entity.Category;

public final class CategoryMapper {

    private CategoryMapper() {
    }

    public static Category toEntity(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        return category;
    }

    public static void updateEntity(Category category, CategoryRequest request) {
        category.setName(request.getName());
    }

    public static CategoryResponse toResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        return response;
    }
}
