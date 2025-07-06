package com.crystalresorts.api_gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.crystalresorts.api_gateway.security.JwtAuthenticationFilter;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri("lb://AUTH-SERVICE"))

                .route("any-secured-service", r -> r.path("/api/booking/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("lb://BOOKING-SERVICE"))

                .build();
    }
}
