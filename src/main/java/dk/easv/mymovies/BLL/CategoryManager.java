package dk.easv.mymovies.BLL;

import dk.easv.mymovies.BE.Category;
import dk.easv.mymovies.BE.Movie;
import dk.easv.mymovies.DAL.dao.CategoryDAO;
import dk.easv.mymovies.DAL.dao.ICategoryDAO;

import java.util.List;

public class CategoryManager {
    private final ICategoryDAO categoryDAO;

    public CategoryManager() throws Exception {
        this.categoryDAO = new CategoryDAO() {
        };
    }

    public List<Category> getAllCategories() throws Exception {
        return this.categoryDAO.getAllCategoriesWithMovies();
    }

    public Category createCategory(Category category) throws Exception {
        return this.categoryDAO.createCategory(category);
    }

    public boolean deleteCategory(Category category) throws Exception {
        return this.categoryDAO.deleteCategory(category);
    }

    public boolean updateCategory(Category category, Movie movie) throws Exception {
        return this.categoryDAO.updateCategory(category, movie);
    }
}
