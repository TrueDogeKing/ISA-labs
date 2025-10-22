package com.example;

import java.util.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    private static final String FILE = "genres.bin";

    public static void main(String[] args) throws Exception {
        System.out.println("=== Movies Lab ===");

        SpringApplication.run(Main.class, args);


        System.out.println("\n=== Done ===");
    }



}
