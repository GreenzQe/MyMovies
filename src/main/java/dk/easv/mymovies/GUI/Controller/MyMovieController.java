package dk.easv.mymovies.GUI.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MyMovieController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}