package hieu.nv.kiemtra3.model;

public class Book {
    public int id;
    public String title, author, publisherName;
    public double price;

    public Book(int id, String title, String author, double price, String publisherName) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.publisherName = publisherName;
    }
}