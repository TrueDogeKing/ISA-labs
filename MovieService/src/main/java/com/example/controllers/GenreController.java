package com.example.controllers;

import com.example.dtos.GenreCreateUpdateDTO;
import com.example.dtos.GenreListItemDTO;
import com.example.dtos.GenreReadDTO;
import com.example.dtos.GenreWithMoviesDTO;
import com.example.dtos.MovieListItemDTO;
import com.example.entities.Genre;
import com.example.services.GenreService;
import com.example.services.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Simplified Genre Controller in Movie Service.
 * Provides basic genre operations to maintain relationships with movies.
 */
@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;
    private final MovieService movieService;

    public GenreController(GenreService genreService, MovieService movieService) {
        this.genreService = genreService;
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<GenreListItemDTO>> getAllGenres() {
        List<Genre> genres = genreService.findAll();
        List<GenreListItemDTO> dtoList = genres.stream()
                .map(g -> new GenreListItemDTO(g.getId(), g.getName()))
                .toList();

        return ResponseEntity.ok(dtoList);
    }

        @GetMapping("/with-movies")
        public ResponseEntity<List<GenreWithMoviesDTO>> getGenresWithMovies() {
        List<Genre> genres = genreService.findAll();
        Map<UUID, List<MovieListItemDTO>> moviesByGenre = movieService.findAll().stream()
            .filter(movie -> movie.getGenre() != null)
            .collect(Collectors.groupingBy(
                movie -> movie.getGenre().getId(),
                Collectors.mapping(movie -> new MovieListItemDTO(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getReleaseYear(),
                    movie.getRating()
                ), Collectors.toList())
            ));

        List<GenreWithMoviesDTO> result = genres.stream()
            .map(genre -> new GenreWithMoviesDTO(
                genre.getId(),
                genre.getName(),
                genre.getDescription(),
                moviesByGenre.getOrDefault(genre.getId(), List.of())
            ))
            .toList();

        return ResponseEntity.ok(result);
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
        var existing = genreService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Genre genre = existing.get();
        genre.setName(dto.name());
        genre.setDescription(dto.description());
        genreService.save(genre);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable UUID id) {
        if (genreService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        genreService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
