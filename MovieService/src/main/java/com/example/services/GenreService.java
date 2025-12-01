package com.example.services;

import com.example.entities.Genre;
import com.example.repositories.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Simplified Genre Service in Movie Service.
 * Provides basic genre management to maintain relationships with movies.
 */
@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Transactional(readOnly = true)
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Genre> findById(UUID id) {
        return genreRepository.findById(id);
    }

    @Transactional
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    @Transactional
    public List<Genre> saveAll(List<Genre> genres) {
        return genreRepository.saveAll(genres);
    }

    @Transactional
    public void deleteById(UUID id) {
        genreRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Genre findByName(String name) {
        return genreRepository.findByName(name);
    }
}
