package com.example.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.*;
import java.util.*;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a movie genre containing a list of associated movies.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "genres")
public class Genre implements Comparable<Genre>, Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;  // client-generated UUID

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Movie> movies = new ArrayList<>();

    /** Adds a movie to the genre and maintains bidirectional relationship. */
    public void addMovie(Movie movie) {
        if (movie != null && !movies.contains(movie)) {
            movies.add(movie);
            movie.setGenre(this);
        }
    }

    @Override
    public int compareTo(Genre other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genre genre)) return false;
        return Objects.equals(name, genre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }



    public List<Movie> getMovies() {
        return movies;
    }


    @Override
    public String toString() {
        return String.format(
                "Genre{name='%s', description='%s', movies=%d}",
                name, description, movies.size()
        );
    }
}
