package com.example.repositories;

import com.example.Entities.Movie;
import com.example.Entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {

    // Custom query method: find movies by genre (category)
    List<Movie> findByGenre(Genre genre);

    // Optional: query by genre name (via nested property)
    List<Movie> findByGenre_Name(String genreName);
}
