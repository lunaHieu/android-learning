package hieu.nv.kiemtra3.model;

public class User {
    public int id;
    public String username, password, role;

    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}