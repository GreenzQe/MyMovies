module dk.easv.mymovieeee {
    requires javafx.controls;
    requires javafx.fxml;


    opens dk.easv.mymovieeee to javafx.fxml;
    exports dk.easv.mymovieeee;
}