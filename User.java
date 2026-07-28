import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username, password, fullName, email;

    public User() {}

    public User(String username, String password, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean validateCredentials(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    @Override
    public String toString() {
        return String.format("Username: %s, Name: %s, Email: %s", username, fullName, email);
    }
}
