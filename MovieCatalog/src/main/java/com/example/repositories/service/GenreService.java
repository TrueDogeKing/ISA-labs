package com.example.repositories.service;

import com.example.Entities.Genre;
import com.example.repositories.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    // Delegate methods

    @Transactional(readOnly = true)
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Genre> findById(UUID id) {
        return genreRepository.findById(id);
    }

    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    public List<Genre> saveAll(List<Genre> genres) {
        return genreRepository.saveAll(genres);
    }

    public void deleteById(UUID id) {
        genreRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    public Genre findByName(String name) {
        return genreRepository.findByName(name);
    }
}
