package com.example.utils;

import com.example.Entities.Genre;

import java.io.*;
import java.util.Collections;
import java.util.List;

public class Serialization {
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
}
