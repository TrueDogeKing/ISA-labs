package com.example.utils;

import com.example.Entities.Genre;
import com.example.Entities.Movie;

import java.util.*;

public class Data {

    public static List<Genre> createSampleData() {
        // === Create genres with UUIDs ===
        Genre sciFi = Genre.builder()
                .id(UUID.randomUUID())
                .name("sci_fi")
                .description("Futuristic and science-based movies")
                .build();

        Genre drama = Genre.builder()
                .id(UUID.randomUUID())
                .name("drama")
                .description("Dramatic, emotional stories")
                .build();

        Genre action = Genre.builder()
                .id(UUID.randomUUID())
                .name("action")
                .description("Movies with action, fights, or car chases")
                .build();

        // === Create movies with UUIDs ===
        Movie m1 = Movie.builder()
                .id(UUID.randomUUID())
                .title("Inception")
                .year(2010)
                .rating(8.8)
                .build();

        Movie m2 = Movie.builder()
                .id(UUID.randomUUID())
                .title("Interstellar")
                .year(2014)
                .rating(8.9)
                .build();

        Movie m3 = Movie.builder()
                .id(UUID.randomUUID())
                .title("The Martian")
                .year(2015)
                .rating(9.0)
                .build();

        Movie m4 = Movie.builder()
                .id(UUID.randomUUID())
                .title("The Shawshank Redemption")
                .year(1994)
                .rating(9.3)
                .build();

        Movie m5 = Movie.builder()
                .id(UUID.randomUUID())
                .title("Forrest Gump")
                .year(1994)
                .rating(8.8)
                .build();

        Movie m6 = Movie.builder()
                .id(UUID.randomUUID())
                .title("Mad Max: Fury Road")
                .year(2015)
                .rating(6.4)
                .build();

        Movie m7 = Movie.builder()
                .id(UUID.randomUUID())
                .title("John Wick")
                .year(2014)
                .rating(7.4)
                .build();

        // === Assign movies to genres (bidirectional) ===
        sciFi.addMovie(m1);
        sciFi.addMovie(m2);
        sciFi.addMovie(m3);

        drama.addMovie(m4);
        drama.addMovie(m5);

        action.addMovie(m6);
        action.addMovie(m7);

        // === Return genres with movies ===
        return List.of(sciFi, drama, action);
    }
}
