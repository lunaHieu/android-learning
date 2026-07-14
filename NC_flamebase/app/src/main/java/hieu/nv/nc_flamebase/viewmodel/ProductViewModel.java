package hieu.nv.nc_flamebase.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import hieu.nv.nc_flamebase.model.Product;
import hieu.nv.nc_flamebase.repository.ProductRepository;

public class ProductViewModel extends ViewModel {
    private final ProductRepository productRepository = new ProductRepository();
    private final MutableLiveData<List<Product>> productsLiveData = new MutableLiveData<>();

    public ProductViewModel() {
        // Tải dữ liệu ngay khi ViewModel được khởi tạo
        getAllProducts();
    }

    // Lấy danh sách sản phẩm và trả về LiveData để View quan sát
    public LiveData<List<Product>> getAllProducts() {
        Log.d("ProductViewModel", "Đang lấy danh sách sản phẩm từ Firebase...");
        productRepository.getAllProducts().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Product> products = new ArrayList<>();
                for (DataSnapshot productSnapshot : task.getResult().getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        products.add(product);
                    }
                }
                productsLiveData.setValue(products);
                Log.d("ProductViewModel", "Tổng số sản phẩm đã tải: " + products.size());
            } else {
                Log.e("ProductViewModel", "Lỗi lấy danh sách sản phẩm", task.getException());
            }
        });
        return productsLiveData;
    }

    public LiveData<List<Product>> getProducts() {
        return productsLiveData;
    }

    // Thêm sản phẩm
    public void addProduct(Product product) {
        productRepository.addProduct(product).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                refreshProducts();
            } else {
                Log.e("ProductViewModel", "Thêm thất bại", task.getException());
            }
        });
    }

    // Cập nhật sản phẩm
    public void updateProduct(Product product) {
        productRepository.updateProduct(product).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                refreshProducts();
            } else {
                Log.e("ProductViewModel", "Cập nhật thất bại", task.getException());
            }
        });
    }

    // Xóa sản phẩm
    public void deleteProduct(String productID) {
        productRepository.deleteProduct(productID).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                refreshProducts();
            } else {
                Log.e("ProductViewModel", "Xóa thất bại", task.getException());
            }
        });
    }

    // Làm mới danh sách sau mỗi thao tác
    private void refreshProducts() {
        getAllProducts();
    }

    public String generateProductID() {
        return com.google.firebase.database.FirebaseDatabase.getInstance().getReference("products").push().getKey();
    }
}