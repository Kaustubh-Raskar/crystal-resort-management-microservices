package com.crystalresorts.auth_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crystalresorts.auth_service.dto.JwtRequest;
import com.crystalresorts.auth_service.dto.JwtResponse;
import com.crystalresorts.auth_service.dto.TokenValidationResponse;
import com.crystalresorts.auth_service.dto.UserDto;
import com.crystalresorts.auth_service.security.JwtUtil;
import com.crystalresorts.auth_service.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto){
        return new ResponseEntity<>(authService.register(userDto), HttpStatus.CREATED);
    }
    

    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(
                new TokenValidationResponse(false, null, null, "Missing or malformed Authorization header")
            );
        }

        String token = authHeader.substring(7); // remove "Bearer "

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.ok(
                new TokenValidationResponse(false, null, null, "Invalid or expired token")
            );
        }

        String username = jwtUtil.extractUsername(token);
        Set<String> roles = jwtUtil.getRolesFromToken(token);

        return ResponseEntity.ok(
            new TokenValidationResponse(true, username, new ArrayList<>(roles), null)
        );
    }
}
