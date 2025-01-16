package dk.easv.mymovies.GUI.Model;

import dk.easv.mymovies.BE.Category;
import dk.easv.mymovies.BE.Movie;
import dk.easv.mymovies.BLL.MovieManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class MovieModel {

    private ObservableList<Movie> movies;
    private MovieManager movieManager;

    private ObservableList<Movie> allMovies;
    private final Set<String> genreSet = new HashSet<>();

    private BooleanProperty updated = new SimpleBooleanProperty(false);

    public MovieModel() throws Exception{
        movieManager = new MovieManager();

        movies = FXCollections.observableArrayList();
        allMovies = FXCollections.observableArrayList();
        allMovies.addAll(movieManager.getAllMovies());
    }

    public ObservableList<Movie> getMovies() throws Exception {
        movies = FXCollections.observableArrayList();

        if (movies.isEmpty())
            movies.addAll(movieManager.getAllMovies());

        return movies;
    }

    public ObservableList<Movie> getMoviesFromGenre(String genre, boolean on) throws Exception {
        if (on)
            genreSet.add(genre);
        else
            genreSet.remove(genre);

        ObservableList<Movie> allMovies;
        ObservableList<Movie> matchedMovies = FXCollections.observableArrayList();
        Set<Movie> addedMovies = new HashSet<>();

        try {
            allMovies = this.getMovies();
        } catch (Exception e) {
            throw new Exception("Kunne ikke få fat i alle film");
        }

        if (genreSet.isEmpty()) {
            matchedMovies.addAll(allMovies);
            return matchedMovies;
        }

        // for hver genre navn i vores genre sæt
        for (String s : genreSet) {
            // for hver film i alle film
            for (Movie movie : allMovies) {
                // hvis filmens kategorier er null eller er tomme, så bare skip den film
                if (movie.getCategories() == null || movie.getCategories().isEmpty())
                    continue;

                // få fat i filmens kategori gennem valgte kategori navn, hvis den er null så skip
                Category category = movie.getCategory(s);
                if (category == null)
                    continue;

                // hvis set ikke indeholder filmen, så tilføj den til settet og observable
                if (!addedMovies.contains(movie)) {
                    matchedMovies.add(movie);
                    addedMovies.add(movie);
                }
            }
        }

        // returner observable
        return matchedMovies;
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
        boolean deleted = movieManager.deleteMovie(movie);
        if (deleted)
            movies.remove(movie);
    }

    public List<Movie> getRecommendedToRemove() throws Exception {
        return movieManager.getRecommendedToRemove();
    }

    public BooleanProperty updatedProperty() {
        return this.updated;
    }



}
