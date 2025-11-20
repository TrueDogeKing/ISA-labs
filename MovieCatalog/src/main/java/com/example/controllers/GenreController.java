package com.example.controllers;

import com.example.Entities.Genre;
import com.example.dtos.GenreDTO.*;
import com.example.dtos.MovieDTO.MovieListItemDTO;
import com.example.repositories.service.GenreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
                .movies(new ArrayList<>())
                .build();

        newGenre = genreService.save(newGenre);

        return ResponseEntity.created(
                uriBuilder.path("/genres/{id}")
                        .buildAndExpand(newGenre.getId())
                        .toUri()
        ).build();
    }

    // GET all genres with movies
    @GetMapping("/with-movies")
    public ResponseEntity<List<GenreWithMoviesDTO>> getGenresWithMovies() {
        List<GenreWithMoviesDTO> list = genreService.findAll().stream()
                .map(g -> new GenreWithMoviesDTO(
                        g.getId(),
                        g.getName(),
                        g.getDescription(),
                        g.getMovies().stream()
                                .map(m -> new MovieListItemDTO(
                                        m.getId(),
                                        m.getTitle(),
                                        m.getReleaseYear(),
                                        m.getRating()
                                ))
                                .toList()
                ))
                .toList();

        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable UUID id) {
        genreService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
