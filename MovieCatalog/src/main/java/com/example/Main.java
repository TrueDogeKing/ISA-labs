package com.example;

import java.io.*;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import com.example.Entities.*;
import com.example.dtos.*;
import com.example.utils.*;

public class Main {
    private static final String FILE = "genres.bin";

    public static void main(String[] args) throws Exception {
        System.out.println("=== Movies Lab ===");

        // Task 2: create data
        List<Genre> genres =  Data.createSampleData();
        //List<Genre> genres = readGenresFromFile(FILE);

        System.out.println("\n--- Genres and movies ---");
        genres.forEach(g -> {
            System.out.println(g);
            g.getMovies().forEach(m -> System.out.println("  " + m));
        });

        // Task 3: collect all movies into a Set
        List<Movie> allMovies = genres.stream()
                .flatMap(genre -> genre.getMovies().stream())
                .distinct()
                .sorted() // natural order from Movie.compareTo()
                .toList();

        //allMovies.forEach(System.out::println);

        System.out.println("\n--- All movies (Set) ---");
        allMovies.forEach(System.out::println);

        // Task 4: filter by rating >= 8, sort by year
        System.out.println("\n--- Filter rating >= 8, sort by year ---");
        allMovies.stream()
                .filter(m -> m.getRating() >= 8)
                .sorted(Comparator.comparing(Movie::getYear))
                .forEach(System.out::println);

        // Task 5: transform to DTOs and sort naturally
        System.out.println("\n--- Movie DTOs sorted ---");
        List<MovieDto> dtos = allMovies.stream()
                .map(m -> new MovieDto(
                        m.getTitle(),
                        m.getYear(),
                        m.getRating(),
                        m.getGenre() == null ? "null" : m.getGenre().getName()
                ))
                .sorted()
                .collect(Collectors.toList());

        dtos.forEach(System.out::println);

        // Task 6: serialization
        writeGenresToFile(genres, FILE);
        List<Genre> readBack = readGenresFromFile(FILE);

        System.out.println("\n--- Deserialized genres ---");
        readBack.forEach(g -> {
            System.out.println(g);
            g.getMovies().forEach(m -> System.out.println("  " + m));
        });

        // Task 7: parallel processing with custom thread pool
        System.out.println("\n--- Parallel processing ---");

        System.out.println("\n--- Parallel processing genres ---");
        parallelThreadGenres(genres);

        System.out.println("\n--- Sequential processing genres ---");
        streamThreadGenres(genres);


        System.out.println("\n=== Done ===");
    }



    private static void writeGenresToFile(List<Genre> genres, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(genres);
            System.out.println("Serialized " + genres.size() + " genres -> " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Genre> readGenresFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<Genre>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

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
