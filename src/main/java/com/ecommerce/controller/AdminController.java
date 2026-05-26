package com.ecommerce.controller;

import com.ecommerce.dto.CategoryRequest;
import com.ecommerce.dto.ProductRequest;
import com.ecommerce.dto.ProductResponse;
import com.ecommerce.entity.Category;
import com.ecommerce.entity.OrderStatus;
import com.ecommerce.dto.OrderResponse;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@Valid @RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @PutMapping("/products/{id}")
    public ProductResponse updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.delete(id);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@Valid @RequestBody CategoryRequest request) {
        return categoryService.create(request);
    }

    @PutMapping("/categories/{id}")
    public Category updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return categoryService.update(id, request);
    }

    @DeleteMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
    }

    @GetMapping("/orders")
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PatchMapping("/orders/{id}/status")
    public OrderResponse updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return orderService.updateStatus(id, status);
    }
}
