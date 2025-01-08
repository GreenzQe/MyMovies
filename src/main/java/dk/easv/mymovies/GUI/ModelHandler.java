package dk.easv.mymovies.GUI;

import dk.easv.mymovies.GUI.Model.CategoryModel;
import dk.easv.mymovies.GUI.Model.MovieModel;

public class ModelHandler {
    private static ModelHandler instance;

    private CategoryModel categoryModel;
    private MovieModel movieModel;

    private ModelHandler() throws Exception {
        try {
            categoryModel = new CategoryModel();
            movieModel = new MovieModel();
        } catch (Exception e) {
            throw new Exception("Could not create ModelHandlers");
        }
    }


    public static ModelHandler getInstance() {
        if (instance == null) {
            try {
                instance = new ModelHandler();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return instance;
    }

    public CategoryModel getCategoryModel() { return categoryModel; }

    public MovieModel getMovieModel() { return movieModel; }
}
