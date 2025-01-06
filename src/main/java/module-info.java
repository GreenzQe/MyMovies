module dk.easv.mymovies {
    requires javafx.controls;
    requires javafx.fxml;


    opens dk.easv.mymovies to javafx.fxml;
    exports dk.easv.mymovies;
}