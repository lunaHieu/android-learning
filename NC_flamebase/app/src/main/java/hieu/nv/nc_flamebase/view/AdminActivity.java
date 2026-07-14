package hieu.nv.nc_flamebase.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hieu.nv.nc_flamebase.R;
import hieu.nv.nc_flamebase.model.Product;
import hieu.nv.nc_flamebase.view.adapter.ProductAdapterAdmin;
import hieu.nv.nc_flamebase.viewmodel.ProductViewModel;

public class AdminActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapterAdmin adapter;
    private ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // 1. Ánh xạ các thành phần giao diện
        Button buttonAddProduct = findViewById(R.id.buttonAddProduct);
        recyclerView = findViewById(R.id.recyclerViewProducts);

        // 2. Thiết lập RecyclerView và Adapter
        adapter = new ProductAdapterAdmin();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 3. Kết nối ViewModel
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        // Quan sát danh sách sản phẩm và cập nhật lên Adapter
        productViewModel.getProducts().observe(this, products -> {
            if (products != null) {
                adapter.setProducts(products);
            }
        });

        // 4. Xử lý sự kiện khi nhấn nút "Thêm sản phẩm"
        buttonAddProduct.setOnClickListener(v -> openEditProductDialog(null));

        // 5. Xử lý sự kiện "Sửa" từ Adapter
        adapter.setOnEditClickListener(product -> openEditProductDialog(product));

        // 6. Xử lý sự kiện "Xóa" từ Adapter
        adapter.setOnDeleteClickListener(product -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa " + product.getName() + " không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        productViewModel.deleteProduct(product.getProductID());
                        Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void openEditProductDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Chú ý: File layout này phải tồn tại trong res/layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_product, null);
        builder.setView(dialogView);

        EditText editTextProductName = dialogView.findViewById(R.id.editTextProductName);
        EditText editTextProductPrice = dialogView.findViewById(R.id.editTextProductPrice);
        EditText editTextProductQuantity = dialogView.findViewById(R.id.editTextProductQuantity);

        // Nếu là chế độ "Sửa", điền dữ liệu cũ vào các ô nhập
        if (product != null) {
            editTextProductName.setText(product.getName());
            editTextProductPrice.setText(String.valueOf(product.getPrice()));
            editTextProductQuantity.setText(String.valueOf(product.getQuantity()));
        }

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            try {
                String productName = editTextProductName.getText().toString().trim();
                int productPrice = Integer.parseInt(editTextProductPrice.getText().toString().trim());
                int productQuantity = Integer.parseInt(editTextProductQuantity.getText().toString().trim());

                if (productName.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập tên sản phẩm", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (product == null) {
                    // THÊM MỚI: Dùng Push ID từ ViewModel/Repository
                    String newID = productViewModel.generateProductID();
                    // ĐÚNG THỨ TỰ CONSTRUCTOR: ID, Name, Price, Quantity
                    Product newProduct = new Product(newID, productName, productPrice, productQuantity);
                    productViewModel.addProduct(newProduct);
                } else {
                    // CẬP NHẬT: Giữ nguyên ID cũ
                    product.setName(productName);
                    product.setPrice(productPrice);
                    product.setQuantity(productQuantity);
                    productViewModel.updateProduct(product);
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Vui lòng nhập đúng định dạng số!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}