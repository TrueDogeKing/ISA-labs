package com.example.controllers;

import com.example.dtos.GenreCreateUpdateDTO;
import com.example.dtos.GenreListItemDTO;
import com.example.dtos.GenreReadDTO;
import com.example.entities.Genre;
import com.example.services.GenreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

/**
 * Simplified Genre Controller in Movie Service.
 * Provides basic genre operations to maintain relationships with movies.
 */
@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
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
        Genre newGenre = Genre.builder()
                .id(UUID.randomUUID())
                .name(dto.name())
                .description(dto.description())
                .build();

        newGenre = genreService.save(newGenre);

        return ResponseEntity.created(
                uriBuilder.path("/genres/{id}")
                        .buildAndExpand(newGenre.getId())
                        .toUri()
        ).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateGenre(@PathVariable UUID id,
                                            @RequestBody GenreCreateUpdateDTO dto) {
        return genreService.findById(id)
                .map(genre -> {
                    genre.setName(dto.name());
                    genre.setDescription(dto.description());
                    genreService.save(genre);
                    return ResponseEntity.<Void>ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable UUID id) {
        genreService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
