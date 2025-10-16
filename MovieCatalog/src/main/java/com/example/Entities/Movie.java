package com.example.Entities;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a movie with a title, release year, rating, and associated genre.
 */
@Getter
@Setter
@Builder
public class Movie implements Comparable<Movie>, Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private int year;
    private double rating;
    private Genre genre;

    @Override
    public int compareTo(Movie other) {
        // 1. Higher rating first
        int ratingCompare = Double.compare(other.rating, this.rating);
        if (ratingCompare != 0) return ratingCompare;

        // 2. Newer year first
        int yearCompare = Integer.compare(other.year, this.year);
        if (yearCompare != 0) return yearCompare;

        // 3. Genre alphabetically (case-insensitive)
        int genreCompare = this.genre.getName().compareToIgnoreCase(other.genre.getName());
        if (genreCompare != 0) return genreCompare;

        // 4. Finally, by title alphabetically
        return this.title.compareToIgnoreCase(other.title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie movie)) return false;
        return Objects.equals(title, movie.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public String toString() {
        return String.format(
                "Movie{title='%s', year=%d, rating=%.1f, genre='%s'}",
                title, year, rating, genre != null ? genre.getName() : "None"
        );
    }

    public void setGenre(Genre genre) {
        if (genre != null) {
            this.genre = genre;
        } else {
            this.genre = null;
        }
    }
}
