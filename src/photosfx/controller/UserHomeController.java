package photosfx.controller;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import photosfx.model.*;

public class UserHomeController {

    @FXML
    private VBox userHomeVBox;

    @FXML
    private Label welcomeLabel;

    @FXML
    private VBox createAlbumBox;

    @FXML
    private TextField albumNameField;

    @FXML
    private Button deleteAlbumButton;

    @FXML
    private Button createAlbumButton;

    @FXML
    private Button logoutButton;

    @FXML
    private VBox albumsContainer;

    private User user;

    public void initialize(User user) {
        this.user = user;
        if (user == null) {
            System.out.println("user load failed");
        }
        welcomeLabel.setText("Welcome, " + user.getUsername() + "!");
        listAlbums();
    }

    private void listAlbums() {
        // Clear existing albums
        albumsContainer.getChildren().clear();

        // Retrieve albums for the user
        List<Album> albums = user.albumList();

        // Create UI components for each album
        if (albums == null) {
            System.out.println("Albums is null");
            return;
        }
        for (Album album : albums) {
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(event -> deleteAlbum(album));

            Label albumLabel = new Label(album.getAlbumName());
            VBox albumBox = new VBox(albumLabel, deleteButton);
            albumsContainer.getChildren().add(albumBox);
        }
    }

    private void deleteAlbum(Album album) {
        boolean deleted = user.deleteAlbum(album);
        if (deleted) {
            listAlbums(); // Refresh album list
            showAlert("Success", "Album deleted successfully.");
        } else {
            showAlert("Error", "Failed to delete album.");
        }
        listAlbums();
    }

    @FXML
    private void createAlbumButtonClicked(ActionEvent event) {
        createAlbumBox.setVisible(true);
    }

    @FXML
    private void saveAlbumButtonClicked(ActionEvent event) {
        String albumName = albumNameField.getText().trim();
        if (!albumName.isEmpty()) {
            if (user.addAlbum(albumName)) {
                showAlert("Album Created", "Album '" + albumName + "' created successfully.");
            } else {
                showAlert("Error", "Album name already in use");
            }
            albumNameField.clear();
            createAlbumBox.setVisible(false);
        } else {
            showAlert("Error", "Please enter a valid album name.");
        }
        user.saveUser();
        listAlbums();
    }

    @FXML
    private void cancelAlbumButtonClicked(ActionEvent event) {
        albumNameField.clear();
        createAlbumBox.setVisible(false);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
