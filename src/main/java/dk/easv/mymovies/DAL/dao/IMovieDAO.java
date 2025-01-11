package dk.easv.mymovies.DAL.dao;

import dk.easv.mymovies.BE.Movie;

import java.util.List;

public interface IMovieDAO {
    List<Movie> getAllMovies() throws Exception;
    Movie createMovie(Movie movie) throws Exception;
    boolean updateMovie(Movie movie) throws Exception;
    void deleteMovie(Movie movie) throws Exception;
}
