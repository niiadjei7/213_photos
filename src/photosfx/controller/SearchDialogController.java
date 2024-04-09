package photosfx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

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

    // Getter and setter for dialogStage
    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    // Method to handle search button click
    @FXML
    private void searchButtonClicked() {
        // Implement search functionality here based on selected options
        // Close the dialog after search
        dialogStage.close();
    }

    // Method to handle cancel button click
    @FXML
    private void cancelButtonClicked() {
        // Close the dialog without performing search
        dialogStage.close();
    }
}