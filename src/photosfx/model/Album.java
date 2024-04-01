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

    public List<Photo> getPhotos() {
        return this.photos;
    }
}
