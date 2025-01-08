package dk.easv.mymovies.DAL.dao;

import dk.easv.mymovies.BE.Movie;

import java.util.List;

public interface IMovieDAO {
    List<Movie> getAllMovies() throws Exception;
}
