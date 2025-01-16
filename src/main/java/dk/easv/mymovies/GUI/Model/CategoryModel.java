package dk.easv.mymovies.GUI.Model;

import dk.easv.mymovies.BE.Category;
import dk.easv.mymovies.BE.Movie;
import dk.easv.mymovies.BLL.CategoryManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
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
        categories.addAll(allCategories);
        return categories;
    }

    public Category addCategory(Category category) throws Exception {
        Category categoryCreated = categoryManager.createCategory(category);
        categories.add(categoryCreated);

        return categoryCreated;
    }

    public void updateCategory(Category category, Movie movie) throws Exception {
        categoryManager.updateCategory(category, movie);
    }

    public void deleteCategory(Category category) throws Exception {
        categoryManager.deleteCategory(category);
        categories.remove(category);
    }

    public boolean deleteCategoryMultiple(ArrayList<Category> categories, ObservableList<Movie> movies, Movie movie) throws Exception {
        ArrayList<Category> deleteCategories = new ArrayList<>();

        for (Category category : categories) {
            int matches = 0;
            for (Movie m : movies) {
                if (m.getFileLink().equals(movie.getFileLink()))
                    continue;

                if (m.getCategory(category.getName()) != null)
                    matches++;
            }

            if (matches != 0)
                continue;

            deleteCategories.add(category);
        }

        return categoryManager.deleteCategoryMultiple(deleteCategories);
    }
}