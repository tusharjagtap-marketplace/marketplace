package com.iexpo.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayServiceApplication {

    @Value("${auth.service.uri:http://auth-service:9000}")
    private String authServiceUri;

    @Value("${marketplace.service.uri:http://marketplace-service:8081}")
    private String marketplaceServiceUri;

    @Value("${payment.service.uri:http://payment-service:8082}")
    private String paymentServiceUri;

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // 1. Auth Service API
            .route("auth-service", r -> r.path("/api/auth/**")
                .uri(authServiceUri))
            // 2. Auth Service Actuator
            .route("auth-actuator", r -> r.path("/auth/actuator/**")
                .filters(f -> f.stripPrefix(1))
                .uri(authServiceUri))
            
            // 3. Marketplace Service API
            .route("marketplace-service", r -> r.path("/api/test/**")
                .uri(marketplaceServiceUri))
            // 4. Marketplace Service Actuator
            .route("marketplace-actuator", r -> r.path("/marketplace/actuator/**")
                .filters(f -> f.stripPrefix(1))
                .uri(marketplaceServiceUri))
            
            // 5. Payment Service API
            .route("payment-service", r -> r.path("/api/payment/**")
                .uri(paymentServiceUri))
            // 6. Payment Service Actuator
            .route("payment-actuator", r -> r.path("/payment/actuator/**")
                .filters(f -> f.stripPrefix(1))
                .uri(paymentServiceUri))
            .build();
    }
}
