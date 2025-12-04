package com.example.controllers;

import com.example.dtos.GenreSyncDTO;
import com.example.entities.Genre;
import com.example.services.GenreService;
import com.example.services.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Internal endpoint that accepts REST events from the Genre Service.
 */
@RestController
@RequestMapping("/internal/categories")
public class CategoryEventController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryEventController.class);

    private final GenreService genreService;
    private final MovieService movieService;

    public CategoryEventController(GenreService genreService, MovieService movieService) {
        this.genreService = genreService;
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<Void> upsertCategory(@RequestBody GenreSyncDTO dto) {
        LOGGER.info("Received genre upsert event for {}", dto.id());
        Genre genre = genreService.findById(dto.id())
                .orElseGet(() -> Genre.builder().id(dto.id()).build());

        genre.setName(dto.name());
        genre.setDescription(dto.description());
        genreService.save(genre);

        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        LOGGER.info("Received genre delete event for {}", id);
        movieService.deleteByGenreId(id);
        genreService.deleteById(id);
        return ResponseEntity.accepted().build();
    }
}
