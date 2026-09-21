package com.example.techstorepro.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.techstorepro.dto.request.CategoryRequest;
import com.example.techstorepro.dto.response.CategoryResponse;
import com.example.techstorepro.entity.Category;
import com.example.techstorepro.exception.ResourceNotFoundException;
import com.example.techstorepro.mapper.CategoryMapper;
import com.example.techstorepro.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Page<CategoryResponse> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(c -> CategoryMapper.toResponse(c));
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
