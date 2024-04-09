package photosfx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import photosfx.model.Photo;
import photosfx.model.Tag;

import java.util.*;

/**
 * Controller class for displaying and managing a single photo.
 */
public class PhotoController {

    @FXML
    private ImageView photoImageView;

    @FXML
    private Label captionLabel;

    @FXML
    private Label dateTimeLabel;

    @FXML
    private ListView<Tag> tagsListView;

    private Photo currentPhoto;

    /**
     * Initialize the photo controller with the given photo.
     * 
     * @param photo The photo to display.
     */
    public void initialize(Photo photo) {
        currentPhoto = photo;
        displayPhotoInfo();
    }

    /**
     * Display the information of the current photo.
     */
    private void displayPhotoInfo() {
        if (currentPhoto != null) {
            // Display photo image
            // Assuming photoImageView is set with the image

            // Display caption
            captionLabel.setText(currentPhoto.getCaption());

            // Display date-time
            String dateTimeStr = currentPhoto.getDate().toString();
            dateTimeLabel.setText(dateTimeStr);

            // Display tags
            List<Tag> tags = currentPhoto.getTags();
            tagsListView.getItems().addAll(tags);
        }
    }

    /**
     * Add a new tag to the current photo.
     */
    @FXML
    private void addTag() {
        // Implement adding a new tag functionality
    }

    /**
     * Delete a tag from the current photo.
     */
    @FXML
    private void deleteTag() {
        // Implement deleting a tag functionality
    }

    /**
     * Close the photo dialog.
     */
    @FXML
    private void closeDialog() {
        // Implement closing the photo dialog
    }
}
