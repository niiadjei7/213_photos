package photosfx.controller;

import java.io.File;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.*;

import photosfx.model.*;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private Button loginButton;

    private User user;

    @FXML
    private void initialize() {
        // Optional: Initialize any UI components or perform setup here
    }

    @FXML
    private void loginButtonClicked() {
        String username = usernameField.getText();
        if (username.equals("admin")) {
            openAdminView();
        } else {

            user = DataFileManager.loadUser(username);
            if (user == null) {
                return;
            } else {
                openUserHome(user);
            }
        }

    }

    private void openAdminView() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../view/Admin.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin View");
            stage.show();

            // Close the current login window
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openUserHome(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/UserHome.fxml"));
            Parent root = loader.load();
            UserHomeController controller = loader.getController();
            if (user == null) {
                return;
            }
            controller.initialize(user); // Pass username to UserHomeController
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("User Home");
            stage.show();

            // Close the current login window
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
