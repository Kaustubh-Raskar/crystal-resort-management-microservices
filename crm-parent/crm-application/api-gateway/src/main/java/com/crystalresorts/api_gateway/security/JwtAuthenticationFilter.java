package com.crystalresorts.api_gateway.security;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.crystalresorts.api_gateway.util.JwtUtil;

import reactor.core.publisher.Mono;
/**
 * @author Kaustubh Raskar
 * Created: July 2025
 */
// @Component
// public class JwtAuthenticationFilter implements WebFilter {

//     private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
//     private final JwtUtil jwtUtil;

//     public JwtAuthenticationFilter(JwtUtil jwtUtil) {
//         this.jwtUtil = jwtUtil;
//     }

//     @Override
//     public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

//         // bypass auth endpoints completely
//         String path = exchange.getRequest().getURI().getPath();
//         if (path.startsWith("/api/auth/")) {
//             return chain.filter(exchange);
//         }

//         String authHeader = exchange.getRequest()
//                 .getHeaders()
//                 .getFirst(HttpHeaders.AUTHORIZATION);

//         if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//             return chain.filter(exchange);
//         }

//         String token = authHeader.substring(7);

//         if (!jwtUtil.validateToken(token)) {
//             exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//             return exchange.getResponse().setComplete();
//         }

//         String username = jwtUtil.extractUsername(token);

//         List<SimpleGrantedAuthority> authorities =
//         jwtUtil.getRolesFromToken(token).stream()
//             // .map(SimpleGrantedAuthority::new)
//             .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
//             .toList();
    
//         log.debug("Authenticated user: {}, authorities: {}", username, authorities);

//         Authentication authentication =
//                 new UsernamePasswordAuthenticationToken(
//                         username, null, authorities
//                 );

//         return chain.filter(exchange)
//                 .contextWrite(
//                         ReactiveSecurityContextHolder.withAuthentication(authentication)
//                 );
//     }
// }
@Component
public class JwtAuthenticationFilter implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // 1️⃣ Allow auth endpoints without authentication
        if (path.startsWith("/api/auth/")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        // 2️⃣ If no Bearer token, pass through (let Spring Security rules decide)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);

        // 3️⃣ Validate token format and signature
        if (!jwtUtil.validateToken(token)) {
            log.warn("Invalid or expired JWT token");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 4️⃣ Extract user information from token
        String username = jwtUtil.extractUsername(token);
        Set<String> roles = jwtUtil.getRolesFromToken(token);

        log.debug("Gateway authenticated user={}, roles={}", username, roles);

        // 5️⃣ Create authorities with ROLE_ prefix for Spring Security
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();

        // 6️⃣ Create Authentication object and set in security context
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                username, null, authorities
        );

        // 7️⃣ Forward trusted headers to downstream services
        ServerHttpRequest mutatedRequest = exchange.getRequest()
                .mutate()
                .headers(headers -> {
                    headers.remove("X-User-Id");
                    headers.remove("X-Roles");
                    headers.remove("X-Auth-Source");

                    headers.add("X-User-Id", username);
                    headers.add("X-Roles", String.join(",", roles));
                    headers.add("X-Auth-Source", "api-gateway");
                })
                .build();

        // 8️⃣ Chain with authentication context so Spring Security can evaluate authorization rules
        return chain.filter(exchange.mutate().request(mutatedRequest).build())
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
    }
}


