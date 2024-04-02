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
        this.albums = new ArrayList<>();
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

    public boolean addAlbum(String album) {
        if (albums != null) {
            for (Album x : this.albums) {
                if (x.getAlbumName().equals(album)) {
                    return false;// Make sure album name is not taken
                }
            }
        }

        Album x = new Album(album);
        this.albums.add(x);
        return true;
    }

    public boolean deleteAlbum(Album album) {
        return albums.remove(album);
    }

    public void saveUser() {
        DataFileManager.saveData(this, "\\Admin\\" + this.getUsername() + ".dat");
    }

    public boolean renameAlbum(String currentName, String newName) {
        // first check if the album name is already taken
        for (Album x : this.albums) {
            if (x.getAlbumName().equals(newName)) {
                return false;
            }
        }

        for (Album x : this.albums) {
            if (x.getAlbumName().equals(currentName)) {
                x.setAlbumName(newName);
                return true;
            }
        }
        return false;// album not found
    }

    // Getters and setters
}
