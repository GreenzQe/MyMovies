package dk.easv.mymovies.DAL.dao;

import dk.easv.mymovies.BE.Movie;
import dk.easv.mymovies.DAL.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO implements IMovieDAO {
    private final DBConnector dbConnector;

    public MovieDAO() throws Exception {
        try {
            this.dbConnector = new DBConnector();
        } catch (Exception e) {
            throw new Exception("An error occured while initializing DB connection");
        }
    }
    @Override
    public List<Movie> getAllMovies() throws Exception {
        List<Movie> movies = new ArrayList<>();

        String query = "SELECT * FROM Movie";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Float pRating = rs.getFloat("pRating");
                String fileLink = rs.getString("fileLink");
                String lastView = rs.getString("lastView");
                Float iRating = rs.getFloat("iRating");
                String posterLink = rs.getString("posterLink");

                movies.add(new Movie(id, name, pRating, fileLink, lastView, iRating, posterLink ));

            }
            return movies;
        } catch (Exception e) {
            throw new Exception("Could not get all movies from database");
        }
    }

}
