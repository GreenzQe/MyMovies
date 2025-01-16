package dk.easv.mymovies.GUI.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ErrorPopup {

    /**
     * Displays a static modal popup with a message and different alert types.
     *
     * @param alertType The type of the alert (e.g., WARNING, ERROR, INFO).
     * @param message   The message to display.
     */
    public static void showAlert(ShowAlert alertType, String message) {
        // Create an Alert of the specified type
        Alert alert = null;
        switch (alertType) {
            case WARNING:
                alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Warning!");
                break;
            case ERROR:
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("An error occurred");
                break;
            case INFO:
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Information");
                break;
        }

        // Set the content of the alert (message)
        if (alert != null) {
            alert.setContentText(message);

            // Show and wait for the user to close the alert (modal)
            alert.showAndWait();
        }
    }
}
