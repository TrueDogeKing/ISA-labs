package com.example.dtos.GenreDTO;


import com.example.dtos.MovieDTO.MovieListItemDTO;
import java.util.List;
import java.util.UUID;

// Read DTO including movies
public record GenreWithMoviesDTO(
        UUID id,
        String name,
        String description,
        List<MovieListItemDTO> movies
) { }

