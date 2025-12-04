package com.example.controllers;

import com.example.config.GenreDataInitializer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final GenreDataInitializer genreDataInitializer;

    public AdminController(GenreDataInitializer genreDataInitializer) {
        this.genreDataInitializer = genreDataInitializer;
    }

    @PostMapping("/sample-data")
    public ResponseEntity<Void> generateSampleGenres() {
        genreDataInitializer.generateSampleData();
        return ResponseEntity.accepted().build();
    }
}
