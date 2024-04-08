package photosfx.model;

import java.io.Serializable;
import java.util.*;

public class Album implements Serializable {
    private String albumName;
    private List<Photo> photos;

    public Album(String name) {
        this.albumName = name;
        this.photos = new ArrayList<>();
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public void setAlbumName(String newName) {
        this.albumName = newName;
        return;
    }

    public List<Photo> getPhotos() {
        return this.photos;
    }

    public boolean addPhoto(Photo photo) {
        if (!this.photos.contains(photo)) {
            this.photos.add(photo);
            return true;
        }
        return false;
    }

    public boolean deletePhoto(Photo photo) {
        if (this.photos.contains(photo)) {
            this.photos.remove(photo);
            return true;
        }
        return false;
    }

    public String getDateRange() {
        if (!(this.photos.isEmpty())) {
            Photo earliestPhoto = photos.get(0);
            for (Photo photo : photos) {
                if (photo.getCalDate().before(earliestPhoto.getCalDate())) {
                    earliestPhoto = photo;
                }
            }

            Photo latestPhoto = photos.get(0);
            for (Photo photo : photos) {
                if (photo.getCalDate().after(latestPhoto.getCalDate())) {
                    latestPhoto = photo;
                }
            }
            return earliestPhoto.getDate().toString() + " - " + latestPhoto.getDate().toString();
        }
        return "";
    }

}
