package photosfx.model;

import java.io.*;

public class DataFileManager implements Serializable {

    public static final String basePath = "C:\\Users\\rekaa\\OneDrive\\Desktop\\Pthotos_app_data";

    public static void saveData(Object data, String fileName) {
        // File folder = new File(itemFolder);
        // folder.mkdirs();
        String filePath = basePath + fileName;
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            // Write the serialized data to the file
            objectOut.writeObject(data);
            System.out.println("Data saved to " + filePath);
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

    public static void deleteData(String name) {
        String filename = basePath + File.separator + name;
        File file = new File(filename);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("File " + filename + " has been successfully deleted.");
            } else {
                System.out.println("Failed to delete file " + filename);
            }
        } else {
            System.out.println("File " + filename + " does not exist.");
        }
    }

}
