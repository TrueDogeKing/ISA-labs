package com.example.integration;

import com.example.dtos.GenreSyncDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class MovieEventPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieEventPublisher.class);

    private final RestTemplate restTemplate;
    private final String movieServiceBaseUrl;

    public MovieEventPublisher(RestTemplate restTemplate,
                               @Value("${movieservice.base-url}") String movieServiceBaseUrl) {
        this.restTemplate = restTemplate;
        this.movieServiceBaseUrl = movieServiceBaseUrl;
    }

    public void publishGenreCreated(GenreSyncDTO dto) {
        try {
            restTemplate.postForEntity(movieServiceBaseUrl + "/internal/categories", dto, Void.class);
            LOGGER.info("Published genre creation event for {}", dto.id());
        } catch (RestClientException ex) {
            LOGGER.warn("Failed to notify movie service about genre creation (id={}): {}", dto.id(), ex.getMessage());
        }
    }

    public void publishGenreDeleted(UUID genreId) {
        try {
            restTemplate.delete(movieServiceBaseUrl + "/internal/categories/{id}", genreId);
            LOGGER.info("Published genre deletion event for {}", genreId);
        } catch (RestClientException ex) {
            LOGGER.warn("Failed to notify movie service about genre deletion (id={}): {}", genreId, ex.getMessage());
        }
    }
}
