package com.crystalresorts.resortservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Reads X-User-Id and X-Roles headers (forwarded by API Gateway)
 * and converts them into a Spring Security Authentication object.
 * This enables role-based security checks using @PreAuthorize and hasRole().
 */
@Component
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String userId = request.getHeader("X-User-Id");
        String rolesHeader = request.getHeader("X-Roles");
        String authSource = request.getHeader("X-Auth-Source");

        // Only process if headers are present and auth source is the gateway
        if (userId != null && rolesHeader != null && "api-gateway".equals(authSource)) {
            try {
                // Parse roles from CSV format (e.g., "ADMIN,USER")
                List<GrantedAuthority> authorities = Arrays.stream(rolesHeader.split(","))
                    .map(String::trim)
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());

                // Create and set authentication in security context
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                logger.warn("Failed to process header-based authentication", e);
            }
        }

        filterChain.doFilter(request, response);
    }
}
