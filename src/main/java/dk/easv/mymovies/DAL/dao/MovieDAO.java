package dk.easv.mymovies.DAL.dao;

import dk.easv.mymovies.BE.Category;
import dk.easv.mymovies.BE.Movie;
import dk.easv.mymovies.DAL.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MovieDAO implements IMovieDAO {
    private final DBConnector dbConnector;
    private final CategoryDAO categoryDAO;

    public MovieDAO() throws Exception {
        try {
            this.dbConnector = new DBConnector();
            this.categoryDAO = new CategoryDAO();
        } catch (Exception e) {
            throw new Exception("An error occurred while initializing DB connection");
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
                double pRating = rs.getDouble("pRating");
                String fileLink = rs.getString("fileLink");
                String lastView = rs.getString("lastView");
                double iRating = rs.getDouble("iRating");
                String posterLink = rs.getString("posterLink");

                movies.add(new Movie(id, name, pRating, fileLink, lastView, iRating, posterLink, getCategoriesFromMovie(id)));
            }
            return movies;
        } catch (Exception e) {
            throw new Exception("Could not get all movies from database");
        }
    }

    @Override
    public Movie createMovie(Movie movie) throws Exception {
        String query = "INSERT INTO Movie (name, pRating, fileLink, lastView, iRating, posterLink) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, movie.getName());
            stmt.setDouble(2, movie.getpRating());
            stmt.setString(3, movie.getFileLink());
            stmt.setDate(4, new Date(System.currentTimeMillis()));
            stmt.setDouble(5, movie.getiRating());
            stmt.setString(6, movie.getPosterLink());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 1) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        movie.setId(rs.getInt(1));
                    }
                }
            }

            // Insert categories and link them with the movie
            for (Category category : movie.getCategories().values()) {
                int categoryId = categoryDAO.createCategory(category).getId();
                linkMovieWithCategory(movie.getId(), categoryId);
            }

            return movie;
        } catch (SQLException e) {
            throw new Exception("Could not create movie in database", e);
        }
    }

    private void linkMovieWithCategory(int movieId, int categoryId) throws Exception {
        String query = "INSERT INTO CatMovie (movieId, categoryId) VALUES (?, ?)";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, movieId);
            stmt.setInt(2, categoryId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Could not link movie with category in database", e);
        }
    }

    @Override
    public boolean updateMovie(Movie movie) throws Exception {
        String query = "UPDATE Movie SET name = ?, pRating = ?, fileLink = ?, lastView = ?, iRating = ?, posterLink = ? WHERE id = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, movie.getName());
            stmt.setDouble(2, movie.getpRating());
            stmt.setString(3, movie.getFileLink());
            stmt.setString(4, movie.getLastView());
            stmt.setDouble(5, movie.getiRating());
            stmt.setString(6, movie.getPosterLink());
            stmt.setInt(7, movie.getId());

            int rowsAffected = stmt.executeUpdate();

            for (Category category : movie.getCategories().values()) {
                int categoryId = category.getId();

                if (categoryId != 0)
                    continue;

                categoryDAO.createCategory(category);
            }

            updateMovieCategories(movie.getId(), movie.getCategories().values());

            return rowsAffected == 1;
        } catch (SQLException e) {
            throw new Exception("Could not update movie in database", e);
        }
    }

    private void updateMovieCategories(int movieId, Collection<Category> categories) throws Exception {
        String deleteQuery = "DELETE FROM CatMovie WHERE movieId = ?";
        String insertQuery = "INSERT INTO CatMovie (movieId, categoryId) VALUES (?, ?)";

        try (Connection conn = dbConnector.getConnection()) {
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, movieId);
                deleteStmt.executeUpdate();
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                for (Category category : categories) {
                    insertStmt.setInt(1, movieId);
                    insertStmt.setInt(2, category.getId());
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }
        } catch (SQLException e) {
            throw new Exception("Could not update movie categories in database", e);
        }
    }

    @Override
    public boolean deleteMovie(Movie movie) throws Exception {
        String query = "DELETE FROM Movie WHERE id = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, movie.getId());
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            throw new Exception("Could not delete movie from database", e);
        }
    }

    @Override
    public List<Movie> getRecommendedToRemove() throws Exception {
        List<Movie> movies = new ArrayList<>();

        String query = "SELECT * FROM Movie WHERE pRating <= 6 AND lastView <= DATEADD(YEAR, -2, GETDATE());";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double pRating = rs.getDouble("pRating");
                String fileLink = rs.getString("fileLink");
                String lastView = rs.getString("lastView");
                double iRating = rs.getDouble("iRating");
                String posterLink = rs.getString("posterLink");

                movies.add(new Movie(id, name, pRating, fileLink, lastView, iRating, posterLink));
            }
            return movies;
        } catch (Exception e) {
            throw new Exception("Could not get all movies from database");
        }
    }

    private HashMap<String, Category> getCategoriesFromMovie(int movieID) throws Exception {
        HashMap<String, Category> genres = new HashMap<>();
        String SQL = """
                SELECT Category.id, Category.name FROM CatMovie 
                INNER JOIN Category ON CatMovie.categoryId = Category.id 
                INNER JOIN Movie ON CatMovie.movieId = Movie.id 
                WHERE Movie.id = ?;
                """;
        try(Connection conn = dbConnector.getConnection(); PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setInt(1, movieID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next() && genres.size() < 3) {
                int id = rs.getInt("id");
                String name = rs.getString("name");

                genres.put(name, new Category(id, name));
            }

            return genres;
        }
        catch (Exception e){


            throw new Exception("Couldn't get Categories from movie with ID: " + movieID);
        }
    }
}