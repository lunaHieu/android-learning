package hieu.nv.nc_flamebase.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hieu.nv.nc_flamebase.R;
import hieu.nv.nc_flamebase.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products = new ArrayList<>();

    // Hàm cập nhật danh sách sản phẩm và làm mới giao diện
    public void setProducts(List<Product> products) {
        if (products != null) {
            this.products = products;
            Log.d("ProductAdapter", "Sản phẩm đã được cập nhật: " + products.size());
        } else {
            this.products = new ArrayList<>();
            Log.d("ProductAdapter", "Danh sách sản phẩm rỗng");
        }
        notifyDataSetChanged(); // Thông báo cho RecyclerView vẽ lại giao diện
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp layout item_product cho từng dòng của danh sách
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);

        // Đổ dữ liệu từ Object Product vào các TextView
        holder.textViewProductName.setText(product.getName());

        // Định dạng giá tiền (ví dụ: 100,000 VND)
        holder.textViewProductPrice.setText(String.format("%,d VND", product.getPrice()));

        // Hiển thị số lượng
        holder.textViewProductQuantity.setText(String.format("Số lượng: %d", product.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    // Lớp giữ các thành phần giao diện của một dòng (Item)
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProductName;
        TextView textViewProductPrice;
        TextView textViewProductQuantity;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            textViewProductQuantity = itemView.findViewById(R.id.textViewProductQuantity);
        }
    }
}