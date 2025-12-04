package com.example.config;

import com.example.dtos.GenreSyncDTO;
import com.example.entities.Genre;
import com.example.integration.MovieEventPublisher;
import com.example.services.GenreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GenreDataInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenreDataInitializer.class);

    private final GenreService genreService;
    private final MovieEventPublisher movieEventPublisher;

    public GenreDataInitializer(GenreService genreService, MovieEventPublisher movieEventPublisher) {
        this.genreService = genreService;
        this.movieEventPublisher = movieEventPublisher;
    }

    public void generateSampleData() {
        List<Genre> seeds = List.of(
            buildGenre("sci_fi", "Futuristic and science-based movies"),
            buildGenre("drama", "Dramatic, emotional stories"),
            buildGenre("action", "Movies with action, fights, or car chases")
        );

        seeds.forEach(seed -> {
            if (genreService.findByName(seed.getName()) == null) {
                Genre saved = genreService.save(seed);
                movieEventPublisher.publishGenreCreated(
                        new GenreSyncDTO(saved.getId(), saved.getName(), saved.getDescription())
                );
                LOGGER.info("Seeded genre '{}'.", saved.getName());
            }
        });
    }

    private static Genre buildGenre(String name, String description) {
        return Genre.builder()
                .id(UUID.randomUUID())
                .name(name)
                .description(description)
                .build();
    }
}
