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
import javafx.stage.*;
import photosfx.model.*;

public class UserHomeController {

    @FXML
    private VBox userHomeVBox;

    @FXML
    private Label welcomeLabel;

    @FXML
    private VBox createAlbumBox;

    @FXML
    private Button searchButton;

    @FXML
    private TextField albumNameField;

    @FXML
    private Button deleteAlbumButton;

    @FXML
    private Button createAlbumButton;

    @FXML
    private Button logoutButton;
    @FXML
    private Button renameButton;

    @FXML
    private Button openAlbumButton;

    @FXML
    private VBox albumsContainer;

    private User user;

    public void initialize(User user) {
        this.user = user;
        if (user == null) {
            showAlert("Error", "User not found");
            ;
        }
        welcomeLabel.setText("Welcome, " + user.getUsername() + "!");
        listAlbums();
    }

    public void updateData() {
        DataFileManager.saveUser(user);
    }

    private void listAlbums() {
        // Clear existing albums
        albumsContainer.getChildren().clear();
        String numPhotos = "";
        // Retrieve albums for the user
        List<Album> albums = user.albumList();

        // Create UI components for each album
        if (albums == null) {
            return;
        }
        for (Album album : albums) {
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(event -> deleteAlbum(album));

            Button renameButton = new Button("Rename");
            renameButton.setOnAction(event -> renameAlbum(album));

            Button openAlbumButton = new Button("Open");
            openAlbumButton.setOnAction(event -> openAlbum(album, openAlbumButton)); // Pass openAlbumButton

            numPhotos = album.getPhotos().size() + " photos " + album.getDateRange();

            Label albumLabel = new Label(album.getAlbumName());
            Label numPhotoLabel = new Label(numPhotos);

            VBox albumBox = new VBox(albumLabel, numPhotoLabel, openAlbumButton, deleteButton, renameButton);
            albumsContainer.getChildren().add(albumBox);
        }
    }

    private void openAlbum(Album album, Button openAlbumButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AlbumView.fxml"));
            Parent root = loader.load();
            AlbumController controller = loader.getController();
            if (album == null) {
                showAlert("Error", "Album not found");
            }
            controller.initialize(album, user); // Pass username to UserHomeController
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(album.getAlbumName());
            stage.show();

            // Close the current login window
            Stage currentStage = (Stage) openAlbumButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteAlbum(Album album) {
        boolean deleted = user.deleteAlbum(album, user.getUsername());
        if (deleted) {
            listAlbums(); // Refresh album list
            showAlert("Success", "Album deleted successfully.");
        } else {
            showAlert("Error", "Failed to delete album.");
        }
        listAlbums();
    }

    private void renameAlbum(Album album) {
        TextInputDialog dialog = new TextInputDialog(album.getAlbumName());
        dialog.setTitle("Rename Album");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter new name for the album:");

        // Get the response value
        dialog.showAndWait().ifPresent(newName -> {
            if (!newName.isEmpty()) {
                user.renameAlbum(album.getAlbumName(), newName);
                listAlbums(); // Refresh album list
                showAlert("Success", "Album renamed successfully.");
            } else {
                showAlert("Error", "Please enter a valid album name.");
            }
        });
        updateData();
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
            Album album = new Album(albumName);
            if (user.addAlbum(album)) {
                showAlert("Album Created", "Album '" + albumName + "' created successfully.");
                DataFileManager.saveAlbum(album, user.getUsername());
            } else {
                showAlert("Error", "Album name already in use");
            }
            albumNameField.clear();
            createAlbumBox.setVisible(false);
        } else {
            showAlert("Error", "Please enter a valid album name.");
        }
        // updateData();
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
            DataFileManager.saveUser(this.user);
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

    @FXML
    private void searchButtonClicked(ActionEvent event) {
        try {
            // Load the SearchDialog.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/SearchDialog.fxml"));
            Parent root = loader.load();

            // Create a new stage for the search dialog
            Stage searchStage = new Stage();
            searchStage.initModality(Modality.WINDOW_MODAL);
            searchStage.initOwner(((Stage) userHomeVBox.getScene().getWindow())); // Set the owner window
            searchStage.setTitle("Search Photos");
            searchStage.setScene(new Scene(root));

            // Show the search dialog
            searchStage.showAndWait(); // Wait for the search dialog to be closed before continuing

        } catch (IOException e) {
            e.printStackTrace(); // Handle error appropriately
        }
    }

}
