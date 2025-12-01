package com.example.dtos;

import java.util.UUID;

/**
 * Event payload used to keep genre data synchronized with the Genre Service.
 */
public record GenreSyncDTO(UUID id, String name, String description) {}
