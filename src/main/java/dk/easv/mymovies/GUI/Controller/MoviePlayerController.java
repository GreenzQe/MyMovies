package dk.easv.mymovies.GUI.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.WindowEvent;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class MoviePlayerController {

    @FXML
    private MediaView mediaView;

    private MediaPlayer mediaPlayer;

    public void initialize() {
        // Placeholder for any initialization logic, if needed
    }

    public void playVideo(String filePath) {
        // Stop and dispose of any existing mediaPlayer
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        // Load the video file using the URI
        Media media = new Media(filePath);
        mediaPlayer = new MediaPlayer(media);

        // Bind the MediaPlayer to the MediaView
        mediaView.setMediaPlayer(mediaPlayer);

        // Use Platform.runLater to ensure the scene is set
        Platform.runLater(() -> {
            // Check if the MediaView's scene is already initialized
            if (mediaView.getScene() != null) {
                mediaView.fitWidthProperty().bind(mediaView.getScene().widthProperty());
                mediaView.fitHeightProperty().bind(mediaView.getScene().heightProperty());
            }
        });

        // Play the video
        mediaPlayer.play();
    }




    // Add a method to handle the window close event
    public void stopVideoOnClose() {
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // Stop the video
            mediaPlayer.dispose(); // Release resources
        }
    }
}
