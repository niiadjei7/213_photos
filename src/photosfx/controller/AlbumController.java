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

/**
 * Controller class for handling actions and events related to the album view.
 */
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

    /**
     * Initializes the AlbumController with the specified album and user.
     * 
     * @param album The album to be displayed and managed.
     * @param user  The user who owns the album.
     */
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

    /**
     * Handles the action of clicking the slideshow button.
     * Shows a slideshow window displaying all photos in the album.
     * 
     * @param event The ActionEvent representing the click event of the slideshow button.
     */
    @FXML
    private void slideShowButtonClicked(ActionEvent event) {
        if (!album.getPhotos().isEmpty()) {
            showSlideshowWindow();
        } else {
            showAlert("Error", "No pictures to show");
        }
    }

    /**
     * Shows the slideshow window displaying all photos in the album.
     */
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

    /**
     * Updates the UI to display the photos in the album.
     * Retrieves the list of photos from the album, clears the existing photo display,
     * and generates UI components to display each photo.
     * Additionally, it updates the user's album data and saves the changes to the user's file.
     * If the photos fail to load, an error alert is displayed.
     */
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

    /**
 * Displays the full-size image of the specified photo.
 * If the photo file does not exist, shows an error alert.
 * 
 * @param photo The photo object for which to display the full-size image.
 */
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

    /**
 * Allows the user to recaption a photo by displaying a dialog box.
 * The user can enter a new caption for the photo, and upon confirmation,
 * the new caption is set for the photo.
 * If the user enters an empty string as the new caption, an error alert is displayed.
 * After recaptioning the photo, the album list is refreshed to reflect the changes.
 * 
 * @param photo The photo to be recaptioned.
 */
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

    /**
 * Prompts the user to add tags to a specified photo.
 * Opens a dialog box for the user to enter tag type and value.
 * Splits the entered tag into type and value, then sets the tag for the photo.
 * Saves the updated photo information to disk and refreshes the album display.
 * Shows a success or error message based on the user input.
 * 
 * @param photo The photo to which the tag will be added.
 */
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

    /**
 * Deletes a tag associated with the specified photo.
 * Displays a dialog containing all tags associated with the photo, allowing the user to select
 * a tag to delete. Upon deletion, the photo display is refreshed to reflect the changes.
 * 
 * @param photo The photo from which to delete the tag.
 */
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

    /**
 * Displays an alert dialog with the specified title and message.
 * 
 * @param title   The title of the alert dialog.
 * @param message The message to be displayed in the alert dialog.
 */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles the action of clicking the add photo button.
     * Opens a file chooser dialog for the user to select a photo file and add it to the album.
     * 
     * @param event The ActionEvent representing the click event of the add photo button.
     */
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

    /**
 * Copies the specified photo to another album chosen by the user.
 * Opens a dialog prompting the user to enter the destination album name.
 * If the destination album exists and does not already contain the photo, the photo is copied to the destination album.
 * 
 * @param photo The photo to be copied.
 */
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

    /**
 * Moves the specified photo from its current album to a destination album.
 * Prompts the user to enter the name of the destination album.
 * If the destination album is valid and does not already contain the photo,
 * the photo is moved to the destination album, and the source album is updated accordingly.
 * Otherwise, an error message is displayed.
 * 
 * @param photo  The photo to be moved.
 * @param source The source album from which the photo is being moved.
 */
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

    /**
     * Handles the action of clicking the home button.
     * Redirects the user back to the user home view.
     */
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

    /**
     * Handles the action of clicking the logout button.
     * Logs out the user and redirects to the login view.
     */
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
