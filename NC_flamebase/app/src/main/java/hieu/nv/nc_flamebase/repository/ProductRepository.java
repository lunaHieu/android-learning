package hieu.nv.nc_flamebase.repository;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hieu.nv.nc_flamebase.model.Product;

public class ProductRepository {
    private final DatabaseReference database;

    public ProductRepository() {
        // Trỏ đến nhánh "products" trong Realtime Database
        database = FirebaseDatabase.getInstance().getReference("products");
    }

    // 1. Lấy tất cả sản phẩm (Lấy 1 lần duy nhất)
    public Task<DataSnapshot> getAllProducts() {
        Log.d("ProductRepository", "Đang lấy tất cả sản phẩm...");
        return database.get();
    }

    // 2. Thêm sản phẩm mới
    public Task<Void> addProduct(Product product) {
        if (product.getProductID() == null || product.getProductID().isEmpty()) {
            // Tự động tạo ID ngẫu nhiên từ Firebase nếu sản phẩm chưa có ID
            String productId = database.push().getKey();
            if (productId != null) {
                product.setProductID(productId);
                Log.d("ProductRepository", "ID sản phẩm mới được tạo: " + productId);
            } else {
                Log.e("ProductRepository", "Không thể tạo ID sản phẩm");
                return Tasks.forException(new Exception("Không thể tạo ID sản phẩm"));
            }
        }
        Log.d("ProductRepository", "Thêm sản phẩm: " + product.getName());
        return database.child(product.getProductID()).setValue(product);
    }

    // 3. Cập nhật sản phẩm
    public Task<Void> updateProduct(Product product) {
        if (product.getProductID() != null && !product.getProductID().isEmpty()) {
            Log.d("ProductRepository", "Cập nhật sản phẩm ID: " + product.getProductID());
            return database.child(product.getProductID()).setValue(product);
        } else {
            Log.e("ProductRepository", "ID sản phẩm không hợp lệ khi cập nhật");
            return Tasks.forException(new Exception("ID sản phẩm không hợp lệ"));
        }
    }

    // 4. Xóa sản phẩm
    public Task<Void> deleteProduct(String productID) {
        if (productID != null && !productID.isEmpty()) {
            Log.d("ProductRepository", "Xóa sản phẩm với ID: " + productID);
            return database.child(productID).removeValue();
        } else {
            Log.e("ProductRepository", "ID sản phẩm không hợp lệ khi xóa");
            return Tasks.forException(new Exception("ID sản phẩm không hợp lệ"));
        }
    }

    /**
     * Gợi ý thêm: Lắng nghe dữ liệu thay đổi theo thời gian thực (Real-time)
     * Thường dùng để cập nhật danh sách lên giao diện ngay khi có thay đổi trên Firebase
     */
    public void listenToProductsRealtime(com.google.firebase.database.ValueEventListener listener) {
        database.addValueEventListener(listener);
    }
}