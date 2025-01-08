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
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.awt.Desktop;


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
    @FXML
    private Button btnAddMovie;

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
        try {
            // Load the FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dk/easv/mymovies/AddEditMovie-view.fxml"));
            Parent parent = fxmlLoader.load();

            // Create a new stage for the Add/Edit Movie view
            Stage stage = new Stage();
            stage.setTitle("Add/Edit Movie");
            stage.setScene(new Scene(parent, 316, 376));
            stage.setResizable(false); // Disable resizing
            stage.initModality(Modality.APPLICATION_MODAL); // Make it a modal window
            stage.showAndWait(); // Show the window and wait for it to be closed

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addElementToTilePane();
    }


    @FXML
    private void openMoviePlayer(ActionEvent actionEvent) {
        try {
            File movieFile = new File("src/main/resources/Movies/TestMovie.mp4");
            if (movieFile.exists()) {
                Desktop.getDesktop().open(movieFile);
            } else {
                System.out.println("File does not exist.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
