package com.ecommerce.config;

import com.ecommerce.entity.*;
import com.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        User admin = userRepository.save(User.builder()
                .email("admin@shop.com")
                .password(passwordEncoder.encode("admin123"))
                .fullName("Store Admin")
                .role(Role.ADMIN)
                .build());

        User customer = userRepository.save(User.builder()
                .email("customer@shop.com")
                .password(passwordEncoder.encode("customer123"))
                .fullName("John Customer")
                .role(Role.CUSTOMER)
                .build());

        cartRepository.save(Cart.builder().user(admin).build());
        cartRepository.save(Cart.builder().user(customer).build());

        Category electronics = categoryRepository.save(Category.builder()
                .name("Electronics")
                .description("Gadgets and devices")
                .build());

        Category clothing = categoryRepository.save(Category.builder()
                .name("Clothing")
                .description("Apparel and accessories")
                .build());

        Category books = categoryRepository.save(Category.builder()
                .name("Books")
                .description("Books and stationery")
                .build());

        productRepository.save(Product.builder()
                .name("Wireless Headphones")
                .description("Noise-cancelling Bluetooth headphones")
                .price(new BigDecimal("79.99"))
                .stock(50)
                .imageUrl("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&q=80")
                .category(electronics)
                .build());

        productRepository.save(Product.builder()
                .name("Smart Watch")
                .description("Fitness tracking smart watch")
                .price(new BigDecimal("199.99"))
                .stock(30)
                .imageUrl("https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&q=80")
                .category(electronics)
                .build());

        productRepository.save(Product.builder()
                .name("Cotton T-Shirt")
                .description("Comfortable casual t-shirt")
                .price(new BigDecimal("24.99"))
                .stock(100)
                .imageUrl("https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=600&q=80")
                .category(clothing)
                .build());

        productRepository.save(Product.builder()
                .name("Denim Jeans")
                .description("Classic fit denim jeans")
                .price(new BigDecimal("49.99"))
                .stock(60)
                .imageUrl("https://images.unsplash.com/photo-1542272604-787c3835535d?w=600&q=80")
                .category(clothing)
                .build());

        productRepository.save(Product.builder()
                .name("Java Programming Book")
                .description("Learn Java from scratch")
                .price(new BigDecimal("39.99"))
                .stock(40)
                .imageUrl("https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&q=80")
                .category(books)
                .build());
    }
}
