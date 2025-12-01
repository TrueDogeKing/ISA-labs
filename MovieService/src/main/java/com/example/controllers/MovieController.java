package com.example.controllers;

import com.example.dtos.MovieCreateUpdateDTO;
import com.example.dtos.MovieListItemDTO;
import com.example.dtos.MovieReadDTO;
import com.example.entities.Genre;
import com.example.entities.Movie;
import com.example.services.GenreService;
import com.example.services.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/genres/{genreId}/movies")
public class MovieController {

    private final MovieService movieService;
    private final GenreService genreService;

    public MovieController(MovieService movieService, GenreService genreService) {
        this.movieService = movieService;
        this.genreService = genreService;
    }

    @GetMapping
    public ResponseEntity<List<MovieListItemDTO>> getMovies(@PathVariable UUID genreId) {
        return genreService.findById(genreId)
                .map(genre -> {
                    List<MovieListItemDTO> movies = movieService.findByGenreId(genreId)
                            .stream()
                            .map(m -> new MovieListItemDTO(
                                    m.getId(),
                                    m.getTitle(),
                                    m.getReleaseYear(),
                                    m.getRating()
                            ))
                            .toList();
                    return ResponseEntity.ok(movies);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> addMovie(@PathVariable UUID genreId,
                                         @RequestBody MovieCreateUpdateDTO dto,
                                         UriComponentsBuilder uriBuilder) {

        return genreService.findById(genreId)
                .map(genre -> {
                    Movie movie = Movie.builder()
                            .id(UUID.randomUUID())
                            .title(dto.title())
                            .releaseYear(dto.releaseYear())
                            .rating(dto.rating())
                            .genre(genre)
                            .build();

                    movieService.save(movie);

                    URI location = uriBuilder.path("/genres/{genreId}/movies/{movieId}")
                            .buildAndExpand(genreId, movie.getId())
                            .toUri();

                    return ResponseEntity.created(location).<Void>build();
                })
                .orElse(ResponseEntity.notFound().<Void>build());
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieReadDTO> getMovie(@PathVariable UUID genreId,
                                                 @PathVariable UUID movieId) {

        return movieService.findById(movieId)
                .filter(movie -> movie.getGenre() != null &&
                        movie.getGenre().getId().equals(genreId))
                .map(movie -> new MovieReadDTO(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getReleaseYear(),
                        movie.getRating(),
                        movie.getGenre().getName()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{movieId}")
    public ResponseEntity<Void> updateMovie(@PathVariable UUID genreId,
                                            @PathVariable UUID movieId,
                                            @RequestBody MovieCreateUpdateDTO dto) {

        return movieService.findById(movieId)
                .filter(movie -> movie.getGenre() != null &&
                        movie.getGenre().getId().equals(genreId))
                .map(movie -> {
                    movie.setTitle(dto.title());
                    movie.setReleaseYear(dto.releaseYear());
                    movie.setRating(dto.rating());
                    movieService.save(movie);
                    return ResponseEntity.<Void>ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable UUID genreId,
                                            @PathVariable UUID movieId) {

        return movieService.findById(movieId)
                .filter(movie -> movie.getGenre() != null &&
                        movie.getGenre().getId().equals(genreId))
                .map(movie -> {
                    movieService.deleteById(movieId);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
