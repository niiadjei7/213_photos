package photosfx.controller;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

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
    private VBox albumsContainer;

    private User user;

    public void initialize(User user) {
        this.user = user;
        welcomeLabel.setText("Welcome, " + user.getUsername() + "!");
        listAlbums();
    }

    private void listAlbums() {
        // Clear existing albums
        albumsContainer.getChildren().clear();

        // Retrieve albums for the user
        List<Album> albums = user.albumList();

        // Create UI components for each album
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
    }

    @FXML
    private void createAlbumButtonClicked(ActionEvent event) {
        createAlbumBox.setVisible(true);
    }

    @FXML
    private void saveAlbumButtonClicked(ActionEvent event) {
        String albumName = albumNameField.getText().trim();
        if (!albumName.isEmpty()) {
            // Create album logic here
            showAlert("Album Created", "Album '" + albumName + "' created successfully.");
            // Clear input field
            albumNameField.clear();
            createAlbumBox.setVisible(false);
        } else {
            showAlert("Error", "Please enter a valid album name.");
        }
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
}
