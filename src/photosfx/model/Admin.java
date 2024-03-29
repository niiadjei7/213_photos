package photosfx.model;

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
        return true;
    }

    public boolean deleteUser(String username) {
        User user = userExists(username);
        if (user != null && !user.isAdmin()) {
            users.remove(user);
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
}
