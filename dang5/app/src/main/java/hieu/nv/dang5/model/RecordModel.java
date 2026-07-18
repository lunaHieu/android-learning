package hieu.nv.dang5.model;

public class RecordModel {
    private int id;
    private String title;
    private String path;
    private long timestamp; // Ngày giờ thực hiện thu âm
    private String length;    // Thời lượng tệp ghi âm (ví dụ: 00:15)

    public RecordModel(int id, String title, String path, long timestamp, String length) {
        this.id = id;
        this.title = title;
        this.path = path;
        this.timestamp = timestamp;
        this.length = length;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getPath() { return path; }
    public long getTimestamp() { return timestamp; }
    public String getLength() { return length; }

    public void setTitle(String title) { this.title = title; }
}