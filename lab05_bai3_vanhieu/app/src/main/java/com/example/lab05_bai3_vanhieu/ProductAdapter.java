package com.example.lab05_bai3_vanhieu;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Cần thêm thư viện Glide vào Gradle

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textViewName.setText(product.getName());
        holder.textViewPrice.setText(String.format("%,.0f VNĐ", product.getPrice()));

        // Load image dùng Glide (cần internet permission và dependency)
        // Nếu ảnh rỗng hoặc lỗi, sẽ hiện ảnh mặc định
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(product.getImageUrl())
                    .placeholder(R.mipmap.ic_launcher) // Ảnh chờ
                    .error(R.mipmap.ic_launcher)       // Ảnh lỗi
                    .into(holder.imageViewProduct);
        }

        // Xử lý click item
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Chọn: " + product.getName(), Toast.LENGTH_SHORT).show();
        });

        // Xử lý nút Sửa
        holder.buttonEdit.setOnClickListener(v -> {
            showProductDialog(product, position, true);
        });

        // Xử lý nút Xóa
        holder.buttonDelete.setOnClickListener(v -> {
            showDeleteConfirmDialog(product, position);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // --- CÁC HÀM XỬ LÝ LOGIC ---

    // Hàm thêm item mới (Gọi từ MainActivity)
    public void addProduct(Product product) {
        productList.add(product);
        notifyItemInserted(productList.size() - 1);
        // Cuộn xuống dòng cuối cùng
        // (Cần xử lý ở MainActivity nếu muốn mượt mà hơn)
    }

    // Hiển thị dialog chung cho Thêm và Sửa
    // public để MainActivity có thể gọi khi bấm nút "Thêm"
    public void showProductDialog(Product product, int position, boolean isEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_product, null);
        builder.setView(dialogView);

        // Tìm view trong dialog
        // Lưu ý: Đảm bảo file dialog_edit_product.xml có các ID này
        @SuppressLint("MissingInflatedId") TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle); // Bạn cần thêm ID này vào XML nếu chưa có
        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextPrice = dialogView.findViewById(R.id.editTextPrice);
        EditText editTextImageUrl = dialogView.findViewById(R.id.editTextImageUrl);

        // Set tiêu đề nếu dialogTitle tồn tại (tránh null pointer)
        if (dialogTitle != null) {
            dialogTitle.setText(isEdit ? "Sửa thông tin sản phẩm" : "Thêm sản phẩm mới");
        }

        // Fill dữ liệu cũ nếu là Sửa
        if (isEdit && product != null) {
            editTextName.setText(product.getName());
            editTextPrice.setText(String.valueOf(product.getPrice()));
            editTextImageUrl.setText(product.getImageUrl());
        }

        builder.setPositiveButton(isEdit ? "Lưu" : "Thêm", (dialog, which) -> {
            String newName = editTextName.getText().toString().trim();
            String newPriceStr = editTextPrice.getText().toString().trim();
            String newImageUrl = editTextImageUrl.getText().toString().trim();

            if (newName.isEmpty() || newPriceStr.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập đủ tên và giá!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double newPrice = Double.parseDouble(newPriceStr);

                if (isEdit) {
                    // LOGIC SỬA
                    product.setName(newName);
                    product.setPrice(newPrice);
                    product.setImageUrl(newImageUrl);
                    notifyItemChanged(position); // Cập nhật lại dòng đó
                    Toast.makeText(context, "Đã cập nhật!", Toast.LENGTH_SHORT).show();
                } else {
                    // LOGIC THÊM MỚI
                    Product newProduct = new Product(newName, newPrice, newImageUrl);
                    addProduct(newProduct);
                    Toast.makeText(context, "Đã thêm mới!", Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                Toast.makeText(context, "Giá tiền phải là số!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    // Dialog xác nhận xóa
    private void showDeleteConfirmDialog(Product product, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa: " + product.getName() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    productList.remove(position);
                    notifyItemRemoved(position);
                    // Quan trọng: Cập nhật lại index cho các item phía sau để tránh lỗi sai vị trí
                    notifyItemRangeChanged(position, productList.size());
                    Toast.makeText(context, "Đã xóa!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct; // Đã đổi tên khớp với item_product.xml
        TextView textViewName;
        TextView textViewPrice;
        Button buttonEdit;
        Button buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ ID từ file item_product.xml
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}