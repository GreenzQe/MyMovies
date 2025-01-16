package dk.easv.mymovies.GUI.Controller;

import dk.easv.mymovies.BE.Category;
import dk.easv.mymovies.BE.Movie;

import dk.easv.mymovies.GUI.Model.CategoryModel;
import dk.easv.mymovies.GUI.Model.MovieModel;
import dk.easv.mymovies.GUI.utils.ErrorPopup;
import dk.easv.mymovies.GUI.utils.ShowAlert;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.awt.Desktop;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MyMovieController {

    @FXML
    public Button btnHome;

    @FXML
    public ListView<Movie> lstMovies;

    @FXML
    public TextField txtSearch;

    @FXML
    public ImageView imgCategory;

    @FXML
    public ComboBox cbbSort;

    @FXML
    public Label lblTotal;

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
    private CategoryModel categoryModel;

    private List<String> selectedGenres = new ArrayList<>();

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

        // Add sorting options to the ComboBox
        cbbSort.getItems().addAll("Date Added to Collection", "IMDb Rating", "Last Seen", "Alphabetical");
        cbbSort.setPrefWidth(ComboBox.USE_COMPUTED_SIZE);
        cbbSort.setValue("Date Added to Collection");

        try {
            movieModel = new MovieModel();
            categoryModel = new CategoryModel();
            ObservableList<Movie> movies = movieModel.getMovies();
            System.out.println("Number of movies: " + movies.size()); // Debug statement
            lstMovies.setItems(movies);
            addMoviesToTilePane(movies);
            populateGenres();
        } catch (Exception e) {
            ErrorPopup.showAlert(ShowAlert.ERROR, e.getMessage());
        }

        // Set custom cell factory to display only the movie name
        lstMovies.setCellFactory(param -> new ListCell<Movie>() {
            @Override
            protected void updateItem(Movie movie, boolean empty) {
                super.updateItem(movie, empty);
                if (empty || movie == null) {
                    setText(null);
                } else {
                    setText(movie.getName());
                }
            }
        });

        // Add a listener to handle sorting when a new option is selected
        cbbSort.setOnAction(event -> {
            try {
                sortMovies();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // Create a Tooltip with examples and filter usage instructions
        Tooltip searchTooltip = new Tooltip();
        searchTooltip.setText("Search Examples:\n" +
                "imdb:7-9 -> IMDb rating between 7 and 9\n" +
                "imdb:6+ -> IMDb rating 6 and above\n" +
                "imdb:-7 -> IMDb rating 7 and below\n" +
                "pr:8-10 -> Personal rating between 8 and 10\n" +
                "pr:5+ -> Personal rating 5 and above\n" +
                "pr:-6 -> Personal rating 6 and below\n");
        txtSearch.setTooltip(searchTooltip);

        // Add double-click event handler to play the movie
        lstMovies.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Movie selectedMovie = lstMovies.getSelectionModel().getSelectedItem();
                if (selectedMovie != null) {
                    openMoviePlayer(selectedMovie.getFileLink());
                }
            }
        });

        genreUpdateListener();
        checkRecommendedToRemove();

    }

    private void checkRecommendedToRemove() {
        List<Movie> movies = new ArrayList<>();
        try {
            movies = movieModel.getRecommendedToRemove();
        } catch (Exception e) {
            ErrorPopup.showAlert(ShowAlert.ERROR, e.getMessage());
        }

        if (movies.isEmpty())
            return;

        StringBuilder msg = new StringBuilder();
        msg.append("We have found movies with lower rating than 6 and older than 2 years.\nWe recommend to remove them.\n\nHere's the list:\n");

        for (Movie movie : movies)
            msg.append("• ").append(movie.getName()).append("\n");

        ErrorPopup.showAlert(ShowAlert.INFO, msg.toString());
    }

    private void genreUpdateListener() {
        movieModel.updatedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                populateGenres();
                try {
                    addMoviesToTilePane(movieModel.getMovies());
                } catch (Exception e) {
                    //TODO: hndter
                    ErrorPopup.showAlert(ShowAlert.ERROR, e.getMessage());
                }
            }
            movieModel.updatedProperty().setValue(false);
        });
    }

    private void sortMovies() throws Exception {
        String selectedOption = (String) cbbSort.getValue();
        ObservableList<Movie> movies = movieModel.getMovies();

        switch (selectedOption) {
            case "Date Added to Collection":
                movies.sort(Comparator.comparingInt(Movie::getId));
                break;
            case "IMDb Rating":
                movies.sort((m1, m2) -> Double.compare(m2.getiRating(), m1.getiRating()));
                break;
            case "Last Seen":
                movies.sort((m1, m2) -> m2.getLastView().compareTo(m1.getLastView()));
                break;
            case "Alphabetical":
                movies.sort((m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName()));
                break;
        }

        // Update the TilePane with the sorted movies
        addMoviesToTilePane(movies);
    }

    private void addMoviesToTilePane(ObservableList<Movie> movies) {
        dynamicTilePane.getChildren().clear();
        System.out.println("Adding movies to TilePane: " + movies.size()); // Debug statement
        for (Movie movie : movies) {
            System.out.println("Movie: " + movie.getName());
            addMovieToTilePane(movie);
        }
        lblTotal.setText("(" + movies.size() + ")");
    }

    private void addMovieToTilePane(Movie movie) {
        System.out.println("Adding movie: " + movie.getName()); // Debug statement
        ImageView imageView = new ImageView();
        VBox vbox = new VBox();

        String posterLink = movie.getPosterLink();
        System.out.println("Poster link: " + posterLink); // Debug statement
        // Ensure the path is correctly formatted for local files
        if (posterLink.startsWith("C:") || posterLink.startsWith("/")) {
            posterLink = "file:/" + posterLink.replace("\\", "/");
        }
        File posterFile = new File(posterLink.replace("file:/", ""));
        System.out.println("Poster file path: " + posterFile.getAbsolutePath()); // Debug statement
        if (posterFile.exists()) {
            String f  =posterFile.toURI().toString();
            System.out.println(f);
            Image image = new Image(f, true);
            imageView.setImage(image);
        } else {
            System.out.println("Failed to load image for movie: " + movie.getName()); // Debug statement
            Image fallbackImage = new Image(getClass().getResource("/Images/Fallback.png").toExternalForm(), true);
            imageView.setImage(fallbackImage);
            //throw new IOException("File not found: " + posterFile.getAbsolutePath());
        }
        imageView.setFitWidth(222);
        imageView.setFitHeight(278);
        imageView.setPreserveRatio(true);
        vbox.getChildren().add(imageView);

        // Create a Pane for the hover effect
        VBox hoverPane = new VBox();
        hoverPane.setStyle("-fx-background-color: rgba(37,39,45, 0.8); -fx-padding: 10; -fx-border-color: black; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4; ");
        hoverPane.setPrefWidth(222); // Set the same width as the image

        Label nameLabel = new Label(movie.getName());
        nameLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 24px;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(200);

        Label categoryLabel = new Label(movie.getCategories().toString());
        categoryLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16px;");
        categoryLabel.setWrapText(true);
        categoryLabel.setMaxWidth(200);

        HBox iRatingBox = new HBox();
        ImageView imdbLogo = new ImageView(new Image(getClass().getResource("/Images/imdb_logo.png").toExternalForm()));
        imdbLogo.setFitHeight(32);
        imdbLogo.setFitWidth(64);
        Label iRatingLabel = new Label(" " + movie.getiRating());
        iRatingLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 24px;");
        iRatingBox.getChildren().addAll(imdbLogo, iRatingLabel);

        HBox pRatingBox = new HBox();
        ImageView pRatingLogo = new ImageView(new Image(getClass().getResource("/Images/prating_logo.png").toExternalForm()));
        pRatingLogo.setFitHeight(32);
        pRatingLogo.setFitWidth(64);
        Label pRatingLabel = new Label(movie.getpRating() != 0.0f ? " " + movie.getpRating() : "Not Rated");
        pRatingLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 24px;");
        pRatingBox.getChildren().addAll(pRatingLogo, pRatingLabel);

        Label lastViewLabel = new Label("Last Seen:\n" + movie.getLastView());
        lastViewLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 24px;");
        lastViewLabel.setWrapText(true);
        lastViewLabel.setMaxWidth(200);

        hoverPane.getChildren().addAll(nameLabel, categoryLabel, iRatingBox, pRatingBox, lastViewLabel);

        // Create a Popup to show the hover pane
        Popup popup = new Popup();
        popup.getContent().add(hoverPane);

        // Show the hover pane on mouse hover
        imageView.setOnMouseEntered(event -> {
            double screenWidth = Screen.getPrimary().getBounds().getWidth();
            double imageViewRightX = imageView.localToScreen(imageView.getBoundsInLocal()).getMaxX();
            double hoverPaneWidth = hoverPane.getPrefWidth();
            double xPosition = imageViewRightX + 12;
            double yPosition = imageView.localToScreen(imageView.getBoundsInLocal()).getMinY();

            if (xPosition + hoverPaneWidth > screenWidth) {
                xPosition = imageView.localToScreen(imageView.getBoundsInLocal()).getMinX() - hoverPaneWidth - 12;
            }

            popup.show(imageView, xPosition, yPosition);
        });

        imageView.setOnMouseExited(event -> popup.hide());

        // Set onMouseClicked event to play the movie on left click and open AddEditMovie on right click
        imageView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                openMoviePlayer(movie.getFileLink());
            } else if (event.getButton() == MouseButton.SECONDARY) {
                openAddEditMovie(movie);
            }
        });

        dynamicTilePane.getChildren().add(vbox);
        System.out.println("Movie added to TilePane: " + movie.getName()); // Debug statement
    }

    @FXML
    private void openAddEditMovie(Movie movie) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dk/easv/mymovies/AddEditMovie-view.fxml"));
            Parent parent = fxmlLoader.load();

            // Pass the movie to the AddEditMovieController
            AddEditMovieController controller = fxmlLoader.getController();
            controller.setMovie(movie);
            controller.updateController(this.movieModel, this.categoryModel);

            Stage stage = new Stage();
            stage.setTitle("Edit Movie");
            stage.setScene(new Scene(parent, 476, 391));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            ErrorPopup.showAlert(ShowAlert.ERROR, "Couldn't create view to add/edit movie");
        }
    }

    @FXML
    private void openMoviePlayer(String fileLink) {
        try {
            File movieFile = new File(fileLink);
            if (movieFile.exists()) {
                Desktop.getDesktop().open(movieFile);
            } else {
                ErrorPopup.showAlert(ShowAlert.ERROR, "The file does not exist");
            }
        } catch (IOException e) {
            ErrorPopup.showAlert(ShowAlert.ERROR, "An unexpected error occured");
        }
    }

    private void populateGenres() {
        try {
            // Fetch categories from the database using CategoryModel
            ObservableList<Category> categories = categoryModel.getCategories();

            // Clear the TilePane to avoid duplicates
            genreTilePane.getChildren().clear();

            for (Category category : categories) {
                HBox genreBox = createGenreCheckbox(category);

                // Add the HBox to the TilePane
                genreTilePane.getChildren().add(genreBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HBox createGenreCheckbox(Category category) {
        HBox genreBox = new HBox();
        genreBox.setPrefSize(180, 34); // Set preferred size
        genreBox.setStyle("-fx-background-color: #25272D; -fx-background-radius: 4;");

        // Create a CheckBox with the category name
        CheckBox checkBox = new CheckBox(category.getName());
        checkBox.setTextFill(Color.WHITE); // Set text color to white
        checkBox.setPrefSize(180, 34); // Match the size of the HBox
        checkBox.setPadding(new Insets(0, 0, 0, 8)); // Add left padding

        checkBox.setStyle("-fx-cursor: hand;");

        checkBox.setOnMouseEntered(event ->
                checkBox.setStyle("-fx-text-fill: grey; -fx-cursor: hand;")
        );
        checkBox.setOnMouseExited(event -> {
                    checkBox.setStyle("-fx-text-fill: white;");
                    if (checkBox.isSelected())
                        checkBox.setStyle("-fx-text-fill: grey; -fx-cursor: hand;");
                }
        );

        listenToGenreCheckbox(checkBox);

        // Add the CheckBox to the HBox
        genreBox.getChildren().add(checkBox);

        return genreBox;
    }

    private void listenToGenreCheckbox(CheckBox checkbox) {
        checkbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updateMovieViewGenre(checkbox.getText(), newValue);
        });
    }

    private void updateMovieViewGenre(String genre, boolean on) {
        if (on) {
            selectedGenres.add(genre);
        } else {
            selectedGenres.remove(genre);
        }

        try {
            List<Movie> allMovies = movieModel.getMovies();
            List<Movie> filteredMovies = new ArrayList<>();

            for (Movie movie : allMovies) {
                boolean matchesAllGenres = true;
                for (String selectedGenre : selectedGenres) {
                    if (!movie.getCategories().containsKey(selectedGenre)) {
                        matchesAllGenres = false;
                        break;
                    }
                }
                if (matchesAllGenres) {
                    filteredMovies.add(movie);
                }
            }

            ObservableList<Movie> observableFilteredMovies = javafx.collections.FXCollections.observableArrayList(filteredMovies);
            lstMovies.setItems(observableFilteredMovies);
            addMoviesToTilePane(observableFilteredMovies);
        } catch (Exception e) {
            ErrorPopup.showAlert(ShowAlert.ERROR, "Couldn't update movie genre view");
        }
    }

    /**
     * Handles the action triggered by the "Add" button.
     */
    public void handleAddButtonAction() throws Exception {
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
            ErrorPopup.showAlert(ShowAlert.ERROR, "Couldn't create view to add movie");
        }

        //addElementToTilePane(); Old version of addMoviesToTilePane?
        addMoviesToTilePane(movieModel.getMovies());
        populateGenres();
        try {
            lstMovies.setItems(movieModel.getMovies());
        } catch (Exception e) {
            ErrorPopup.showAlert(ShowAlert.ERROR, e.getMessage());
        }
    }

    @FXML
    public void handleSearchKeyReleased(KeyEvent keyEvent) throws Exception {
        String searchText = txtSearch.getText().toLowerCase();
        ObservableList<Movie> filteredMovies;

        if (searchText.startsWith("imdb:") || searchText.startsWith("pr:")) {
            boolean isImdb = searchText.startsWith("imdb:");
            String ratingText = searchText.substring(isImdb ? 5 : 3);
            filteredMovies = filterByRating(ratingText, isImdb);
        } else {
            filteredMovies = movieModel.searchMovie(searchText);
        }

        lstMovies.setItems(filteredMovies);
        addMoviesToTilePane(filteredMovies);
        lblTotal.setText("(" + filteredMovies.size() + ")");
    }

    private ObservableList<Movie> filterByRating(String ratingText, boolean isImdb) throws Exception {
        try {
            double minRating, maxRating = Double.MAX_VALUE;
            if (ratingText.contains("-") && !ratingText.startsWith("-")) {
                String[] parts = ratingText.split("-");
                minRating = Double.parseDouble(parts[0]);
                maxRating = Double.parseDouble(parts[1]);
            } else if (ratingText.endsWith("+")) {
                minRating = Double.parseDouble(ratingText.substring(0, ratingText.length() - 1));
            } else if (ratingText.startsWith("-")) {
                minRating = 0;
                maxRating = Double.parseDouble(ratingText.substring(1));
            } else {
                minRating = maxRating = Double.parseDouble(ratingText);
            }

            double finalMaxRating = maxRating;
            return movieModel.getMovies().filtered(movie -> {
                double rating = isImdb ? movie.getiRating() : movie.getpRating();
                return rating >= minRating && rating <= finalMaxRating;
            });
        } catch (Exception e) {
            return movieModel.getMovies(); // If parsing fails, show all movies
        }
    }

    @FXML
    public void handleHomeButtonAction(ActionEvent actionEvent) {
        // Clear the search bar
        txtSearch.clear();

        // Set the sort ComboBox to "Date Added to Collection"
        cbbSort.setValue("Date Added to Collection");

        // Unselect all categories
        genreTilePane.getChildren().forEach(node -> {
            if (node instanceof HBox) {
                HBox hbox = (HBox) node;
                hbox.getChildren().forEach(child -> {
                    if (child instanceof CheckBox) {
                        ((CheckBox) child).setSelected(false);
                    }
                });
            }
        });

        populateGenres();
        try {
            addMoviesToTilePane(movieModel.getMovies());
        } catch (Exception e) {
            ErrorPopup.showAlert(ShowAlert.ERROR, e.getMessage());
        }

        // Refresh the movie list
        try {
            ObservableList<Movie> movies = movieModel.getMovies();
            lstMovies.setItems(movies);
            addMoviesToTilePane(movies);
            lblTotal.setText("(" + movies.size() + ")");
        } catch (Exception e) {
            ErrorPopup.showAlert(ShowAlert.ERROR, e.getMessage());
        }
    }
}
