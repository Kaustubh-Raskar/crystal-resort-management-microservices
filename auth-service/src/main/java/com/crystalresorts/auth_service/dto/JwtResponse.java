package com.crystalresorts.auth_service.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {
    private String token;               // Access Token
    private String refreshToken;       // Refresh Token
    private String username;
    private String fullName;
    private Set<String> roles;
}