module dk.easv.mymovies {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.naming;
    requires com.microsoft.sqlserver.jdbc;
    requires java.desktop;
    requires javafx.media;


    opens dk.easv.mymovies to javafx.fxml;
    exports dk.easv.mymovies;
    exports dk.easv.mymovies.GUI;
    opens dk.easv.mymovies.GUI to javafx.fxml;
    exports dk.easv.mymovies.GUI.Controller;
    opens dk.easv.mymovies.GUI.Controller to javafx.fxml;
}