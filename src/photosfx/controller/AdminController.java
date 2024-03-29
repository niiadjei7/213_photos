package photosfx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import photosfx.model.*;
import java.util.List;

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
    private Button deleteUserButton;

    private Admin admin;

    public void initialize() {
        admin = new Admin();
        listUsers();
    }

    private void listUsers() {
        // Logic to retrieve and display list of users
        userListVBox.getChildren().clear(); // Clear existing list
        List<User> users = admin.getUsers();
        for (User user : users) {
            Label userLabel = new Label(user.getUsername());
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(event -> deleteUserButtonClicked(user.getUsername()));
            VBox userBox = new VBox(userLabel, deleteButton);
            userListVBox.getChildren().add(userBox);
        }
    }

    @FXML
    private void addUserButtonClicked(String username) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        boolean added = admin.createUser(username); // Add user to UserManager
        if (added) {
            alert.setContentText("User added successfully.");
        } else {
            alert.setContentText("Username is already taken.");
        }
        alert.showAndWait();
        listUsers(); // Refresh user list
    }

    @FXML
    private void deleteUserButtonClicked(String username) {
        boolean deleted = admin.deleteUser(username);
        if (deleted) {
            listUsers(); // Refresh user list
        }
    }
}
