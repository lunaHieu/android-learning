package hieu.nv.dang10.model;

public class StudentModel {
    private int id;
    private String name;
    private double score;
    private String email;

    public StudentModel(int id, String name, double score, String email) {
        this.id = id; this.name = name; this.score = score; this.email = email;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public double getScore() { return score; }
    public String getEmail() { return email; }
}