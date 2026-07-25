package hieu.nv.dang11.model;

public class MeetingModel {
    private int id;
    private String title;
    private String attendees; // Danh sách email những người tham gia (ngăn cách bằng dấu phẩy)
    private String host;      // Người chủ trì
    private String time;      // Thời gian họp
    private String location;  // Địa điểm họp
    private int status;       // Trạng thái: 0 là Chưa xem, 1 là Đã xem

    public MeetingModel(int id, String title, String attendees, String host, String time, String location, int status) {
        this.id = id;
        this.title = title;
        this.attendees = attendees;
        this.host = host;
        this.time = time;
        this.location = location;
        this.status = status;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAttendees() { return attendees; }
    public String getHost() { return host; }
    public String getTime() { return time; }
    public String getLocation() { return location; }
    public int getStatus() { return status; }
}