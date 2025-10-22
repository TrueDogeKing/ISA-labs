package com.example.utils;


import com.example.Entities.Genre;
import com.example.repositories.service.*;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.repositories.*;

import java.util.List;

@Component
public class DataInitializer {

    private final GenreRepository genreRepo;

    public DataInitializer(GenreRepository genreRepo) {
        this.genreRepo = genreRepo;
    }

    @PostConstruct
    public void init() {
        List<Genre> genres = Data.createSampleData();
        genreRepo.saveAll(genres);
        System.out.println("=== Sample data loaded ===");
    }
}


