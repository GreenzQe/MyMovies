package dk.easv.mymovies;

import dk.easv.mymovies.GUI.Controller.AddEditMovieController;
import dk.easv.mymovies.GUI.Controller.MyMovieController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MyMovieApplication extends Application {
    private String fxmlFile = "MyMovie-view.fxml";

    @Override
    public void start(Stage stage) {
        try {
            URL fxmlUrl = MyMovieApplication.class.getResource(fxmlFile);
            if (fxmlUrl == null) {
                System.err.println("FXML file not found: " + fxmlFile);
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(fxmlLoader.load());

            URL cssUrl = getClass().getResource("/styles.css");
            if (cssUrl == null) {
                System.err.println("CSS file not found: /styles.css");
                return;
            }
            scene.getStylesheets().add(cssUrl.toExternalForm());

            stage.setTitle("MyMovies");
            Image icon = new Image(getClass().getResourceAsStream("/Images/MyMovies.png"));
            stage.getIcons().add(icon);

            stage.setScene(scene);

            // Set the window to maximized
            stage.setMaximized(true);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
