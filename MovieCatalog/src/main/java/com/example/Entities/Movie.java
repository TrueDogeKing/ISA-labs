package com.example.Entities;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.*;


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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies")
public class Movie implements Comparable<Movie>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "release_year")
    private int releaseYear;

    @Column(name = "rating")
    private double rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @Override
    public int compareTo(Movie other) {
        int ratingCompare = Double.compare(other.rating, this.rating);
        if (ratingCompare != 0) return ratingCompare;

        int yearCompare = Integer.compare(other.releaseYear, this.releaseYear);
        if (yearCompare != 0) return yearCompare;

        // Compare genre safely: check null
        String thisGenreName = genre != null ? genre.getName() : "";
        String otherGenreName = other.genre != null ? other.genre.getName() : "";
        int genreCompare = thisGenreName.compareToIgnoreCase(otherGenreName);
        if (genreCompare != 0) return genreCompare;

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
                "Movie{title='%s', year=%d, rating=%.1f}",
                title, releaseYear, rating
        );
    }

}
