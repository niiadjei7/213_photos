package photosfx.controller;

import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import photosfx.model.*;

public class AlbumController {

    @FXML
    private VBox AlbumVBox;

    @FXML
    private VBox addPhotoBox;

    @FXML
    private TextField photoNameField;

    @FXML
    private Button deletePhotoButton;

    @FXML
    private Button addPhotoButton;

    @FXML
    private Button recaptionButton;

    @FXML
    private VBox photosContainer;

    private Album album;

    public void initialize(Album album) {
        this.album = album;
        if (album == null) {
            System.out.println("album load failed");
        }
        showPhotos();
    }

    private void showPhotos() {
        photosContainer.getChildren().clear();
        String caption = "";
        List<Photo> photos = album.getPhotos();

        if (photos == null) {
            System.out.println("Photos is null");
            return;
        }
        for (Photo photo : photos) {
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(event -> deletePhoto(photo));

            Button renameButton = new Button("Rename");
            recaptionButton.setOnAction(event -> recaptionPhoto(photo));

            Label photoLabel = new Label(photo.getCaption());
            Label tagsLabel = new Label(photo.getTags());

            VBox photoBox = new VBox(photoLabel, tagsLabel, deleteButton, renameButton);
            photosContainer.getChildren().add(photoBox);
        }
    }

    private void deletePhoto(Photo photo) {
        boolean deleted = album.deletePhoto(photo);
        if (deleted) {
            showPhotos(); // Refresh album list
            showAlert("Success", "Album deleted successfully.");
        } else {
            showAlert("Error", "Failed to delete album.");
        }
        showPhotos();
        ;
    }

    private void recaptionPhoto(Photo photo) {
        TextInputDialog dialog = new TextInputDialog(photo.getCaption());
        dialog.setTitle("Rename Album");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter new name for the album:");

        // Get the response value
        dialog.showAndWait().ifPresent(newName -> {
            if (!newName.isEmpty()) {
                photo.setCaption(newName);
                showPhotos(); // Refresh album list
                showAlert("Success", "Album renamed successfully.");
            } else {
                showAlert("Error", "Please enter a valid album name.");
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
