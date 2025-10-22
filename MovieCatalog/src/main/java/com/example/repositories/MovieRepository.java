package com.example.repositories;

import com.example.Entities.Movie;
import com.example.Entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {

    // Find movies by Genre entity
    List<Movie> findByGenre(Genre genre);

    // Find movies by Genre ID
    List<Movie> findByGenre_Id(UUID id);

    Optional<Movie> findByTitleIgnoreCase(String title);
}
