package photosfx.model;

import java.io.*;

public class DataFileManager implements Serializable {

    public static final String basePath = "C:\\Users\\rekaa\\OneDrive\\Desktop\\Pthotos_app_data" + File.separator
            + "Admin";

    public static void saveData(Object data, String fileName) {
        // File folder = new File(itemFolder);
        // folder.mkdirs();
        System.out.println("Attempted file path: " + fileName);
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            // Write the serialized data to the file
            objectOut.writeObject(data);
            System.out.println("Data saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Error saving data to disk: " + e.getMessage());
        }
    }

    public static Object loadData(String name) {
        String fileName = name;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            return inputStream.readObject(); // Deserialize and return the object
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName + " ---Load Failed---");
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
        }
        return null; // Return null if an error occurs
    }

    public static void deleteData(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("File or directory " + path + " does not exist.");
            return;
        }

        if (file.isDirectory()) {
            // Recursively delete all files and subdirectories
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteData(f.getAbsolutePath());
                }
            }
        }

        // Attempt to delete the file or directory
        if (file.delete()) {
            System.out.println("File or directory " + path + " has been successfully deleted.");
        } else {
            System.out.println("Failed to delete file or directory " + path);
        }
    }

    public static void saveUser(User user) {
        String userFolder = basePath + File.separator + user.getUsername();
        File folder = new File(userFolder);
        folder.mkdirs();

        String userDataPath = userFolder + File.separator + "user.dat";
        System.out.println("userDataPath: " + userDataPath);
        saveData(user, userDataPath);

        for (Album album : user.albumList()) {
            saveAlbum(album, userFolder);
        }
    }

    public static User loadUser(String username) {
        String userFolder = basePath + File.separator + username;
        System.out.println("Loading user from: " + userFolder);
        String userDataPath = userFolder + File.separator + "user.dat";

        User user = (User) loadData(userDataPath);

        if (user != null) {
            System.out.println("user loaded...");
            File[] albumFolders = new File(userFolder).listFiles(File::isDirectory);
            if (albumFolders != null) {
                for (File albumFolder : albumFolders) {
                    Album album = loadAlbum(albumFolder.getAbsolutePath(), user);
                    if (album != null) {
                        System.out.println("Album " + album.getAlbumName() + " loaded...");
                        user.addAlbum(album);
                    }
                }

            }
        }

        return user;
    }

    public static void saveAlbum(Album album, String userFolder) {
        String albumFolder = File.separator + userFolder + File.separator + album.getAlbumName();
        if (!(albumFolder.contains(basePath))) {
            albumFolder = basePath + albumFolder;
        }
        File folder = new File(albumFolder);
        folder.mkdirs();

        String albumDataPath = albumFolder + File.separator + "album.dat";
        System.out.println("attempted save path: " + albumDataPath);
        saveData(album, albumDataPath);

        for (Photo photo : album.getPhotos()) {
            System.out.println("albumFolder: " + albumFolder);
            savePhoto(photo, albumFolder);
        }
    }

    public static Album loadAlbum(String albumFolder, User user) {
        System.out.println("Loading albums from: " + albumFolder);
        String albumDataPath = albumFolder + File.separator + "album.dat";
        Album album = (Album) loadData(albumDataPath);

        if (album != null) {
            File[] photoFiles = new File(albumFolder)
                    .listFiles((dir, name) -> name.endsWith(".dat") && !name.equals("album.dat"));
            if (photoFiles != null) {
                System.out.println("Photo files present");
                for (File photoFile : photoFiles) {

                    Photo photo = loadPhoto(photoFile.getAbsolutePath(), user);
                    if (photo != null) {
                        album.addPhoto(photo);
                        System.out.println("photo " + photo.getCaption() + " loaded...");
                    }
                }
            } else {
                System.out.println("No photo files present");
            }
        }

        return album;
    }

    public static void savePhoto(Photo photo, String albumFolder) {
        if (!(albumFolder.contains(basePath))) {
            albumFolder = basePath + albumFolder;
        }
        String photoDataPath = File.separator + albumFolder + File.separator + photo.getCaption() + ".dat";
        System.out.println("Attempted save path for photo: " + photoDataPath);

        try {
            File folder = new File(photoDataPath).getParentFile();
            if (!folder.exists()) {
                if (!folder.mkdirs()) {
                    throw new IOException("Failed to create directory: " + folder.getAbsolutePath());
                }
            }

            saveData(photo, photoDataPath);
            System.out.println("Photo saved successfully at: " + photoDataPath);
        } catch (IOException e) {
            System.err.println("Error saving photo: " + e.getMessage());
            e.printStackTrace();
        }
        /*
         * for (Tag tag : photo.getTags()) {
         * savePhoto(photo, albumFolder);
         * }
         */
    }

    public static Photo loadPhoto(String photoFolder, User user) {
        System.out.println("photoFolder: " + photoFolder);
        String photoDataPath = photoFolder;
        Photo photo = (Photo) loadData(photoDataPath);
        if (photo != null) {
            System.out.println("photo loaded");
        } else {
            System.out.println("photo loaded as null");
        }

        /*
         * if (photo != null) {
         * File[] photoFiles = new File(photoFolder)
         * .listFiles((dir, name) -> name.endsWith(".dat") &&
         * !name.equals("album.dat"));
         * if (photoFiles != null) {
         * for (File photoFile : photoFiles) {
         * Photo photo = loadPhoto(photoFile.getAbsolutePath());
         * if (photo != null) {
         * album.addPhoto(photo);
         * }
         * }
         * }
         * }
         */

        return photo;
    }

}
