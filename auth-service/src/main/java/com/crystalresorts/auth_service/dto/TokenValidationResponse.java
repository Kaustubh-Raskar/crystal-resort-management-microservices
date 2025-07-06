package com.crystalresorts.auth_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author Kaustubh Raskar
 * Created: July 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidationResponse {
    private boolean valid;
    private String username;
    private List<String> roles;
    private String error;

}
