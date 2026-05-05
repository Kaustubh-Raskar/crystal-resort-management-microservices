package com.crystalresorts.roomservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String userId = request.getHeader("X-User-Id");
            String rolesHeader = request.getHeader("X-Roles");
            String authSource = request.getHeader("X-Auth-Source");

            log.debug("Processing headers - X-User-Id: {}, X-Roles: {}, X-Auth-Source: {}", 
                    userId, rolesHeader, authSource);

            if (userId != null && rolesHeader != null && "api-gateway".equals(authSource)) {
                
                // Parse roles from comma-separated header
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                if (!rolesHeader.isEmpty()) {
                    String[] roles = rolesHeader.split(",");
                    for (String role : roles) {
                        String trimmedRole = role.trim();
                        if (!trimmedRole.startsWith("ROLE_")) {
                            trimmedRole = "ROLE_" + trimmedRole;
                        }
                        authorities.add(new SimpleGrantedAuthority(trimmedRole));
                    }
                }

                log.debug("Creating authentication for user: {} with authorities: {}", 
                        userId, authorities);

                // Create authentication object
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);
                
                // Set in security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                log.debug("Authentication set in security context for user: {}", userId);
            }
        } catch (Exception e) {
            log.error("Error processing authentication headers", e);
        }

        filterChain.doFilter(request, response);
    }
}
