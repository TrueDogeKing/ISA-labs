package com.example.dtos;

import java.util.List;
import java.util.UUID;

public record GenreWithMoviesDTO(
        UUID id,
        String name,
        String description,
        List<MovieListItemDTO> movies
) {}
