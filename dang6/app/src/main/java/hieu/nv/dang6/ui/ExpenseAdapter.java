package hieu.nv.dang6.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hieu.nv.dang6.R;
import hieu.nv.dang6.model.ExpenseModel;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    private List<ExpenseModel> list;

    public ExpenseAdapter(List<ExpenseModel> list) { this.list = list; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExpenseModel model = list.get(position);
        holder.txtTitle.setText(model.getTitle());

        if ("THU".equals(model.getType())) {
            holder.txtAmount.setText("+" + model.getAmount() + "đ");
            holder.txtAmount.setTextColor(0xFF4CAF50); // Màu xanh lá
        } else {
            holder.txtAmount.setText("-" + model.getAmount() + "đ");
            holder.txtAmount.setTextColor(0xFFF44336); // Màu đỏ
        }
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtAmount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_trans_title);
            txtAmount = itemView.findViewById(R.id.txt_trans_amount);
        }
    }
}