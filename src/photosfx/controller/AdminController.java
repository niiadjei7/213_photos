package photosfx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import photosfx.model.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AdminController {

    @FXML
    private ListView<String> userList;

    @FXML
    private TextField newUsernameField;

    @FXML
    private VBox userListVBox;

    @FXML
    private Button createUserButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button deleteUserButton;

    private Admin admin;

    public void initialize() {
        admin = new Admin();
        admin.loadUsers();
        listUsers();
    }

    private void listUsers() {
        userListVBox.getChildren().clear(); // Clear existing list
        List<User> users = admin.getUsers();
        for (User user : users) {
            Label userLabel = new Label(user.getUsername());
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(this::deleteUserButtonClicked); // Set the event handler for the delete button
            VBox userBox = new VBox(userLabel, deleteButton);
            userListVBox.getChildren().add(userBox); // Add user entry to the list
        }
    }

    /*
     * @FXML
     * private void toggleAddUserTextBox() {
     * newUsernameField.setVisible(!newUsernameField.isVisible());
     * }
     */
    @FXML
    private void addUserButtonClicked() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add User");
        dialog.setHeaderText("Enter Username:");
        dialog.setContentText("Username:");

        // Show the dialog and wait for user input
        Optional<String> result = dialog.showAndWait();

        // Process the result if the user entered a username
        result.ifPresent(username -> {
            // Check if the username is not empty
            if (!username.trim().isEmpty()) {
                // Add the user to the list
                boolean added = admin.createUser(username);
                if (added) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("User added successfully.");
                    successAlert.showAndWait();
                    listUsers(); // Refresh user list
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Username is already taken.");
                    errorAlert.showAndWait();
                }
            } else {
                // Display an error message if the username is empty
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Please enter a valid username.");
                errorAlert.showAndWait();
            }
        });
    }

    @FXML
    private void deleteUserButtonClicked(ActionEvent event) {
        Button button = (Button) event.getSource();
        VBox userBox = (VBox) button.getParent(); // Get the parent VBox containing the user label and delete button
        Label userLabel = (Label) userBox.getChildren().get(0); // Assuming the username label is the first child
        String username = userLabel.getText(); // Get the username from the label
        boolean deleted = admin.deleteUser(username);
        if (deleted) {
            userListVBox.getChildren().remove(userBox); // Remove the user entry from the list
        }
    }

    @FXML
    private void logoutButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) logoutButton.getScene().getWindow(); // Assuming logoutButton is the button that
                                                                       // triggered the action
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle error appropriately
        }
    }
}
