package inventory.management.system;

/**
 * User model class representing an authenticated user.
 */
public class WholeSellers {
    private int id;
    private String username;
    private String fullName;
    private String role;
    private String category;

    public WholeSellers(int id, String username, String fullName, String role, String category) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
