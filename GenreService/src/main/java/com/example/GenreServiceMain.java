package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GenreServiceMain {

    public static void main(String[] args) {
        System.out.println("=== Genre Service (Microservice) ===");
        SpringApplication.run(GenreServiceMain.class, args);
        System.out.println("=== Genre Service Running ===");
    }
}
