package hieu.nv.dang9.model;

public class MedicineModel {
    private int id;
    private String name;
    private String expiryDate; // Tiêu chí: Sắp xếp theo hạn sử dụng
    private int quantity;
    private String usageLocation; // Bản chất là "Nhóm mặt hàng" (Ví dụ: Nhóm Tủ kính, Nhóm Tủ lạnh)
    private String code;          // Bổ sung thuộc tính Mã mặt hàng để truy xuất giá

    public MedicineModel(int id, String name, String expiryDate, int quantity, String usageLocation, String code) {
        this.id = id;
        this.name = name;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.usageLocation = usageLocation;
        this.code = code;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getExpiryDate() { return expiryDate; }
    public int getQuantity() { return quantity; }
    public String getUsageLocation() { return usageLocation; }
    public String getCode() { return code; }
}