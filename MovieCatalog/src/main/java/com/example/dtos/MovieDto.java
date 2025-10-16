package com.example.dtos;

public class MovieDto implements Comparable<MovieDto> {
    private String title;
    private int year;
    private double rating;
    private String genreName;

    public MovieDto(String title, int year, double rating, String genreName) {
        this.title = title;
        this.year = year;
        this.rating = rating;
        this.genreName = genreName;
    }

    @Override
    public int compareTo(MovieDto other) {

        if(this.rating!=other.rating){
            return Double.compare(other.rating,this.rating);
        }else if(this.year!=other.year){
            return Integer.compare(other.year,this.year);
        }else if (this.genreName.compareTo(other.genreName) != 0) {
            return other.genreName.compareTo(this.genreName);
        } else {
            return this.title.compareTo(other.title);
        }
    }

    @Override
    public String toString() {
        return "MovieDto{title='" + title + "', year=" + year + ", rating=" + rating + ", genre='" + genreName + "'}";
    }
}