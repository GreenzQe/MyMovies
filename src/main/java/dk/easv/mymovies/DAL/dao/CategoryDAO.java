package dk.easv.mymovies.DAL.dao;

import dk.easv.mymovies.BE.Category;
import dk.easv.mymovies.BE.Movie;
import dk.easv.mymovies.DAL.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
        return null;
    }

    @Override
    public boolean deleteCategory(Category category) throws Exception {
        return false;
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
