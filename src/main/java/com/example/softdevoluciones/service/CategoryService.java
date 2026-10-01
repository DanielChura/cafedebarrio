package com.example.softdevoluciones.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.softdevoluciones.dto.request.CategoryRequest;
import com.example.softdevoluciones.dto.response.CategoryResponse;
import com.example.softdevoluciones.entity.Category;
import com.example.softdevoluciones.exception.ResourceNotFoundException;
import com.example.softdevoluciones.mapper.CategoryMapper;
import com.example.softdevoluciones.repository.CategoryRepository;

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
        return CategoryMapper.toResponse(getCategory(id));
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        Category category = CategoryMapper.toEntity(request);
        return CategoryMapper.toResponse(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse update(UUID id, CategoryRequest request) {
        Category category = getCategory(id);
        CategoryMapper.updateEntity(category, request);
        return CategoryMapper.toResponse(categoryRepository.save(category));
    }

    @Transactional
    public void deleteById(UUID id) {
        Category category = getCategory(id);
        categoryRepository.delete(category);
    }

    public Category getCategory(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "La categoría solicitada no existe o fue eliminada. Por favor, verifica la información e inténtalo de nuevo."));
    }
}
