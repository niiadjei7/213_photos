package photosfx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

/**
 * Controller class for the search dialog window.
 * Handles user interactions and search functionality.
 */
public class SearchDialogController {

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private CheckBox locationCheckBox;

    @FXML
    private CheckBox personCheckBox;

    private Stage dialogStage;

    /**
     * Gets the dialog stage.
     *
     * @return The dialog stage.
     */
    public Stage getDialogStage() {
        return dialogStage;
    }

    /**
     * Sets the dialog stage.
     *
     * @param dialogStage The dialog stage to set.
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Handles the action of clicking the search button.
     * Performs the search based on the selected options and closes the dialog.
     */
    @FXML
    private void searchButtonClicked() {
        // Implement search functionality here based on selected options
        // Close the dialog after search
        dialogStage.close();
    }

    /**
     * Handles the action of clicking the cancel button.
     * Closes the dialog without performing the search.
     */
    @FXML
    private void cancelButtonClicked() {
        // Close the dialog without performing search
        dialogStage.close();
    }
}
