package dk.easv.mymovies.GUI.Controller;

import dk.easv.mymovies.BE.Category;
import dk.easv.mymovies.BE.Movie;
import dk.easv.mymovies.BLL.MovieManager;
import dk.easv.mymovies.GUI.Model.CategoryModel;
import dk.easv.mymovies.GUI.Model.MovieModel;
import dk.easv.mymovies.GUI.utils.ErrorPopup;
import dk.easv.mymovies.GUI.utils.ShowAlert;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddEditMovieController {
    @FXML
    private TextField tfdName;
    @FXML
    private TextField tfdIRating;
    @FXML
    private TextField tfdPRating;
    @FXML
    private ComboBox<String> cbbCat1;
    @FXML
    private ComboBox<String> cbbCat2;
    @FXML
    private ComboBox<String> cbbCat3;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnAddMovieFile;
    @FXML
    private Button btnAddPosterFile;
    @FXML
    private Label lblMovieFileName;
    @FXML
    private ImageView imvPoster;
    @FXML
    private Label lblPosterFileName;
    @FXML
    private Button btnDelete;

    private MovieModel movieModel;
    private MovieManager movieManager;
    private CategoryModel categoryModel;
    private Movie movie;

    private HashMap<String, Category> categories;

    public AddEditMovieController() {
        try {
            movieManager = new MovieManager();
        } catch (Exception e) {
            ErrorPopup.showAlert(ShowAlert.ERROR, e.getMessage());;
        }
    }

    @FXML
    public void initialize() throws Exception {
        updateController(new MovieModel(), new CategoryModel());

        btnCancel.setOnAction(event -> closeWindow());
        btnSave.setOnAction(event -> saveMovie());
        btnAddMovieFile.setOnAction(event -> openFilePicker(lblMovieFileName));
        btnAddPosterFile.setOnAction(event -> openFilePicker(lblPosterFileName, imvPoster));
        btnDelete.setOnAction(event -> deleteMovie());

        populateCategoriesHashMap();
        populateCategories();

    }

    public void updateController(MovieModel movieModel, CategoryModel categoryModel) {
        this.movieModel = movieModel;
        this.categoryModel = categoryModel;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        if (movie != null) {
            System.out.println("Movie categories: " + movie.getCategories()); // Debug statement
            updateMovie(movie);
            btnDelete.setVisible(true);
        } else {
            btnDelete.setVisible(false);
        }
    }

    private void populateCategoriesHashMap() {
        this.categories = new HashMap<>(0);
        try {
            List<Category> categories = categoryModel.getCategories();

            // for hver kategori, tilføj sæt til hashmap f.eks:
            // this.categories.get("Drama") giver dig Category BE for drama
            for (Category category : categories)
                this.categories.put(category.getName(), category);

        } catch (Exception e) {
            ErrorPopup.showAlert(ShowAlert.ERROR, e.getMessage());
        }
    }

    private void populateCategories() {
        try {
            cbbCat1.getItems().setAll(categories.keySet());
            cbbCat2.getItems().setAll(categories.keySet());
            cbbCat3.getItems().setAll(categories.keySet());

            if (movie != null && movie.getCategories() != null) {
                HashMap<String, Category> movieCategories = movie.getCategories();
                List<Category> categoryList = new ArrayList<>(movieCategories.values());

                if (!categoryList.isEmpty()) cbbCat1.setValue(categoryList.get(0).getName());
                if (categoryList.size() > 1) cbbCat2.setValue(categoryList.get(1).getName());
                if (categoryList.size() > 2) cbbCat3.setValue(categoryList.get(2).getName());
            }
        } catch (Exception e) {
            ErrorPopup.showAlert(ShowAlert.ERROR, "Failed to get categories from database");
        }
    }



    public void updateMovie(Movie movie) {
        this.movie = movie;
        tfdName.setText(movie.getName());
        tfdIRating.setText(String.valueOf(movie.getiRating()));
        tfdPRating.setText(String.valueOf(movie.getpRating()));
        lblMovieFileName.setText(movie.getFileLink());
        lblPosterFileName.setText(movie.getPosterLink());
        try {
            File posterFile = new File(movie.getPosterLink());
            if (posterFile.exists()) {
                imvPoster.setImage(new Image(posterFile.toURI().toString()));
            } else {
                ErrorPopup.showAlert(ShowAlert.WARNING, "File not found: " + posterFile.getAbsolutePath());
            }
        } catch (Exception e) {
            System.out.println("Failed to load image for movie: " + movie.getName());
            imvPoster.setImage(new Image(getClass().getResource("/Images/Fallback.png").toExternalForm()));
        }

    }

    private void saveMovie() {
        try {
            String name = tfdName.getText();
            double imdbRating = Double.parseDouble(tfdIRating.getText().replace(',', '.'));
            double yRating = Double.parseDouble(tfdPRating.getText().replace(',', '.'));
            String fileLink = lblMovieFileName.getText();
            String lastView = ""; // Set this to the appropriate value
            String posterLink = lblPosterFileName.getText();

            String cValue1 = cbbCat1.getValue();
            String cValue2 = cbbCat2.getValue();
            String cValue3 = cbbCat3.getValue();

            HashMap<String, Category> categories = new HashMap<>();
            Category first = this.categories.get(cValue1);
            Category second = this.categories.get(cValue2);
            Category third = this.categories.get(cValue3);

            if (first != null)
                categories.put(first.getName(), first);
            else if (cValue1 != null && cValue1.length() > 2){
                categories.put(cValue1, new Category(cValue1));
            }

            if  (second != null)
                categories.put(second.getName(), second);
            else if (cValue2 != null && cValue2.length() > 2){
                categories.put(cValue2, new Category(cValue2));
            }

            if (third != null)
                categories.put(third.getName(), third);
            else if (cValue3 != null && cValue3.length() > 2){
                categories.put(cValue3, new Category(cValue3));
            }


            if (movie == null) {
                movie = new Movie(0, name, yRating, fileLink, lastView, imdbRating, posterLink, categories);
                movieModel.addMovie(movie);
            } else {
                ArrayList<Category> removeCategories = new ArrayList<>();
                for (Category c : movie.getCategories().values()) {
                    if (!categories.containsKey(c.getName())) {
                        removeCategories.add(c);
                    }
                }

                movie.setName(name);
                movie.setiRating(imdbRating);
                movie.setpRating(yRating);
                movie.setFileLink(fileLink);
                movie.setPosterLink(posterLink);
                movie.setCategories(categories);
                movieModel.updateMovie(movie);

                categoryModel.deleteCategoryMultiple(removeCategories, movieModel.getMovies(), movie);
            }

            refreshMoviesAndCategories();
            movieModel.updatedProperty().setValue(true);

            closeWindow();
        } catch (Exception e) {
            ErrorPopup.showAlert(ShowAlert.ERROR, "Failed to update/add movie: " + movie.getName());
        }
    }
    private void refreshMoviesAndCategories() {
        try {
            movieModel.getMovies().clear();
            movieModel.getMovies().addAll(movieManager.getAllMovies());
           // categoryModel.getCategories().clear();
            categoryModel.getCategories().setAll(categoryModel.getCategories());

            populateCategories();
        } catch (Exception e) {
            ErrorPopup.showAlert(ShowAlert.ERROR, "Failed to refresh movies");

        }
    }

    private void deleteMovie() {
        try {
            if (movie != null) {
                movieModel.deleteMovie(movie);
            }
            closeWindow();
        } catch (Exception e) {
            ErrorPopup.showAlert(ShowAlert.ERROR, "Failed to delete movie");
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void openFilePicker(Label label) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            label.setText(selectedFile.getAbsolutePath());
        }
    }

    private void openFilePicker(Label label, ImageView imageView) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            label.setText(selectedFile.getAbsolutePath());
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
            imageView.setFitWidth(135);
            imageView.setFitHeight(172);
            imageView.setPreserveRatio(true);
        }
    }
}