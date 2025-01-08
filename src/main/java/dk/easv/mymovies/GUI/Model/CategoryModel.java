package dk.easv.mymovies.GUI.Model;

import dk.easv.mymovies.BE.Category;
import dk.easv.mymovies.BE.Movie;
import dk.easv.mymovies.BLL.CategoryManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CategoryModel {
    private final ObservableList<Category> categories;
    private final CategoryManager categoryManager;

    public CategoryModel() throws Exception {

        try {
            categoryManager = new CategoryManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        categories = FXCollections.observableArrayList();
        categories.addAll(categoryManager.getAllCategories());
    }

    public Category createCategory(Category category) throws Exception {
        return categoryManager.createCategory(category);
    }

    public ObservableList<Category> getCategories() {return categories;}

    public void addCategory(Category category) {categories.add(category);}

    public void removeCategory(Category category) throws Exception {
        boolean deletedb = categoryManager.deleteCategory(category);

        if (deletedb) {
            categories.remove(category);
        }
    }

    public void addMovieToCategory(Category category, Movie movie) {
        category.addMovie(movie);
    }

    public void removeMovieFromCategory(Category category, Movie movie) { category.removeMovie(movie);}

    public boolean updateCategory(int idx, Category category, Movie movie) throws Exception {

        // baggrundstråd - for at minimere lag
        Platform.runLater(() -> {
            try {
                boolean didUpdateDB = categoryManager.updateCategory(category, movie);

                if (!didUpdateDB)
                    return;

                System.out.println("Added");

                movie.addCategory(category);

                addMovieToCategory(category, movie);


            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return true;
    }
}
