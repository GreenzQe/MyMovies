package dk.easv.mymovies.GUI.Controller;

import dk.easv.mymovies.BE.Category;
import dk.easv.mymovies.BE.Movie;
import dk.easv.mymovies.BLL.MovieManager;
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
import java.util.List;

public class AddEditMovieController {


    @FXML
    private TextField tfdName;
    @FXML
    private TextField tfdIRating;
    @FXML
    private TextField tfdPRating;
    @FXML
    private ComboBox cbbCat1;
    @FXML
    private ComboBox cbbCat2;
    @FXML
    private ComboBox cbbCat3;
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

    private MovieManager movieManager;

    public AddEditMovieController() {
        try {
            movieManager = new MovieManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        btnSave.setOnAction(event -> saveMovie());
        btnAddMovieFile.setOnAction(event -> openFilePicker(lblMovieFileName));
        btnAddPosterFile.setOnAction(event -> openFilePicker(lblPosterFileName, imvPoster));
    }

    private void saveMovie() {
        try {
            String name = tfdName.getText();
            float imdbRating = Float.parseFloat(tfdIRating.getText());
            float yRating = Float.parseFloat(tfdPRating.getText());
            String fileLink = lblMovieFileName.getText();
            String lastView = ""; // Set this to the appropriate value
            String posterLink = lblPosterFileName.getText();
            List<Category> categories = List.of(
                    new Category(cbbCat1.getValue().toString()),
                    new Category(cbbCat2.getValue().toString()),
                    new Category(cbbCat3.getValue().toString())
            );

            Movie movie = new Movie(0, name, yRating, fileLink, lastView, imdbRating, posterLink, categories);
            movieManager.addMovie(movie);

            // Close the window
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the error (e.g., show an alert)
        }
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