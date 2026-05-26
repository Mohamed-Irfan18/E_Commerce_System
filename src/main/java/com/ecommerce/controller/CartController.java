package com.ecommerce.controller;

import com.ecommerce.dto.CartItemRequest;
import com.ecommerce.dto.CartResponse;
import com.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public CartResponse getCart() {
        return cartService.getCart();
    }

    @PostMapping("/items")
    public CartResponse addItem(@Valid @RequestBody CartItemRequest request) {
        return cartService.addItem(request);
    }

    @PutMapping("/items/{itemId}")
    public CartResponse updateItem(@PathVariable Long itemId, @RequestParam Integer quantity) {
        return cartService.updateItemQuantity(itemId, quantity);
    }

    @DeleteMapping("/items/{itemId}")
    public CartResponse removeItem(@PathVariable Long itemId) {
        return cartService.removeItem(itemId);
    }
}
