package photosfx.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * The MainController class controls the main view of the application.
 * It handles initialization tasks and quitting the application.
 */
public class MainController {

    /**
     * Initializes the main view of the application.
     * This method is automatically called when the FXML file is loaded.
     * It can be used to initialize any necessary components or perform startup tasks.
     */
    @FXML
    private void initialize() {
        // Initialize any necessary components or perform startup tasks
    }

    /**
     * Handles the action of quitting the application.
     * Displays a confirmation dialog to confirm the action.
     * If the user confirms, the application is exited.
     */
    @FXML
    private void handleQuitApplication() {
        // Handle quitting the application
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit Application");
        alert.setHeaderText("Are you sure you want to quit?");
        alert.setContentText("Any unsaved changes will be lost.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Platform.exit();
            }
        });
    }
}
