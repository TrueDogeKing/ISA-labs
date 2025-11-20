package com.example.utils;


import com.example.Entities.Genre;
import com.example.Entities.Movie;
import com.example.repositories.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;


@Component
public class ConsoleRunner implements CommandLineRunner {

    private final GenreService genreService;
    private final MovieService movieService;
    private final ConfigurableApplicationContext context;

    public ConsoleRunner(GenreService genreService, MovieService movieService,ConfigurableApplicationContext context) {
        this.genreService = genreService;
        this.movieService = movieService;
        this.context = context;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        printHelp();

        while (running) {
            System.out.print("\nEnter command: ");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "help" -> printHelp();

                case "list categories" -> listCategories();

                case "list movies" -> listMovies();

                case "add movie" -> addMovie(scanner);

                case "delete movie" -> deleteMovie(scanner);

                case "exit" -> {
                    System.out.println("Stopping application...");
                    running = false;
                    SpringApplication.exit(context, () -> 0); // 0 = exit code
                }

                default -> System.out.println("Unknown command. Type 'help' to see available commands.");
            }
        }

        scanner.close();
    }

    private void printHelp() {
        System.out.println("=== Available Commands ===");
        System.out.println("help             - show this help");
        System.out.println("list categories  - list all genres");
        System.out.println("list movies      - list all movies");
        System.out.println("add movie        - add a new movie");
        System.out.println("delete movie     - delete a movie by title");
        System.out.println("exit             - stop the application");
    }

    private void listCategories() {
        List<Genre> genres = genreService.findAll();
        System.out.println("=== Genres ===");
        genres.forEach(g -> System.out.println(g.getName() + " (" + g.getDescription() + ")"));
    }

    private void listMovies() {
        System.out.println("=== Movies ===");
        movieService.findAll().forEach(System.out::println);
    }


    private void addMovie(Scanner scanner) {
        System.out.print("Enter movie title: ");
        String title = scanner.nextLine();

        int year = 0;
        int currentYear = java.time.Year.now().getValue();
        while (true) {
            System.out.print("Enter release year: ");
            year = Integer.parseInt(scanner.nextLine());
            if (year > currentYear) {
                System.out.println("Wrong value: release year cannot be in the future (" + currentYear + " is the current year).");
            }else{
                break;
            }
        }

        double rating =0;
        while (true){
            System.out.print("Enter rating (0–10): ");
            rating = Double.parseDouble(scanner.nextLine());
            if (rating < 0 || rating > 10) {
                System.out.println("Wrong value: rating must be between 0 and 10.");
            }else{
                break;
            }
        }

        listCategories();
        System.out.print("Enter genre name: ");
        String genreName = scanner.nextLine();

        Genre genre = genreService.findByName(genreName);
        if (genre == null) {
            System.out.println("Genre not found. Movie will be added without genre.");
        }

        Movie movie = Movie.builder()
                .id(UUID.randomUUID())
                .title(title)
                .releaseYear(year)
                .rating(rating)
                .genre(genre)
                .build();

        movieService.save(movie);

        System.out.println("Movie added successfully!");
    }


    private void deleteMovie(Scanner scanner) {
        System.out.print("Enter movie title to delete: ");
        String title = scanner.nextLine();

        List<Movie> movies = movieService.findAll();
        movies.stream()
                .filter(m -> m.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .ifPresentOrElse(
                        m -> {
                            movieService.deleteById(m.getId());
                            System.out.println("Movie deleted successfully.");
                        },
                        () -> System.out.println("Movie not found.")
                );
    }
}
