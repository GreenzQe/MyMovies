package dk.easv.mymovies.GUI.Controller;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;


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

    private PauseTransition inactivityTimer;

    public void initialize() {
        // Add a listener to the volume slider to change the media player's volume
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newValue.doubleValue());
            }
        });

    // Set up the inactivity timer
    inactivityTimer = new PauseTransition(Duration.seconds(5));
    inactivityTimer.setOnFinished(event -> hideControls());

    // Add event handlers to detect activity
        Platform.runLater(() -> {
        mediaView.getScene().addEventFilter(MouseEvent.MOUSE_MOVED, event -> resetInactivityTimer());
        mediaView.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> resetInactivityTimer());
        mediaView.getScene().addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> resetInactivityTimer());
    });

    // Start the inactivity timer
    resetInactivityTimer();
    }

    private void resetInactivityTimer() {
        showControls();
        inactivityTimer.playFromStart();
    }

    private void hideControls() {
        fullScreenButton.setVisible(false);
        playPauseButton.setVisible(false);
        volumeSlider.setVisible(false);
    }

    private void showControls() {
        fullScreenButton.setVisible(true);
        playPauseButton.setVisible(true);
        volumeSlider.setVisible(true);
    }

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
