package com.ecommerce.dto;

import com.ecommerce.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String email;
    private String fullName;
    private Role role;
}
