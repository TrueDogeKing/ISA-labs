package com.example.dtos;

import java.util.UUID;

public record MovieListItemDTO(UUID id, String title, int releaseYear, double rating) {}
