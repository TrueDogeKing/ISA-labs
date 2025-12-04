package com.example.config;

import com.example.entities.Genre;
import com.example.entities.Movie;
import com.example.services.GenreService;
import com.example.services.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MovieDataInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieDataInitializer.class);

    private final GenreService genreService;
    private final MovieService movieService;

    public MovieDataInitializer(GenreService genreService, MovieService movieService) {
        this.genreService = genreService;
        this.movieService = movieService;
    }

    public void generateSampleData() {
        if (!movieService.findAll().isEmpty()) {
            LOGGER.info("Skipping movie sample data generation because repository is not empty");
            return;
        }

        Genre sciFi = ensureGenreExists("sci_fi", "Futuristic and science-based movies");
        Genre drama = ensureGenreExists("drama", "Dramatic, emotional stories");
        Genre action = ensureGenreExists("action", "Movies with action, fights, or car chases");

        LOGGER.info("Ensuring genres exist: sci_fi, drama, action");

        List<Movie> movies = List.of(
            buildMovie("Inception", 2010, 8.8, sciFi),
            buildMovie("Interstellar", 2014, 8.9, sciFi),
            buildMovie("The Martian", 2015, 9.0, sciFi),
            buildMovie("The Shawshank Redemption", 1994, 9.3, drama),
            buildMovie("Forrest Gump", 1994, 8.8, drama),
            buildMovie("Mad Max: Fury Road", 2015, 6.4, action),
            buildMovie("John Wick", 2014, 7.4, action)
        );

        movies.forEach(movie -> {
            movieService.save(movie);
            LOGGER.info("Seeded movie '{}'", movie.getTitle());
        });
    }

    private Genre ensureGenreExists(String name, String description) {
        Genre existing = genreService.findByName(name);
        if (existing != null) {
            LOGGER.info("Genre '{}' already exists with id {}", name, existing.getId());
            return existing;
        }
        Genre genre = Genre.builder()
                .id(UUID.randomUUID())
                .name(name)
                .description(description)
                .build();
        LOGGER.info("Created missing genre '{}' with id {}", name, genre.getId());
        return genreService.save(genre);
    }

    private static Movie buildMovie(String title, int year, double rating, Genre genre) {
        return Movie.builder()
                .id(UUID.randomUUID())
                .title(title)
                .releaseYear(year)
                .rating(rating)
                .genre(genre)
                .build();
    }
}
