package photosfx.controller;

import java.util.List;
import java.util.*;
import java.io.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.image.*;
import photosfx.model.*;
import javafx.geometry.*;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;

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
    private Button addTagsButton;

    @FXML
    private VBox photosContainer;

    @FXML
    private Button logoutButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button copyButton;

    @FXML
    private Button deleteTagButton;

    @FXML
    private Button moveButton;

    @FXML
    private Label captionLabel;

    @FXML
    private Label dateTimeLabel;

    @FXML
    private Button slideShowButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button prevButton;

    @FXML
    private Label tagsList;

    @FXML
    private FlowPane thumbnailsFlowPane;

    @FXML
    private VBox photoDisplayVBox;

    @FXML
    private ImageView photoDisplayImageView;

    private Album album;

    private User user;

    private int currentPhotoIndex = 0;
    private Stage slideShowStage;

    public void initialize(Album album, User user) {
        this.album = album;
        this.user = user;
        if (album == null) {
            showAlert("Error", "Album not found");
            ;
        }
        if (user == null) {
            showAlert("Error", "User not found");
            return;
        }

        if (album.getPhotos().isEmpty()) {
            slideShowButton.setDisable(true);
        }

        showPhotos();
    }

    @FXML
    private void slideShowButtonClicked(ActionEvent event) {
        if (!album.getPhotos().isEmpty()) {
            showSlideshowWindow();
        } else {
            showAlert("Error", "No pictures to show");
        }
    }

    private void showSlideshowWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Slideshow.fxml"));
            Parent root = loader.load();
            SlideshowController controller = loader.getController();
            controller.initialize(album);
            slideShowStage = new Stage();
            slideShowStage.setScene(new Scene(root));
            slideShowStage.setTitle("Slideshow");
            slideShowStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle error appropriately
        }
    }

    private void showPhotos() {
        String userPath = DataFileManager.basePath + File.separator + user.getUsername();
        String albumPath = userPath + File.separator + album.getAlbumName();
        user.updateAlbum(album);
        DataFileManager.saveUser(this.user);
        photosContainer.getChildren().clear();
        List<Photo> photos = album.getPhotos();
        if (photos == null) {
            showAlert("Error", "Photo failed to load");
            return;
        }
        for (Photo photo : photos) {
            File photoFile = new File(photo.getPathInDisk());
            if (!photoFile.exists()) {
                showAlert("Error", "Photo not found");
                continue;
            }
            Image thumbnailImage = new Image(photoFile.toURI().toString());
            ImageView thumbnailView = new ImageView(thumbnailImage);
            thumbnailView.setFitWidth(100); // Set thumbnail width
            thumbnailView.setPreserveRatio(true); // Preserve aspect ratio
            thumbnailView.setSmooth(true); // Enable smoother image rendering
            thumbnailView.setCache(true); // Cache the image for better performance

            thumbnailView.setOnMouseClicked(event -> displayFullSizeImage(photo));

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(event -> deletePhoto(photo));

            Button recaptionButton = new Button("Rename");
            recaptionButton.setOnAction(event -> recaptionPhoto(photo));

            Button addTagsButton = new Button("Add tag");
            addTagsButton.setOnAction(event -> addTag(photo));

            Button deleteTagButton = new Button("Delete Tag");
            deleteTagButton.setOnAction(event -> deleteTag(photo));

            Button copyButton = new Button("Copy");
            copyButton.setOnAction(event -> copyPhoto(photo));

            Button moveButton = new Button("Move");
            moveButton.setOnAction(event -> movePhoto(photo, album));

            Label photoLabel = new Label(photo.getCaption());
            Label tagsLabel = new Label(photo.getTags().toString());
            Label dateLabel = new Label(photo.getDate());

            VBox photoBox = new VBox(thumbnailView, photoLabel, tagsLabel, dateLabel, deleteButton, recaptionButton,
                    addTagsButton, deleteTagButton, copyButton, moveButton);
            photoBox.setSpacing(5);
            photosContainer.getChildren().add(photoBox);
        }
    }

    private void displayFullSizeImage(Photo photo) {
        File photoFile = new File(photo.getPathInDisk());
        if (!photoFile.exists()) {
            showAlert("Error", "Photo not found");
            ;
            return;
        }

        // Load the image
        Image fullImage = new Image(photoFile.toURI().toString());

        // Create an ImageView to display the image
        ImageView imageView = new ImageView(fullImage);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(600); // Adjust the width as needed

        // Create labels for caption, date of capture, and tags
        Label captionLabel = new Label("Caption: " + photo.getCaption());
        Label dateTimeLabel = new Label("Date of Capture: " + photo.getDate());
        Label tagsLabel = new Label("Tags: " + photo.getTags().toString());

        // Create a VBox to hold the image view
        VBox vbox = new VBox(imageView, captionLabel, dateTimeLabel, tagsLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        // Create a new scene and set it to the stage
        Scene scene = new Scene(vbox);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Full Size Image");
        stage.show();
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
    }

    private void recaptionPhoto(Photo photo) {
        TextInputDialog dialog = new TextInputDialog(photo.getCaption());
        dialog.setTitle("Recaption photo");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter new caption:");

        // Get the response value
        dialog.showAndWait().ifPresent(newName -> {
            if (!newName.isEmpty()) {
                photo.setCaption(newName);
                showPhotos(); // Refresh album list
                showAlert("Success", "Caption changed.");
            } else {
                showAlert("Error", "Please enter a valid caption.");
            }
        });
        showPhotos();
    }

    private void addTag(Photo photo) {
        TextInputDialog dialog = new TextInputDialog(photo.getTags().toString());
        dialog.setTitle("Add tags");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter tag type and value. Eg: Location,New Brunswick");

        dialog.showAndWait().ifPresent(newTag -> {
            if (!newTag.isEmpty()) {
                String[] tag = newTag.split(",");
                photo.setTag(tag[0], tag[1]);
                DataFileManager.savePhoto(photo, album.getAlbumName());
                showPhotos(); // Refresh album list
                showAlert("Success", "Tag added.");
            } else {
                showAlert("Error", "Please enter a valid Tag.");
            }
        });
        showPhotos();
    }

    private void deleteTag(Photo photo) {
        // Create a scroll pane to display all tags
        ScrollPane scrollPane = new ScrollPane();
        VBox tagsPane = new VBox();
        for (Tag tag : photo.getTags()) {
            HBox tagBox = new HBox();
            Label tagLabel = new Label(tag.toString());
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(event -> {
                photo.deleteTag(tag);
                showPhotos(); // Refresh the photo display
            });
            tagBox.getChildren().addAll(tagLabel, deleteButton);
            tagsPane.getChildren().add(tagBox);
        }
        scrollPane.setContent(tagsPane);

        // Create a dialog to display the scroll pane
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Delete Tag");
        dialog.setHeaderText("Select a tag to delete:");
        dialog.getDialogPane().setContent(scrollPane);

        // Add a close button to the dialog
        ButtonType closeButton = new ButtonType("Close", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);

        // Show the dialog
        dialog.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void addPhotoButtonClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Photo File");
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Photo");
            dialog.setHeaderText("Add Photo from Disk");
            dialog.setContentText("Enter a caption for the photo:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(caption -> {
                // Create a new Photo instance with the selected file and entered caption
                Calendar currentDate = Calendar.getInstance();
                Photo photo = new Photo(selectedFile.getName(), currentDate); // You should adjust this to get the photo
                                                                              // date
                photo.setCaption(caption);
                photo.setPath(selectedFile.getAbsolutePath());

                // Add the photo to the album
                album.addPhoto(photo);
                String userPath = DataFileManager.basePath + File.separator + user.getUsername();
                String albumPath = userPath + File.separator + album.getAlbumName();
                DataFileManager.saveUser(this.user);

                // Refresh the photos display
                showPhotos();

                showAlert("Success", "Photo added successfully.");
            });
        }
    }

    private void copyPhoto(Photo photo) {

        TextInputDialog dialog = new TextInputDialog(photo.getTags().toString());
        dialog.setTitle("Copy photo");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter destination album");

        dialog.showAndWait().ifPresent(destination -> {
            if (!destination.isEmpty()) {
                for (Album x : this.user.albumList()) {
                    if (x.getAlbumName().equals(destination) && !(x.getPhotos().contains(photo))) {
                        x.addPhoto(photo);
                        DataFileManager.saveAlbum(x, this.user.getUsername());
                        showAlert("Success", "Photo copied.");
                        break;
                    }
                }
                DataFileManager.saveUser(this.user);
                showPhotos(); // Refresh album list
            } else {
                showAlert("Error", "Copy failed.");
            }
        });
    }

    private void movePhoto(Photo photo, Album source) {
        TextInputDialog dialog = new TextInputDialog(photo.getTags().toString());
        dialog.setTitle("Move photo");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter destination album");

        dialog.showAndWait().ifPresent(destination -> {
            if (!destination.isEmpty()) {
                for (Album x : this.user.albumList()) {
                    if (x.getAlbumName().equals(destination) && !(x.getPhotos().contains(photo))) {
                        x.addPhoto(photo);
                        source.deletePhoto(photo);
                        DataFileManager.saveAlbum(x, this.user.getUsername());
                        showAlert("Success", "Photo moved.");
                        break;
                    }
                }
                DataFileManager.saveUser(this.user);
                showPhotos(); // Refresh album list
            } else {
                showAlert("Error", "Move failed.");
            }
        });
    }

    @FXML
    private void homeButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/UserHome.fxml"));
            Parent root = loader.load();
            UserHomeController controller = loader.getController();
            if (user == null) {
                showAlert("Error", "User not found");
            }
            controller.initialize(user); // Pass username to UserHomeController
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("User Home");
            stage.show();

            // Close the current login window
            Stage currentStage = (Stage) addPhotoButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

}
