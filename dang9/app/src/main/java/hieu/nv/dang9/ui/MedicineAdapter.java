package hieu.nv.dang9.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hieu.nv.dang9.R;
import hieu.nv.dang9.model.MedicineModel;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {
    private List<MedicineModel> list;
    private OnMedicineDeleteClickListener listener;

    public interface OnMedicineDeleteClickListener { void onDeleteClick(MedicineModel model); }

    public MedicineAdapter(List<MedicineModel> list, OnMedicineDeleteClickListener listener) {
        this.list = list; this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MedicineModel model = list.get(position);
        holder.txtName.setText(model.getName());
        holder.txtDetails.setText("Mã: " + model.getCode() + " | Nhóm: " + model.getUsageLocation() + " | SL: " + model.getQuantity());
        holder.txtExpiry.setText("Hạn dùng: " + model.getExpiryDate());

        // Điểm cộng thuật toán: Nếu số lượng mặt hàng tồn kho cực ít (< 5) -> Cảnh báo đổi sang màu chữ đỏ rực
        if (model.getQuantity() < 5) {
            holder.txtDetails.setTextColor(0xFFD32F2F);
        } else {
            holder.txtDetails.setTextColor(0xFF757575);
        }

        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(model));
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtDetails, txtExpiry; ImageView btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_med_name);
            txtDetails = itemView.findViewById(R.id.txt_med_details);
            txtExpiry = itemView.findViewById(R.id.txt_med_expiry);
            btnDelete = itemView.findViewById(R.id.btn_med_delete);
        }
    }
}