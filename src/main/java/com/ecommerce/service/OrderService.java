package com.ecommerce.service;

import com.ecommerce.dto.OrderItemResponse;
import com.ecommerce.dto.OrderRequest;
import com.ecommerce.dto.OrderResponse;
import com.ecommerce.entity.*;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        User user = getCurrentUser();
        Cart cart = cartService.getUserCartForOrder();

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .shippingAddress(request.getShippingAddress())
                .totalAmount(BigDecimal.ZERO)
                .items(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new BadRequestException("Insufficient stock for: " + product.getName());
            }

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            BigDecimal unitPrice = product.getPrice();
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .unitPrice(unitPrice)
                    .build();
            order.getItems().add(orderItem);
            total = total.add(unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        order.setTotalAmount(total);
        Order saved = orderRepository.save(order);
        cartService.clearCart(cart);

        return toResponse(saved);
    }

    public List<OrderResponse> getMyOrders() {
        User user = getCurrentUser();
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream().map(this::toResponse).toList();
    }

    public OrderResponse getOrderById(Long id) {
        Order order = findOrder(id);
        User user = getCurrentUser();
        if (!order.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new BadRequestException("Access denied");
        }
        return toResponse(order);
    }

    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatus status) {
        Order order = findOrder(id);
        order.setStatus(status);
        return toResponse(orderRepository.save(order));
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(this::toResponse).toList();
    }

    private Order findOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    private User getCurrentUser() {
        CustomUserDetails details = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return details.getUser();
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream().map(item ->
                OrderItemResponse.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subtotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .build()
        ).toList();

        return OrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .createdAt(order.getCreatedAt())
                .items(items)
                .build();
    }
}
