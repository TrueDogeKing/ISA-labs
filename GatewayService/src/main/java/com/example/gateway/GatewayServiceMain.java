package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import java.util.List;

@SpringBootApplication
public class GatewayServiceMain {

    public static void main(String[] args) {
        System.out.println("=== API Gateway ===");
        SpringApplication.run(GatewayServiceMain.class, args);
    }
}
