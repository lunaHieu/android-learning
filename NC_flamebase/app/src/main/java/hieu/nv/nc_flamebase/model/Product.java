package hieu.nv.nc_flamebase.model;

public class Product {
    private String productID;
    private String name;
    private int price;
    private int quantity;

    public Product() {}

    public Product(String productID, String name, int price, int quantity) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductID() { return productID; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // 5. Hàm toString (Tùy chọn - Giúp bạn in Log để kiểm tra dữ liệu dễ hơn)
    @Override
    public String toString() {
        return "Product{" +
                "id='" + productID + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}