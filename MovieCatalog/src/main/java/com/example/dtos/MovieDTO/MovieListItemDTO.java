package com.example.dtos.MovieDTO;

import lombok.*;

import java.util.UUID;

// List Item DTO
public record MovieListItemDTO(UUID id, String title,int releaseYear,double rating){}
