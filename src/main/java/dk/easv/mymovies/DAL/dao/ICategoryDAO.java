package dk.easv.mymovies.DAL.dao;

import dk.easv.mymovies.BE.Category;
import dk.easv.mymovies.BE.Movie;

import java.util.ArrayList;
import java.util.List;

public interface ICategoryDAO {
    List<Category> getAllCategories() throws Exception;
    List<Category> getAllCategoriesWithMovies() throws Exception;

    ArrayList<Category> getAllCategoriesMovies(int CategoryId) throws Exception;

    Category createCategory(Category category) throws Exception;
    boolean deleteCategory(Category category) throws Exception;
    boolean updateCategory(Category category, Movie movie) throws Exception;
}
