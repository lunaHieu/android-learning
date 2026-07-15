package hieu.nv.nguyenvanhieu_128.model;
public class TopicModel {
    private String id;
    private String title;
    private String host;
    private double budget;
    private int year;
    private String result;

    public TopicModel(String id, String title, String host, double budget, int year, String result) {
        this.id = id;
        this.title = title;
        this.host = host;
        this.budget = budget;
        this.year = year;
        this.result = result;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getHost() { return host; }
    public double getBudget() { return budget; }
    public int getYear() { return year; }
    public String getResult() { return result; }
}