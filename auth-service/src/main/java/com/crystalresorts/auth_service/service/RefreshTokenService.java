package com.crystalresorts.auth_service.service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.crystalresorts.auth_service.security.JwtUtil;

import lombok.RequiredArgsConstructor;
/**
 * @author Kaustubh Raskar
 * Created: July 2025
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    public void storeRefreshToken(String username, String refreshToken) {
        // Store refresh token with an expiry same as its TTL
        redisTemplate.opsForValue().set("refresh:" + username, refreshToken, Duration.ofDays(7));
    }

    public boolean validateRefreshToken(String username, String refreshToken) {
        String storedToken = redisTemplate.opsForValue().get("refresh:" + username);
        return refreshToken.equals(storedToken) && jwtUtil.validateToken(refreshToken);
    }

    public void deleteRefreshToken(String username) {
        redisTemplate.delete("refresh:" + username);
    }

    public void blacklistAccessToken(String token, long expiryMillis) {
        redisTemplate.opsForValue().set("blacklist:" + token, "true", Duration.ofMillis(expiryMillis));
    }
    
    public boolean isAccessTokenBlacklisted(String token) {
        return Boolean.TRUE.toString().equals(
            redisTemplate.opsForValue().get("blacklist:" + token)
        );
    }
    
}

