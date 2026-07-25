package hieu.nv.dang12.model;

public class JobModel {
    private int id;
    private String title;
    private String role;       // Chỉ đạo, Chủ trì, Phối hợp, Thông báo để biết
    private String timeStart;
    private String timeEnd;
    private String email;      // Email người tham gia để gửi thông báo giao việc
    private String status;     // chưa xử lý, đang xử lý, đã xử lý

    public JobModel(int id, String title, String role, String timeStart, String timeEnd, String email, String status) {
        this.id = id;
        this.title = title;
        this.role = role;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.email = email;
        this.status = status;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getRole() { return role; }
    public String getTimeStart() { return timeStart; }
    public String getTimeEnd() { return timeEnd; }
    public String getEmail() { return email; }
    public String getStatus() { return status; }
}