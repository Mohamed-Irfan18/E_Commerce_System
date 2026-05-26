package com.ecommerce.service;

import com.ecommerce.dto.CartItemRequest;
import com.ecommerce.dto.CartItemResponse;
import com.ecommerce.dto.CartResponse;
import com.ecommerce.entity.*;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartResponse getCart() {
        Cart cart = getUserCart();
        return toResponse(cart);
    }

    @Transactional
    public CartResponse addItem(CartItemRequest request) {
        Cart cart = getUserCart();
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getStock() < request.getQuantity()) {
            throw new BadRequestException("Insufficient stock for product: " + product.getName());
        }

        CartItem existing = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            int newQty = existing.getQuantity() + request.getQuantity();
            if (product.getStock() < newQty) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName());
            }
            existing.setQuantity(newQty);
        } else {
            cart.getItems().add(CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build());
        }

        return toResponse(cartRepository.save(cart));
    }

    @Transactional
    public CartResponse updateItemQuantity(Long itemId, Integer quantity) {
        Cart cart = getUserCart();
        CartItem item = findCartItem(cart, itemId);

        if (quantity <= 0) {
            cart.getItems().remove(item);
        } else {
            if (item.getProduct().getStock() < quantity) {
                throw new BadRequestException("Insufficient stock");
            }
            item.setQuantity(quantity);
        }

        return toResponse(cartRepository.save(cart));
    }

    @Transactional
    public CartResponse removeItem(Long itemId) {
        Cart cart = getUserCart();
        CartItem item = findCartItem(cart, itemId);
        cart.getItems().remove(item);
        cartItemRepository.delete(item);
        return toResponse(cart);
    }

    @Transactional
    public void clearCart(Cart cart) {
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public Cart getUserCartForOrder() {
        return getUserCart();
    }

    private Cart getUserCart() {
        User user = getCurrentUser();
        return cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    private CartItem findCartItem(Cart cart, Long itemId) {
        return cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
    }

    private User getCurrentUser() {
        CustomUserDetails details = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return details.getUser();
    }

    private CartResponse toResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream().map(item -> {
            BigDecimal subtotal = item.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            return CartItemResponse.builder()
                    .id(item.getId())
                    .productId(item.getProduct().getId())
                    .productName(item.getProduct().getName())
                    .quantity(item.getQuantity())
                    .unitPrice(item.getProduct().getPrice())
                    .subtotal(subtotal)
                    .build();
        }).toList();

        BigDecimal total = items.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .cartId(cart.getId())
                .items(items)
                .totalAmount(total)
                .build();
    }
}
