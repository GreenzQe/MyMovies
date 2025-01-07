package dk.easv.mymovies.GUI.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class MoviePlayerController {

    @FXML
    private MediaView mediaView;

    private MediaPlayer mediaPlayer;

    @FXML
    private Button fullScreenButton;

    private Stage stage;
    @FXML
    private Button playPauseButton;

    @FXML
    private Slider volumeSlider;

    public void initialize() {
        // Add a listener to the volume slider to change the media player's volume
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newValue.doubleValue());
            }
        });    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleFullScreenButtonAction() {
        if (stage != null) {
            stage.setFullScreen(!stage.isFullScreen());
        }
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

        volumeSlider.setValue(mediaPlayer.getVolume());

        // Play the video
        mediaPlayer.play();
        playPauseButton.setText("Pause");
    }




    // Add a method to handle the window close event
    public void stopVideoOnClose() {
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // Stop the video
            mediaPlayer.dispose(); // Release resources
        }
    }

    @FXML
    private void handlePlayPauseButtonAction(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                playPauseButton.setText("Play");
            } else {
                mediaPlayer.play();
                playPauseButton.setText("Pause");
            }
        }
    }
}
