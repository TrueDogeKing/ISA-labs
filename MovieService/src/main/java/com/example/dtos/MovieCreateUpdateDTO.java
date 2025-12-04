package com.example.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public record MovieCreateUpdateDTO(
	String title,
	int releaseYear,
	@DecimalMin(value = "0.0")
	@DecimalMax(value = "10.0")
	double rating
) {}
