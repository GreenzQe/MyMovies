package dk.easv.mymovies.BE;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private int id;
    private String name;
    private List<Movie> movies;

    public Category(String name) {
        this.name = name;
        this.movies = new ArrayList<Movie>();
    }
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
        this.movies = new ArrayList<>();
    }
    public Category(int id, String name, List<Movie> movies) {
        this.id = id;
        this.name = name;
        this.movies = movies;
    }
    public Category(String name, List<Movie> movies) {
        this.name = name;
        this.movies = movies;
    }

    public String getName() {return this.name;}


    public int setId(int anInt) {return this.id = anInt;}

    public int getId() {return id;}

    @Override
    public String toString() {
        return name;
    }
}
