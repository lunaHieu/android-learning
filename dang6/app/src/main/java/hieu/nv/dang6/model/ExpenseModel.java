package hieu.nv.dang6.model;

public class ExpenseModel {
    private int id;
    private String title;
    private double amount;
    private String type; // "THU" hoặc "CHI"

    public ExpenseModel(int id, String title, double amount, String type) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.type = type;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
}