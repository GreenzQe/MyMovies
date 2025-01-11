package dk.easv.mymovies.GUI.Controller;

import dk.easv.mymovies.BE.Movie;

import dk.easv.mymovies.GUI.Model.MovieModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.awt.Desktop;


import java.util.List;

public class MyMovieController {

    @FXML
    public Button btnHome;

    @FXML
    public ListView<Movie> lstMovies;

    @FXML
    public TextField txtSearch;

    @FXML
    private TilePane genreTilePane;

    @FXML
    private TilePane dynamicTilePane;

    @FXML
    private ScrollPane scrollPane;

    private Stage moviePlayerStage; // Keep track of the Movie Player Stage
    @FXML
    private Button btnAddMovie;

    private MovieModel movieModel;

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

        try {
            movieModel = new MovieModel();
            ObservableList<Movie> movies = movieModel.getMovies();
            System.out.println("Number of movies: " + movies.size()); // Debug statement
            lstMovies.setItems(movieModel.getMovies());
            addMoviesToTilePane();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addMoviesToTilePane() throws Exception {
        dynamicTilePane.getChildren().clear();
        ObservableList<Movie> movies = movieModel.getMovies();
        System.out.println("Adding movies to TilePane: " + movies.size()); // Debug statement
        for (Movie movie : movieModel.getMovies()) {
            addMovieToTilePane(movie);
        }
    }

    private void addMovieToTilePane(Movie movie) {
        System.out.println("Adding movie: " + movie.getName()); // Debug statement
        ImageView imageView = new ImageView();
        VBox vbox = new VBox();
        try {
            String posterLink = movie.getPosterLink();
            System.out.println("Poster link: " + posterLink); // Debug statement
            // Ensure the path is correctly formatted for local files
            if (posterLink.startsWith("C:") || posterLink.startsWith("/")) {
                posterLink = "file:/" + posterLink.replace("\\", "/");
            }
            File posterFile = new File(posterLink.replace("file:/", ""));
            if (posterFile.exists()) {
                Image image = new Image(posterLink, true);
                imageView.setImage(image);
            } else {
                throw new IOException("File not found");
            }
            imageView.setFitWidth(222);
            imageView.setFitHeight(278);
            imageView.setPreserveRatio(true);
            vbox.getChildren().add(imageView);
        } catch (Exception e) {
            System.out.println("Failed to load image for movie: " + movie.getName()); // Debug statement
            Image fallbackImage = new Image(getClass().getResource("/Images/Fallback.png").toExternalForm(), true);
            imageView.setImage(fallbackImage);
            imageView.setFitWidth(222);
            imageView.setFitHeight(278);
            imageView.setPreserveRatio(true);
            vbox.getChildren().add(imageView);
        }

        // Add hover effect with movie description
        Tooltip tooltip = new Tooltip(movie.getName() + "\n" + movie.getiRating());
        Tooltip.install(imageView, tooltip);

        vbox.setPrefSize(222, 278); // Set the exact required dimensions
        dynamicTilePane.getChildren().add(vbox);
        System.out.println("Movie added to TilePane: " + movie.getName()); // Debug statement
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
        button.setPrefSize(222, 278);
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
            stage.setScene(new Scene(parent, 476, 391));
            stage.setResizable(false); // Disable resizing
            stage.initModality(Modality.APPLICATION_MODAL); // Make it a modal window
            stage.showAndWait(); // Show the window and wait for it to be closed
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //addElementToTilePane();
        try {
            lstMovies.setItems(movieModel.getMovies());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    @FXML
    public void handleSearchKeyReleased(KeyEvent keyEvent) throws Exception {
        String searchText = txtSearch.getText().toLowerCase();
        ObservableList<Movie> filteredMovies = movieModel.searchMovie(searchText);
        lstMovies.setItems(filteredMovies);
        addMoviesToTilePane();
    }
}
