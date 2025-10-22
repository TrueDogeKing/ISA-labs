package com.example.repositories;

import com.example.Entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GenreRepository extends JpaRepository<Genre, UUID> {
    // You can later add custom finders if needed, e.g.:
    Genre findByName(String name);
}
