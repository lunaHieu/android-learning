package hieu.nv.nguyenvanhieu_128.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hieu.nv.nguyenvanhieu_128.R; // Thêm import R chuẩn xác
import hieu.nv.nguyenvanhieu_128.model.TopicModel;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {
    private List<TopicModel> list;
    private Context context;
    private OnTopicActionListener listener;

    public interface OnTopicActionListener {
        void onTopicClick(TopicModel model);
    }

    public TopicAdapter(List<TopicModel> list, Context context, OnTopicActionListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_topic, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TopicModel m = list.get(position);
        holder.txtTitle.setText(m.getId() + ": " + m.getTitle());
        holder.txtDetails.setText("Chủ nhiệm: " + m.getHost() + "\nNăm: " + m.getYear() + " | KP: " + String.format("%,.0f", m.getBudget()) + "đ");
        holder.txtResult.setText("Nghiệm thu: " + m.getResult().toUpperCase());

        String res = m.getResult().toLowerCase();
        if (res.contains("xuất sắc")) {
            holder.txtResult.setTextColor(0xFFD32F2F);
        } else if (res.contains("đạt")) {
            holder.txtResult.setTextColor(0xFF388E3C);
        } else {
            holder.txtResult.setTextColor(0xFF757575);
        }

        holder.itemView.setOnClickListener(v -> listener.onTopicClick(m));
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDetails, txtResult;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_item_title);
            txtDetails = itemView.findViewById(R.id.txt_item_details);
            txtResult = itemView.findViewById(R.id.txt_item_result);
        }
    }
}