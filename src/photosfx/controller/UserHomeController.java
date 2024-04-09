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

/**
 * Controller class for the User Home view.
 * Handles user interactions and actions related to the user's home interface.
 */
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

    /**
     * Initializes the UserHomeController with the logged-in user.
     *
     * @param user The logged-in user.
     */
    public void initialize(User user) {
        this.user = user;
        if (user == null) {
            showAlert("Error", "User not found");
            ;
        }
        welcomeLabel.setText("Welcome, " + user.getUsername() + "!");
        listAlbums();
    }

    /**
     * Updates user data by saving changes.
     */
    public void updateData() {
        DataFileManager.saveUser(user);
    }

    /**
     * Lists all albums belonging to the user.
     */
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

    /**
     * Opens the selected album.
     *
     * @param album            The album to be opened.
     * @param openAlbumButton  The button triggering the action.
     */
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

    /**
     * Deletes the specified album.
     *
     * @param album The album to be deleted.
     */
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

    /**
     * Renames the specified album.
     *
     * @param album The album to be renamed.
     */
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

    /**
 * Handles the action of clicking the create album button.
 * Makes the create album box visible, allowing the user to enter a new album name.
 *
 * @param event The ActionEvent representing the click event of the create album button.
 */
    @FXML
    private void createAlbumButtonClicked(ActionEvent event) {
        createAlbumBox.setVisible(true);
    }

    /**
 * Handles the action of clicking the save album button.
 * Creates a new album with the provided name, adds it to the user's list of albums,
 * and saves it to the data file. Displays an alert indicating success or failure.
 *
 * @param event The ActionEvent representing the click event of the save album button.
 */
    @FXML
    private void saveAlbumButtonClicked(ActionEvent event) {
        String albumName = albumNameField.getText().trim();
        if (!albumName.isEmpty()) {
            // Create a new album with the provided name
            Album album = new Album(albumName);
            // Add the album to the user's list of albums
            if (user.addAlbum(album)) {
                // If the album was added successfully, save it to the data file
                showAlert("Album Created", "Album '" + albumName + "' created successfully.");
                DataFileManager.saveAlbum(album, user.getUsername());
            } else {
                // If the album name is already in use, display an error alert
                showAlert("Error", "Album name already in use");
            }
            // Clear the album name field and hide the create album box
            albumNameField.clear();
            createAlbumBox.setVisible(false);
        } else {
            // If the provided album name is empty, display an error alert
            showAlert("Error", "Please enter a valid album name.");
        }
        // Update the list of albums displayed in the UI
        listAlbums();
    }

    /**
 * Handles the action of clicking the cancel album button.
 * Clears the album name field and hides the create album box.
 *
 * @param event The ActionEvent representing the click event of the cancel album button.
 */
    @FXML
    private void cancelAlbumButtonClicked(ActionEvent event) {
        albumNameField.clear();
        createAlbumBox.setVisible(false);
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
     * Handles the action of clicking the logout button.
     * Saves user data and returns to the login screen.
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

    /**
     * Handles the action of clicking the search button.
     * Opens a search dialog window where users can search for photos based on various criteria.
     *
     * @param event The ActionEvent representing the click event of the search button.
     */
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
