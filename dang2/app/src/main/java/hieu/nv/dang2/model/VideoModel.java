package hieu.nv.dang2.model;

public class VideoModel {
    private int id;
    private String title;
    private String path;
    private long timestamp;
    private String category;

    public VideoModel(int id, String title, String path, long timestamp, String category) {
        this.id = id;
        this.title = title;
        this.path = path;
        this.timestamp = timestamp;
        this.category = category;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getPath() { return path; }
    public long getTimestamp() { return timestamp; }
    public String getCategory() { return category; }

    public void setTitle(String title) { this.title = title; }
    public void setCategory(String category) { this.category = category; }
}