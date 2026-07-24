package hieu.nv.dang1.model;

public class ImageModel {
    private int id;
    private String title;
    private String path;
    private long timestamp;
    private int albumId;

    public ImageModel(int id, String title, String path, long timestamp, int albumId) {
        this.id = id;
        this.title = title;
        this.path = path;
        this.timestamp = timestamp;
        this.albumId = albumId;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getPath() { return path; }
    public long getTimestamp() { return timestamp; }
    public int getAlbumId() { return albumId; }

    public void setTitle(String title) { this.title = title; }
}