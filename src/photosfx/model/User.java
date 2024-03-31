package photosfx.model;

import java.util.*;
import java.io.*;

public class User implements Serializable {
    private String username;
    private boolean isAdmin;
    private List<Album> albums;

    public User(String username, boolean isAdmin) {
        this.username = username;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public List<Album> albumList() {
        return this.albums;
    }

    public boolean deleteAlbum(Album album) {
        return albums.remove(album);
    }

    // Getters and setters
}
