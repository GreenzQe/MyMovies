package dk.easv.mymovies.GUI.Model;

import dk.easv.mymovies.BE.Category;
import dk.easv.mymovies.BE.Movie;
import dk.easv.mymovies.BLL.CategoryManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class CategoryModel {
    private ObservableList<Category> categories;
    private CategoryManager categoryManager;

    public CategoryModel() throws Exception {
        categoryManager = new CategoryManager();
        categories = FXCollections.observableArrayList();
        categories.addAll(categoryManager.getAllCategories());
    }

    public ObservableList<Category> getCategories() throws Exception {
        categories.clear();
        List<Category> allCategories = categoryManager.getAllCategories();
        System.out.println("Fetched categories from CategoryManager: " + allCategories); // Debug statement
        categories.addAll(allCategories);
        return categories;
    }

    public void addCategory(Category category) throws Exception {
        categoryManager.createCategory(category);
        categories.add(category);
    }

    public void updateCategory(Category category, Movie movie) throws Exception {
        categoryManager.updateCategory(category, movie);
    }

    public void deleteCategory(Category category) throws Exception {
        categoryManager.deleteCategory(category);
        categories.remove(category);
    }
}