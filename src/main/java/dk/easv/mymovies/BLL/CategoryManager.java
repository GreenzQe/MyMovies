package dk.easv.mymovies.BLL;

import dk.easv.mymovies.BE.Category;
import dk.easv.mymovies.BE.Movie;
import dk.easv.mymovies.DAL.dao.CategoryDAO;
import dk.easv.mymovies.DAL.dao.ICategoryDAO;

import java.util.List;

public class CategoryManager {
    private final ICategoryDAO categoryDAO;

    public CategoryManager() throws Exception {
        this.categoryDAO = new CategoryDAO();
    }

    public Category createCategory(Category category) throws Exception {
        return categoryDAO.createCategory(category);
    }

    public boolean updateCategory(Category category, Movie movie) throws Exception {
        return categoryDAO.updateCategory(category, movie);
    }

    public void deleteCategory(Category category) throws Exception {
        categoryDAO.deleteCategory(category);
    }

    public List<Category> getAllCategories() throws Exception {
        return categoryDAO.getAllCategories();
    }

}