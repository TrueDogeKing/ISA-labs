package com.example.controllers;

import com.example.config.MovieDataInitializer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final MovieDataInitializer movieDataInitializer;

    public AdminController(MovieDataInitializer movieDataInitializer) {
        this.movieDataInitializer = movieDataInitializer;
    }

    @PostMapping("/sample-data")
    public ResponseEntity<Void> generateSampleMovies() {
        movieDataInitializer.generateSampleData();
        return ResponseEntity.accepted().build();
    }
}
