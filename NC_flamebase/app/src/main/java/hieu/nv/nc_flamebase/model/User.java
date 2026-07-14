package hieu.nv.nc_flamebase.model;

public class User {

    private String password;
    private String userID;
    private String email;
    private String role;

    // 1. Constructor không tham số (Bắt buộc cho Firebase)
    public User() {}

    // 2. Constructor đầy đủ tham số
    public User(String password, String userID, String email, String role) {
        this.password = password;
        this.userID = userID;
        this.email = email;
        this.role = role;
    }

    // 3. Các hàm Getter
    public String getUserID() { return userID; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getPassword() {
        return password;
    }

    // 4. Các hàm Setter (Cần bổ sung để Firebase hoạt động)
    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}