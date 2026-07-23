package hieu.nv.dang3.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import hieu.nv.dang3.R;
import hieu.nv.dang3.model.AlarmModel;
import java.util.List;
import java.util.Locale;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {
    private Context context;
    private List<AlarmModel> list;
    private OnAlarmStatusChangeListener listener;

    public interface OnAlarmStatusChangeListener {
        void onToggleChange(AlarmModel model, boolean isChecked);
    }

    public AlarmAdapter(Context context, List<AlarmModel> list, OnAlarmStatusChangeListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_alarm, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AlarmModel model = list.get(position);
        holder.txtTime.setText(String.format(Locale.getDefault(), "%02d:%02d", model.getHour(), model.getMinute()));

        holder.switchToggle.setOnCheckedChangeListener(null); // Xóa listener cũ tránh lỗi lặp vô hạn
        holder.switchToggle.setChecked(model.getIsActive() == 1);

        holder.switchToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            listener.onToggleChange(model, isChecked);
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTime;
        SwitchCompat switchToggle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txt_alarm_time_display);
            switchToggle = itemView.findViewById(R.id.switch_alarm_toggle);
        }
    }
}