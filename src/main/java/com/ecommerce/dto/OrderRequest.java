package com.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderRequest {

    @NotBlank @Size(max = 300)
    private String shippingAddress;
}
