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

    public boolean addAlbum(Album album) {
        if (albums != null) {
            for (Album x : this.albums) {
                if (x.getAlbumName().equals(album.getAlbumName())) {
                    return false;// Make sure album name is not taken
                }
            }
        }

        this.albums.add(album);
        return true;
    }

    public void updateAlbum(Album album) {
        for (int i = 0; i < albums.size(); i++) {
            Album x = albums.get(i);
            if (x.getAlbumName().equals(album.getAlbumName())) {
                // Replace the existing album with the updated one
                albums.set(i, album);
                return;
            }
        }
    }

    public boolean deleteAlbum(Album album, String username) {
        DataFileManager.deleteData(
                DataFileManager.basePath + File.separator + username + File.separator + album.getAlbumName());
        return albums.remove(album);
    }

    public void saveUser() {
        File userFolder = new File("\\Admin\\" + File.separator + this.getUsername());
        userFolder.mkdirs();
        DataFileManager.saveData(this, userFolder + File.separator + "user.dat");
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
