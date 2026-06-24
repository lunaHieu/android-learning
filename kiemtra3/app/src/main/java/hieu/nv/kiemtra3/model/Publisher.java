package hieu.nv.kiemtra3.model;

public class Publisher {
    public int id;
    public String name;

    public Publisher(int id, String name) {
        this.id = id;
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}