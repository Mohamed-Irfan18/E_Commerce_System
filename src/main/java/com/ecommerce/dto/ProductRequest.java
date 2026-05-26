package com.ecommerce.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank @Size(max = 200)
    private String name;

    @Size(max = 1000)
    private String description;

    @NotNull @DecimalMin("0.01")
    private BigDecimal price;

    @NotNull @Min(0)
    private Integer stock;

    @Size(max = 500)
    private String imageUrl;

    @NotNull
    private Long categoryId;
}
