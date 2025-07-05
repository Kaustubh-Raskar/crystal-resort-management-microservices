package com.crystalresorts.auth_service.security;

import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationMs}")
    private long expirationMs;

    /**
     * Generate JWT token with username and roles.
     */
    public String generateToken(String username, Set<String> roleNames) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roleNames)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extract username from the token.
     */
    public String extractUsername(String token) {
        if (isTokenInvalid(token)) return null;
        return extractClaims(token).getSubject();
    }

    /**
     * Validate the token for structure, signature, and expiry.
     */
    public boolean validateToken(String token) {
        if (isTokenInvalid(token)) return false;

        try {
            extractClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token expired: {}", e.getMessage());
        } catch (JwtException e) {
            logger.warn("Invalid JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Extract roles from JWT claims.
     */
    public Set<String> getRolesFromToken(String token) {
        if (isTokenInvalid(token)) return Collections.emptySet();

        Claims claims = extractClaims(token);
        Object roles = claims.get("roles");

        if (roles instanceof Collection<?>) {
            return ((Collection<?>) roles).stream()
                    .map(Object::toString)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    /**
     * Extract all claims from the token.
     */
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Decode and prepare signing key.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Utility to check if token is null or blank.
     */
    private boolean isTokenInvalid(String token) {
        if (token == null || token.trim().isEmpty()) {
            logger.warn("JWT token is null or empty");
            return true;
        }
        return false;
    }

    // public List<String> extractRoles(String token) {
    //     Claims claims = extractClaims(token);
    //     Object rolesObj = claims.get("roles");
    
    //     if (rolesObj instanceof Collection<?>) {
    //         return ((Collection<?>) rolesObj)
    //                 .stream()
    //                 .map(Object::toString)
    //                 .collect(Collectors.toList());
    //     }
    
    //     return Collections.emptyList();
    // }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                   .setSubject(username)
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis() + Duration.ofDays(7).toMillis()))
                   .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                   .compact();
    }

    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }    
    
}
