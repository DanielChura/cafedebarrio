package com.example.cafedebarrio.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cafedebarrio.dto.request.CategoryRequest;
import com.example.cafedebarrio.dto.response.CategoryResponse;
import com.example.cafedebarrio.entity.Category;
import com.example.cafedebarrio.exception.ResourceNotFoundException;
import com.example.cafedebarrio.mapper.CategoryMapper;
import com.example.cafedebarrio.repository.CategoryRepository;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> CategoryMapper.toResponse(c))
                .toList();
    }

    public CategoryResponse findById(UUID id) {
        return CategoryMapper.toResponse(findCategoryByIdOrThrow(id));
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        Category category = CategoryMapper.toEntity(request);
        return CategoryMapper.toResponse(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse update(UUID id, CategoryRequest request) {
        Category category = findCategoryByIdOrThrow(id);
        CategoryMapper.updateEntity(category, request);
        return CategoryMapper.toResponse(categoryRepository.save(category));
    }

    @Transactional
    public void deleteById(UUID id) {
        Category category = findCategoryByIdOrThrow(id);
        categoryRepository.delete(category);
    }

    public Category findCategoryByIdOrThrow(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
    }
}
