package dk.easv.mymovies;

import dk.easv.mymovies.GUI.Controller.MyMovieController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
            Scene scene = new Scene(fxmlLoader.load(), 1660, 1000);

            URL cssUrl = getClass().getResource("/styles.css");
            if (cssUrl == null) {
                System.err.println("CSS file not found: /styles.css");
                return;
            }
            scene.getStylesheets().add(cssUrl.toExternalForm());

            MyMovieController controller = fxmlLoader.getController();
            controller.populateGenres(Arrays.asList("Action", "Comedy", "Family", "History", "Mystery", "Sci-Fi",
                    "War", "Adventure", "Crime", "Fantasy", "Horror", "News",
                    "Short", "Western", "Sport", "Animation", "Documentary",
                    "Film-Noir", "Music", "Reality-TV", "Talk-Show", "Biography",
                    "Drama", "Game-Show", "Musical", "Romance", "Thriller"
            ));

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