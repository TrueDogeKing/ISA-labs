package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayServiceMain {

    public static void main(String[] args) {
        System.out.println("=== API Gateway ===");
        SpringApplication.run(GatewayServiceMain.class, args);
    }
}
