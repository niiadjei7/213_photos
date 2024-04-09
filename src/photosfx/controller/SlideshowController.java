package photosfx.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import photosfx.model.Album;
import photosfx.model.Photo;
import javafx.scene.control.*;

import java.util.List;

/**
 * Controller class for the slideshow view in the application.
 * Manages the display of photos in a slideshow format.
 */
public class SlideshowController {

    @FXML
    private ImageView imageView;

    @FXML
    private Label captionLabel;

    @FXML
    private Button previousButton;

    @FXML
    private Button nextButton;

    private Album album;
    private List<Photo> photos;
    private int currentIndex;
    private Timeline slideshowTimeline;

    /**
     * Initializes the slideshow controller with the specified album.
     * @param album The album containing the photos for the slideshow.
     */
    public void initialize(Album album) {
        this.album = album;
        this.photos = album.getPhotos();
        this.currentIndex = 0;

        if (!photos.isEmpty()) {
            displayPhoto();
            setupSlideshowTimeline();
        } else {
            return;
        }
    }

    /**
     * Displays the current photo in the slideshow.
     */
    private void displayPhoto() {
        Photo currentPhoto = photos.get(currentIndex);
        String photoPath = currentPhoto.getPathInDisk();
        Image image = new Image("file:" + photoPath);
        imageView.setImage(image);
        captionLabel.setText(currentPhoto.getCaption());
    }

    /**
     * Sets up the timeline for the slideshow.
     */
    private void setupSlideshowTimeline() {
        slideshowTimeline = new Timeline(
                new KeyFrame(Duration.seconds(3), event -> showNextPhoto()));
        slideshowTimeline.setCycleCount(Timeline.INDEFINITE);
        slideshowTimeline.play();
    }

    /**
     * Shows the next photo in the slideshow.
     */
    private void showNextPhoto() {
        currentIndex = (currentIndex + 1) % photos.size();
        displayPhoto();
    }

    /**
     * Handles the action of clicking the previous button.
     * Displays the previous photo in the slideshow.
     */
    @FXML
    private void previousButtonClicked() {
        currentIndex = (currentIndex - 1 + photos.size()) % photos.size();
        displayPhoto();
        resetSlideshowTimeline();
    }

    /**
     * Handles the action of clicking the next button.
     * Displays the next photo in the slideshow.
     */
    @FXML
    private void nextButtonClicked() {
        showNextPhoto();
        resetSlideshowTimeline();
    }

    /**
     * Resets the slideshow timeline.
     */
    private void resetSlideshowTimeline() {
        slideshowTimeline.stop();
        slideshowTimeline.play();
    }
}
