package com.example;

import java.util.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;


import com.example.Entities.*;
import com.example.dtos.*;
import com.example.utils.*;
import com.example.repositories.*;
import jakarta.persistence.*;

@SpringBootApplication
public class Main {
    private static final String FILE = "genres.bin";

    public static void main(String[] args) throws Exception {
        System.out.println("=== Movies Lab ===");

        SpringApplication.run(Main.class, args);


        System.out.println("\n=== Done ===");
    }

    @Bean
    CommandLineRunner initDatabase(GenreRepository genreRepo, MovieRepository movieRepo) {
        return args -> {

            System.out.println("=== Create Data ===");
            List<Genre> genres = Data.createSampleData();

            System.out.println("=== save all genreRepo ===");
            genreRepo.saveAll(genres);

            System.out.println("=== All Movies ===");
            movieRepo.findAll().forEach(System.out::println);
        };
    }



}
