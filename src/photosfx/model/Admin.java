package photosfx.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Admin {
    private List<User> users;

    public Admin() {
        this.users = new ArrayList<>();
        users.add(new User("admin", true));
    }

    public boolean createUser(String username) {
        // Check if the user already exists
        if (userExists(username) != null) {
            return false; // User already exists
        }
        // Create a new user
        User user = new User(username, false); // Non-admin user
        users.add(user);
        saveUser(user);
        return true;
    }

    public boolean deleteUser(String username) {
        User user = userExists(username);
        if (user != null && !user.isAdmin()) {
            users.remove(user);
            DataFileManager.deleteData("Admin" + File.separator + username + ".dat");
            return true;
        }
        return false; // User not found or is admin
    }

    private User userExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null; // User not found
    }

    public List<User> getUsers() {
        return this.users;
    }

    public void saveUser(User user) {
        DataFileManager.saveData(user, "\\Admin\\" + user.getUsername() + ".dat");
    }

    public void loadUsers() {
        String adminPath = DataFileManager.basePath + "\\Admin";
        File adminFolder = new File(adminPath);
        File[] userFiles = adminFolder.listFiles();
        if (userFiles != null) {
            for (File file : userFiles) {
                if (file.isFile()) {
                    User user = (User) DataFileManager.loadData(adminPath + "\\" + file.getName());
                    if (user != null) {
                        this.users.add(user);
                    }
                }
            }
        }
    }
}
