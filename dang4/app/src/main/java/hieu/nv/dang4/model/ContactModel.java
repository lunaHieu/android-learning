package hieu.nv.dang4.model;

public class ContactModel {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String groupName; // Lưu tên nhóm (ví dụ: Gia đình, Bạn bè, Công việc)

    public ContactModel(int id, String name, String phone, String email, String groupName) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.groupName = groupName;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getGroupName() { return groupName; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
}