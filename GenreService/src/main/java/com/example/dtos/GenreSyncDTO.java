package com.example.dtos;

import java.util.UUID;

/**
 * Event payload shared with the Movie Service when categories change.
 */
public record GenreSyncDTO(UUID id, String name, String description) {}
