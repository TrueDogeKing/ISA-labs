package com.example.utils;

import com.example.Entities.Genre;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class ThreadManager {
    private static void parallelThreadGenres(List<Genre> genres) {
        ForkJoinPool customThreadPool = new ForkJoinPool(4);

        try {
            customThreadPool.submit(() ->
                    genres.parallelStream().forEach(genre -> {
                        System.out.println("\n--- " + Thread.currentThread().getName() + " Genre: " + genre.getName() + " ---");

                        genre.getMovies().forEach(movie -> {
                            System.out.println("--- " + Thread.currentThread().getName() + " Movie: " + movie + " ---");
                        });

                        System.out.println("--- " + Thread.currentThread().getName() + " End Genre: " + genre.getName() + " ---\n");
                    })
            ).get();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            customThreadPool.shutdown();
            try {
                if (!customThreadPool.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                    customThreadPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                customThreadPool.shutdownNow();
            }
        }
    }

    private static void streamThreadGenres(List<Genre> genres) {
        ForkJoinPool customThreadPool = new ForkJoinPool(4);

        try {
            customThreadPool.submit(() ->
                    genres.stream().forEach(genre -> {
                        System.out.println("\n--- " + Thread.currentThread().getName() + " Genre: " + genre.getName() + " ---");

                        genre.getMovies().forEach(movie -> {
                            System.out.println("--- " + Thread.currentThread().getName() + " Movie: " + movie + " ---");
                        });

                        System.out.println("--- " + Thread.currentThread().getName() + " End Genre: " + genre.getName() + " ---\n");
                    })
            ).get();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            customThreadPool.shutdown();
            try {
                if (!customThreadPool.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                    customThreadPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                customThreadPool.shutdownNow();
            }
        }
    }
}
