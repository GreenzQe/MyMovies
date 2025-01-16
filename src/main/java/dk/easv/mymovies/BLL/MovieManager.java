package dk.easv.mymovies.BLL;

import dk.easv.mymovies.BE.Movie;
import dk.easv.mymovies.DAL.dao.IMovieDAO;
import dk.easv.mymovies.DAL.dao.MovieDAO;

import java.util.List;

public class MovieManager {
    private final IMovieDAO movieDAO;

    public MovieManager() throws Exception {
        this.movieDAO = new MovieDAO();
    }

    public Movie createMovie(Movie movie) throws Exception {
        return movieDAO.createMovie(movie);
    }

    public boolean updateMovie(Movie movie) throws Exception {
        return movieDAO.updateMovie(movie);
    }

    public boolean deleteMovie(Movie movie) throws Exception {
        return movieDAO.deleteMovie(movie);
    }

    public List<Movie> getAllMovies() throws Exception {
        return this.movieDAO.getAllMovies();
    }

    public List<Movie> getRecommendedToRemove() throws Exception {
        return this.movieDAO.getRecommendedToRemove();
    }
}
