package hieu.nv.nc_flamebase.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hieu.nv.nc_flamebase.R;
import hieu.nv.nc_flamebase.view.adapter.ProductAdapter;
import hieu.nv.nc_flamebase.viewmodel.ProductViewModel;

public class CustomerActivity extends AppCompatActivity {
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        // 1. Ánh xạ RecyclerView từ giao diện
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);

        // Thiết lập LayoutManager (dạng danh sách dọc)
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        // 2. Khởi tạo Adapter và gán cho RecyclerView
        productAdapter = new ProductAdapter();
        recyclerViewProducts.setAdapter(productAdapter);

        // 3. Khởi tạo ViewModel bằng ViewModelProvider
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        // 4. Quan sát (Observe) danh sách sản phẩm từ ViewModel
        productViewModel.getProducts().observe(this, products -> {
            if (products != null && !products.isEmpty()) {
                Log.d("CustomerActivity", "Số lượng sản phẩm nhận được: " + products.size());

                // Cập nhật danh sách mới vào Adapter để hiển thị lên màn hình
                productAdapter.setProducts(products);
            } else {
                Log.d("CustomerActivity", "Danh sách sản phẩm trống hoặc null");
                Toast.makeText(this, "Hiện tại không có sản phẩm nào", Toast.LENGTH_SHORT).show();
            }
        });
    }
}