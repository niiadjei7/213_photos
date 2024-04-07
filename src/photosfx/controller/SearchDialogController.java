package photosfx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.time.LocalDate;

public class SearchDialogController {

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<String> tagTypeComboBox;

    @FXML
    private ComboBox<String> tagValueComboBox;

    private Stage dialogStage;
    private boolean isSearchClicked = false;

    // Method to initialize the controller
    public void initialize() {
        // Initialize tag type combo box with sample data
        ObservableList<String> tagTypes = FXCollections.observableArrayList("Location", "Person", "Event");
        tagTypeComboBox.setItems(tagTypes);
    }

    // Set the dialog stage
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    // Return true if the search button is clicked, otherwise false
    public boolean isSearchClicked() {
        return isSearchClicked;
    }

    // Return the selected start date
    public LocalDate getStartDate() {
        return startDatePicker.getValue();
    }

    // Return the selected end date
    public LocalDate getEndDate() {
        return endDatePicker.getValue();
    }

    // Return the selected tag type
    public String getTagType() {
        return tagTypeComboBox.getValue();
    }

    // Return the selected tag value
    public String getTagValue() {
        return tagValueComboBox.getValue();
    }

    // Called when the user clicks on the search button
    @FXML
    private void searchPhotos() {
        isSearchClicked = true;
        dialogStage.close();
    }

    // Called when the user clicks on the cancel button
    @FXML
    private void closeDialog() {
        dialogStage.close();
    }

    // Method to populate tag values based on the selected tag type
    @FXML
    private void populateTagValues() {
        // Sample implementation, you'll need to populate based on selected tag type
        String selectedTagType = tagTypeComboBox.getValue();
        ObservableList<String> tagValues = FXCollections.observableArrayList();
        // Populate tag values based on selected tag type
        // Add code to fetch tag values from the model
        tagValueComboBox.setItems(tagValues);
    }
}