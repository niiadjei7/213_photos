package photosfx.model;

import java.util.*;

public class Album {
    private String albumName;
    private List<Photo> photos;

    public Album(String name) {
        this.albumName = name;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public List<Photo> getPhotos() {
        return this.photos;
    }
}
