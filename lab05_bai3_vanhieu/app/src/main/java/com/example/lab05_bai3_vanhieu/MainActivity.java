package com.example.lab05_bai3_vanhieu;

import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager; // Thêm import LayoutManager
import androidx.recyclerview.widget.RecyclerView; // Thêm import RecyclerView

import java.util.ArrayList; // Thêm import ArrayList
import java.util.List;      // Thêm import List

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerView);
        Button buttonAdd = findViewById(R.id.buttonAdd);
        productList = new ArrayList<>();
        productList.add(new Product("Sản phẩm 1", 10000, "https://cdn.shopify.com/s/files/1/0456/5070/6581/files/top-23-mau-giay-sneaker-dang-duoc-san-lung-nhat-nam-2022_600x600.jpg?v=1666940188"));
        productList.add(new Product("Sản phẩm 2", 20000, "https://cdn.shopify.com/s/files/1/0456/5070/6581/files/nhung-doi-giay-sneaker_1024x1024.png?v=1684721992"));
        productList.add(new Product("Sản phẩm 3", 15000, "https://cdn.shopify.com/s/files/1/0456/5070/6581/files/than-giay-duoc-gia-cong-tu-chat-lieu-cao-cap_600x600.jpg?v=1666940380"));
        adapter = new ProductAdapter(this, productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        buttonAdd.setOnClickListener(v -> {
            adapter.showProductDialog(null, -1, false);
        });
    }
}