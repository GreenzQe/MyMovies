package dk.easv.mymovies.GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

public class MyMovieController {

    public Button btnHome;
    @FXML
    private TilePane genreTilePane;

    @FXML
    private TilePane dynamicTilePane;

    @FXML
    private ScrollPane scrollPane;

    private Stage moviePlayerStage; // Keep track of the Movie Player Stage

    /**
     * Initializes the controller and binds the dynamicTilePane's width to the scrollPane's width.
     */
    @FXML
    public void initialize() {
        // Set a maximum height for genreTilePane
        genreTilePane.setMaxHeight(200); // Set your desired max height
        genreTilePane.prefHeightProperty().bind(genreTilePane.maxHeightProperty());

        // Bind the TilePane's width to the ScrollPane's viewport width
        dynamicTilePane.prefWidthProperty().bind(scrollPane.widthProperty());
    }

    public void populateGenres(List<String> genres) {
        // Clear the TilePane to avoid duplicates
        genreTilePane.getChildren().clear();

        for (String genre : genres) {
            // Create a styled HBox
            HBox genreBox = new HBox();
            genreBox.setPrefSize(180, 34); // Set preferred size
            genreBox.setStyle("-fx-background-color: #25272D; -fx-background-radius: 4;");

            // Create a CheckBox with the genre name
            CheckBox checkBox = new CheckBox(genre);
            checkBox.setTextFill(javafx.scene.paint.Color.WHITE); // Set text color to white
            checkBox.setPrefSize(180, 34); // Match the size of the HBox
            checkBox.setPadding(new javafx.geometry.Insets(0, 0, 0, 8)); // Add left padding

            // Add the CheckBox to the HBox
            genreBox.getChildren().add(checkBox);

            // Add the HBox to the TilePane
            genreTilePane.getChildren().add(genreBox);
        }
    }

    /**
     * Adds an element to the dynamicTilePane.
     */
    public void addElementToTilePane() {
        Button button = new Button();
        button.setPrefSize(222, 333);
        button.setStyle("-fx-background-color: #25272D; -fx-background-radius: 4;");
        dynamicTilePane.getChildren().add(button);
    }

    /**
     * Handles the action triggered by the "Add" button.
     */
    public void handleAddButtonAction() {
        addElementToTilePane();
    }

    @FXML
    private void openMoviePlayer(ActionEvent actionEvent) {
    }
}
