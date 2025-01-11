package dk.easv.mymovies;

import dk.easv.mymovies.GUI.Controller.MyMovieController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public class MyMovieApplication extends Application {

    @Override
    public void start(Stage stage) {
        try {
            URL fxmlUrl = MyMovieApplication.class.getResource("MyMovie-view.fxml");
            if (fxmlUrl == null) {
                System.err.println("FXML file not found: MyMovie-view.fxml");
                return;
            }
            System.out.println("Loading FXML from: " + fxmlUrl);

            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(fxmlLoader.load());

            URL cssUrl = getClass().getResource("/styles.css");
            if (cssUrl == null) {
                System.err.println("CSS file not found: /styles.css");
                return;
            }
            scene.getStylesheets().add(cssUrl.toExternalForm());

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(screenBounds.getMinX());
            stage.setY(screenBounds.getMinY());
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());

            stage.setTitle("MyMovies");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}