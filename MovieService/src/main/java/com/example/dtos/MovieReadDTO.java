package com.example.dtos;

import java.util.UUID;

public record MovieReadDTO(UUID id, String title, int releaseYear, double rating, String genreName) {}
