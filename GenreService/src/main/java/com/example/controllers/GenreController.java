package com.example.controllers;

import com.example.dtos.GenreCreateUpdateDTO;
import com.example.dtos.GenreListItemDTO;
import com.example.dtos.GenreReadDTO;
import com.example.dtos.GenreSyncDTO;
import com.example.entities.Genre;
import com.example.integration.MovieEventPublisher;
import com.example.services.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private static final ResponseEntity<Void> NOT_FOUND = new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    private static final ResponseEntity<Void> CONFLICT = new ResponseEntity<Void>(HttpStatus.CONFLICT);

    private final GenreService genreService;
    private final MovieEventPublisher movieEventPublisher;

    public GenreController(GenreService genreService, MovieEventPublisher movieEventPublisher) {
        this.genreService = genreService;
        this.movieEventPublisher = movieEventPublisher;
    }

    @GetMapping
    public ResponseEntity<List<GenreListItemDTO>> getAllGenres() {
        List<Genre> genres = genreService.findAll();
        List<GenreListItemDTO> dtoList = genres.stream()
                .map(g -> new GenreListItemDTO(g.getId(), g.getName()))
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreReadDTO> getGenreById(@PathVariable UUID id) {
        return genreService.findById(id)
                .map(g -> new GenreReadDTO(g.getId(), g.getName(), g.getDescription()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> createGenre(@RequestBody GenreCreateUpdateDTO dto,
                                            UriComponentsBuilder uriBuilder) {
        Genre existing = genreService.findByName(dto.name());
        if (existing != null) {
            return CONFLICT;
        }

        Genre newGenre = Genre.builder()
                .id(UUID.randomUUID())
                .name(dto.name())
                .description(dto.description())
                .build();

        newGenre = genreService.save(newGenre);

        movieEventPublisher.publishGenreCreated(
            new GenreSyncDTO(newGenre.getId(), newGenre.getName(), newGenre.getDescription())
        );

        return ResponseEntity.created(
                uriBuilder.path("/genres/{id}")
                        .buildAndExpand(newGenre.getId())
                        .toUri()
        ).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateGenre(@PathVariable UUID id,
                                            @RequestBody GenreCreateUpdateDTO dto) {
        var existingGenre = genreService.findById(id);
        if (existingGenre.isEmpty()) {
            return NOT_FOUND;
        }

        Genre genre = existingGenre.get();
        genre.setName(dto.name());
        genre.setDescription(dto.description());
        genreService.save(genre);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable UUID id) {
        if (genreService.findById(id).isEmpty()) {
            return NOT_FOUND;
        }

        genreService.deleteById(id);
        movieEventPublisher.publishGenreDeleted(id);
        return ResponseEntity.noContent().build();
    }
}
