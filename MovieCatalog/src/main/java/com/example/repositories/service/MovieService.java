package com.example.repositories.service;

import com.example.Entities.Genre;
import com.example.Entities.Movie;
import com.example.repositories.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional(readOnly = true)
    public List<Movie> findAll() {
        return movieRepository.findAll(); // use transactional context
    }

    @Transactional(readOnly = true)
    public Optional<Movie> findById(UUID id) {
        return movieRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Movie> findByGenreId(UUID genreId) {
        return movieRepository.findByGenre_Id(genreId);
    }


    @Transactional
    public void save(Movie movie) {
        movieRepository.save(movie);
    }

    @Transactional
    public void deleteById(UUID id) {
        movieRepository.deleteById(id);
    }
}


