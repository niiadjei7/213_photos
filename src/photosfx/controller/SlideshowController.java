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

    private void displayPhoto() {
        Photo currentPhoto = photos.get(currentIndex);
        String photoPath = currentPhoto.getPathInDisk();
        Image image = new Image("file:" + photoPath);
        imageView.setImage(image);
        captionLabel.setText(currentPhoto.getCaption());
    }

    private void setupSlideshowTimeline() {
        slideshowTimeline = new Timeline(
                new KeyFrame(Duration.seconds(3), event -> showNextPhoto()));
        slideshowTimeline.setCycleCount(Timeline.INDEFINITE);
        slideshowTimeline.play();
    }

    private void showNextPhoto() {
        currentIndex = (currentIndex + 1) % photos.size();
        displayPhoto();
    }

    @FXML
    private void previousButtonClicked() {
        currentIndex = (currentIndex - 1 + photos.size()) % photos.size();
        displayPhoto();
        resetSlideshowTimeline();
    }

    @FXML
    private void nextButtonClicked() {
        showNextPhoto();
        resetSlideshowTimeline();
    }

    private void resetSlideshowTimeline() {
        slideshowTimeline.stop();
        slideshowTimeline.play();
    }
}
