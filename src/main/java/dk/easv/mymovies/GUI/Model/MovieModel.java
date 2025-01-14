package dk.easv.mymovies.GUI.Model;

import dk.easv.mymovies.BE.Movie;
import dk.easv.mymovies.BLL.MovieManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class MovieModel {

    private ObservableList<Movie> movies;
    private MovieManager movieManager;

    private ObservableList<Movie> allMovies;

    public MovieModel() throws Exception{
        movieManager = new MovieManager();

        movies = FXCollections.observableArrayList();
        allMovies = FXCollections.observableArrayList();
        allMovies.addAll(movieManager.getAllMovies());
    }

    public ObservableList<Movie> getMovies() throws Exception {
        movies = FXCollections.observableArrayList();
        movies.addAll(movieManager.getAllMovies());
        return movies;
    }

    public ObservableList<Movie> searchMovie(String searchWord) {
        List<Movie> searchMovies = new ArrayList<>();
        for(Movie movie : allMovies) {
            String movieName = movie.getName() + " " + movie.getName();
            if (movieName.toLowerCase().contains(searchWord.toLowerCase())) {
                searchMovies.add(movie);
            }
        }
        return FXCollections.observableArrayList(searchMovies);
    }

    public void addMovie(Movie movie) throws Exception {
        movieManager.createMovie(movie);
        movies.clear();
        movies.addAll(movieManager.getAllMovies());
    }
    public void updateMovie(Movie movie) throws Exception {
        if (movieManager.updateMovie(movie)) {
            int index = movies.indexOf(movie);
            if (index >= 0) {
                movies.set(index, movie);
            }
        }
    }

    public void deleteMovie(Movie movie) throws Exception {
        movieManager.deleteMovie(movie);
        movies.remove(movie);
    }
}
