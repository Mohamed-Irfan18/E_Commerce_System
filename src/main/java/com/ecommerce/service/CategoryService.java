package com.ecommerce.service;

import com.ecommerce.dto.CategoryRequest;
import com.ecommerce.entity.Category;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Transactional
    public Category create(CategoryRequest request) {
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new BadRequestException("Category already exists");
        }
        return categoryRepository.save(Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build());
    }

    @Transactional
    public Category update(Long id, CategoryRequest request) {
        Category category = getById(id);
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return categoryRepository.save(category);
    }

    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
