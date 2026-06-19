package hieu.nv.ktra2_nguyenvanhieu.view.adapter;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import hieu.nv.ktra2_nguyenvanhieu.R;
import hieu.nv.ktra2_nguyenvanhieu.model.Transaction;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Transaction> list = new ArrayList<>();
    private final int TYPE_THU = 1;
    private final int TYPE_CHI = 2;
    private int positionItemLongClick; // Lưu vị trí item được nhấn giữ để sửa/xóa

    // Interface để truyền sự kiện ra ngoài Fragment
    private OnItemLongClickListener listener;
    public interface OnItemLongClickListener {
        void onLongClick(Transaction transaction);
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.listener = listener;
    }

    public void setList(List<Transaction> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getPositionItemLongClick() {
        return positionItemLongClick;
    }

    public Transaction getTransactionAt(int position) {
        return list.get(position);
    }

    // Quyết định xem dòng này là Thu hay Chi
    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType() == 1 ? TYPE_THU : TYPE_CHI;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_THU) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction_thu, parent, false);
            return new ThuViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction_chi, parent, false);
            return new ChiViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Transaction t = list.get(position);
        DecimalFormat formatter = new DecimalFormat("#,### đ");

        if (holder.getItemViewType() == TYPE_THU) {
            ThuViewHolder thuHolder = (ThuViewHolder) holder;
            thuHolder.tvTitle.setText(t.getTitle());
            thuHolder.tvCategoryDate.setText(t.getCategory() + " | " + t.getDate());
            thuHolder.tvAmount.setText("+ " + formatter.format(t.getAmount()));
        } else {
            ChiViewHolder chiHolder = (ChiViewHolder) holder;
            chiHolder.tvTitle.setText(t.getTitle());
            chiHolder.tvCategoryDate.setText(t.getCategory() + " | " + t.getDate());
            chiHolder.tvAmount.setText("- " + formatter.format(t.getAmount()));
        }

        // Bắt sự kiện nhấn giữ (Long Click) để mở Menu Ngữ Cảnh (Sửa/Xóa)
        holder.itemView.setOnLongClickListener(v -> {
            positionItemLongClick = position;
            if (listener != null) listener.onLongClick(t);
            return false; // Trả về false để hệ thống tiếp tục hiển thị ContextMenu
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // ViewHolder cho loại THU
    class ThuViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView tvTitle, tvCategoryDate, tvAmount;
        public ThuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategoryDate = itemView.findViewById(R.id.tvCategoryDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            itemView.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Tùy chọn giao dịch");
            menu.add(0, 111, 0, "Sửa");
            menu.add(0, 112, 1, "Xóa");
        }
    }

    // ViewHolder cho loại CHI
    class ChiViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView tvTitle, tvCategoryDate, tvAmount;
        public ChiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategoryDate = itemView.findViewById(R.id.tvCategoryDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            itemView.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Tùy chọn giao dịch");
            menu.add(0, 111, 0, "Sửa");
            menu.add(0, 112, 1, "Xóa");
        }
    }
}