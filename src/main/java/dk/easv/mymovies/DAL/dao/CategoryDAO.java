package dk.easv.mymovies.DAL.dao;

import dk.easv.mymovies.BE.Category;
import dk.easv.mymovies.BE.Movie;
import dk.easv.mymovies.DAL.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements ICategoryDAO {
    private final DBConnector dbConnector;

    public CategoryDAO() throws Exception{
        try {
            this.dbConnector = new DBConnector();
        } catch (Exception e) {
            throw new Exception("An error occured while creating the DB connection");
        }
    }

    @Override
    public List<Category> getAllCategories() throws Exception {
        List<Category> categories = new ArrayList<>();

        String query = "SELECT * FROM Category ORDER BY id";

        try (Connection conn = dbConnector.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");

                categories.add(new Category(id, name));
            }

            return categories;
        } catch (Exception e) {
            throw new Exception("An error occured while getting all categories");
        }
    }

    @Override
    public List<Category> getAllCategoriesWithMovies() throws Exception {
        return List.of();
    }

    @Override
    public ArrayList<Category> getAllCategoriesMovies(int CategoryId) throws Exception {
        return null;
    }

    @Override
    public Category createCategory(Category category) throws Exception {
        String selectQuery = "SELECT id FROM Category WHERE name = ?";
        String insertQuery = "INSERT INTO Category (name) VALUES (?)";

        try (Connection conn = dbConnector.getConnection()) {
            // Check if the category already exists
            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                selectStmt.setString(1, category.getName());
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        category.setId(rs.getInt("id"));
                        return category;
                    }
                }
            }

            // Insert the new category
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, category.getName());
                int rowsAffected = insertStmt.executeUpdate();

                if (rowsAffected == 1) {
                    try (ResultSet rs = insertStmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            category.setId(rs.getInt(1));
                            return category;
                        }
                    }
                }
                throw new Exception("Could not insert category in database");
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            throw new Exception("Could not insert category in database", e);
        }
    }

    @Override
    public boolean deleteCategory(Category category) throws Exception {
        String deleteQuery = "DELETE FROM Category WHERE id = ?";

        try (Connection conn = dbConnector.getConnection()) {
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, category.getId());

                deleteStmt.executeQuery();
            }

            return true;
        } catch (SQLException e) {
            throw new Exception("Could not update movie categories in database", e);
        }

    }

    @Override
    public boolean deleteCategoryMultiple(ArrayList<Category> categories) throws Exception {
        String deleteQuery = "DELETE FROM Category WHERE id = ?";

        try (Connection conn = dbConnector.getConnection()) {
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
               for (Category category : categories) {
                    deleteStmt.setInt(1, category.getId());

                    deleteStmt.addBatch();
               }
               deleteStmt.executeBatch();
            }

            return true;
        } catch (SQLException e) {
            throw new Exception("Could not update movie categories in database", e);
        }
    }

    @Override
    public boolean updateCategory(Category category, Movie movie) throws Exception {
        String query = """
                INSERT INTO CatMovie (CategoryId, MovieId)
                       VALUES(?,?);
                """;

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, category.getId());
            stmt.setInt(2, movie.getId());

            int rowsAffected = stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                //if (rs.next()) {
                    //playlist.setId(rs.getInt(1));
                //}
                return rs.next();
            }

        } catch (Exception e) {
            throw new Exception("Could not add movie to category in DB");
        }
    }


}
