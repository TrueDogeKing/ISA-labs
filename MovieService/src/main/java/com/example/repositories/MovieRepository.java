package com.example.repositories;

import com.example.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {
    List<Movie> findByGenre_Id(UUID genreId);
    void deleteByGenre_Id(UUID genreId);
}
