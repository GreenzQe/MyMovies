package dk.easv.mymovies;

import dk.easv.mymovies.GUI.Controller.MyMovieController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class MyMovieApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(MyMovieApplication.class.getResource("MyMovie-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1660, 1000);

        // Get the controller from the FXMLLoader
        MyMovieController controller = fxmlLoader.getController();
        controller.populateGenres(Arrays.asList("Action", "Comedy", "Drama", "Horror", "Sci-Fi", "Romance"));

        stage.setTitle("MyMovies");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
