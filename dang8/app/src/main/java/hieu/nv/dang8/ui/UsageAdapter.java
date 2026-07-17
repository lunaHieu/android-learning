package hieu.nv.dang8.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hieu.nv.dang8.R;
import hieu.nv.dang8.model.AppUsageModel;
import java.util.List;

public class UsageAdapter extends RecyclerView.Adapter<UsageAdapter.ViewHolder> {
    private List<AppUsageModel> list;
    private OnAppMonitorClickListener listener;

    public interface OnAppMonitorClickListener {
        void onStartMonitorClick(AppUsageModel model);
    }

    public UsageAdapter(List<AppUsageModel> list, OnAppMonitorClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usage, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppUsageModel model = list.get(position);
        holder.txtAppName.setText(model.getAppName());
        holder.txtDetails.setText("Limit: " + model.getTimeLimit() + " mins • Parent: " + model.getParentPhone());

        holder.btnStart.setOnClickListener(v -> listener.onStartMonitorClick(model));
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAppName, txtDetails;
        Button btnStart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAppName = itemView.findViewById(R.id.txt_app_name);
            txtDetails = itemView.findViewById(R.id.txt_app_details);
            btnStart = itemView.findViewById(R.id.btn_start_monitor);
        }
    }
}