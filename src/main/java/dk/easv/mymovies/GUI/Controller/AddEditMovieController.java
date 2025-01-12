package dk.easv.mymovies.GUI.Controller;

import dk.easv.mymovies.BE.Category;
import dk.easv.mymovies.BE.Movie;
import dk.easv.mymovies.BLL.MovieManager;
import dk.easv.mymovies.GUI.Model.CategoryModel;
import dk.easv.mymovies.GUI.Model.MovieModel;
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
import java.util.List;

public class AddEditMovieController {
    @FXML
    private TextField tfdName;
    @FXML
    private TextField tfdIRating;
    @FXML
    private TextField tfdPRating;
    @FXML
    private ComboBox<Category> cbbCat1;
    @FXML
    private ComboBox<Category> cbbCat2;
    @FXML
    private ComboBox<Category> cbbCat3;
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

    public AddEditMovieController() {
        try {
            movieModel = new MovieModel();
            categoryModel = new CategoryModel();
            movieManager = new MovieManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        btnCancel.setOnAction(event -> closeWindow());
        btnSave.setOnAction(event -> saveMovie());
        btnAddMovieFile.setOnAction(event -> openFilePicker(lblMovieFileName));
        btnAddPosterFile.setOnAction(event -> openFilePicker(lblPosterFileName, imvPoster));
        btnDelete.setOnAction(event -> deleteMovie());
        populateCategories();
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
        //populateCategories();
    }

    private void populateCategories() {
        try {
            List<Category> categories = categoryModel.getCategories();
            System.out.println("All categories: " + categories); // Debug statement
            cbbCat1.getItems().setAll(categories);
            cbbCat2.getItems().setAll(categories);
            cbbCat3.getItems().setAll(categories);

            if (movie != null) {
                List<Category> movieCategories = movie.getCategories();
                System.out.println("Movie categories in populateCategories: " + movieCategories); // Debug statement
                if (movieCategories != null) {
                    if (movieCategories.size() > 0) cbbCat1.setValue(movieCategories.get(0));
                    if (movieCategories.size() > 1) cbbCat2.setValue(movieCategories.get(1));
                    if (movieCategories.size() > 2) cbbCat3.setValue(movieCategories.get(2));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                throw new IOException("File not found: " + posterFile.getAbsolutePath());
            }
        } catch (Exception e) {
            System.out.println("Failed to load image for movie: " + movie.getName());
            imvPoster.setImage(new Image(getClass().getResource("/Images/Fallback.png").toExternalForm()));
        }
    }

    private void saveMovie() {
        try {
            String name = tfdName.getText();
            float imdbRating = Float.parseFloat(tfdIRating.getText().replace(',', '.'));
            float yRating = Float.parseFloat(tfdPRating.getText().replace(',', '.'));
            String fileLink = lblMovieFileName.getText();
            String lastView = ""; // Set this to the appropriate value
            String posterLink = lblPosterFileName.getText();
            List<Category> categories = List.of(
                    cbbCat1.getValue(),
                    cbbCat2.getValue(),
                    cbbCat3.getValue()
            );

            if (movie == null) {
                movie = new Movie(0, name, yRating, fileLink, lastView, imdbRating, posterLink, categories);
                movieModel.addMovie(movie);
            } else {
                movie.setName(name);
                movie.setiRating(imdbRating);
                movie.setpRating(yRating);
                movie.setFileLink(fileLink);
                movie.setPosterLink(posterLink);
                movie.setCategories(categories);
                movieModel.updateMovie(movie);
            }

            closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the error (e.g., show an alert)
        }
    }

    private void deleteMovie() {
        try {
            if (movie != null) {
                movieModel.deleteMovie(movie);
            }
            closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the error (e.g., show an alert)
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