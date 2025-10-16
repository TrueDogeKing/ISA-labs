package com.example;

import java.io.*;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import com.example.Entities.*;
import com.example.dtos.*;

public class Main {
    private static final String FILE = "genres.bin";

    public static void main(String[] args) throws Exception {
        System.out.println("=== Movies Lab ===");

        // Task 2: create data
        List<Genre> genres = createSampleData();

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

    private static List<Genre> createSampleData() {
        Genre sciFi = Genre.builder()
                .name("Sci-Fi")
                .description("Futuristic movies")
                .build();

        Genre drama = Genre.builder()
                .name("Drama")
                .description("Dramatic movies")
                .build();

        Genre action = Genre.builder()
                .name("Action")
                .description("Movies with racing/shooting")
                .build();

// Movies are automatically added to genres
        // Create movies separately and attach to genres
        Movie m1 = Movie.builder().title("Inception").year(2010).rating(8.8).build();
        m1.setGenre(sciFi);
        sciFi.addMovie(m1);

        Movie m2 = Movie.builder().title("Interstellar").year(2014).rating(8.9).build();
        m2.setGenre(sciFi);
        sciFi.addMovie(m2);

        Movie m3 = Movie.builder().title("The Martian").year(2015).rating(9.0).build();
        m3.setGenre(sciFi);
        sciFi.addMovie(m3);

        Movie m4 = Movie.builder().title("The Shawshank Redemption").year(1994).rating(9.3).build();
        m4.setGenre(drama);
        drama.addMovie(m4);

        Movie m5 = Movie.builder().title("Forrest Gump").year(1994).rating(8.8).build();
        m5.setGenre(drama);
        drama.addMovie(m5);

        Movie m6 = Movie.builder().title("Mad Max: Fury Road").year(2015).rating(6.4).build();
        m6.setGenre(action);
        action.addMovie(m6);

        Movie m7 = Movie.builder().title("John Wick").year(2014).rating(7.4).build();
        m7.setGenre(action);
        action.addMovie(m7);


        List<Genre> genres = List.of(sciFi, drama, action);
        return List.of(sciFi, drama, action);

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
