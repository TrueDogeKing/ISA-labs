package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieServiceMain {

    public static void main(String[] args) {
        System.out.println("=== Movie Service (Microservice) ===");
        SpringApplication.run(MovieServiceMain.class, args);
        System.out.println("=== Movie Service Running ===");
    }
}
