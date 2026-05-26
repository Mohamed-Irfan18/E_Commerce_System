package com.ecommerce.controller;

import com.ecommerce.dto.ProductResponse;
import com.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponse> getAll(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search) {
        if (categoryId != null) {
            return productService.getByCategory(categoryId);
        }
        if (search != null && !search.isBlank()) {
            return productService.search(search);
        }
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {
        return productService.getById(id);
    }
}
