package hieu.nv.dang5.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import hieu.nv.dang5.R;
import hieu.nv.dang5.model.RecordModel;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private Context context;
    private List<RecordModel> list;
    private OnRecordItemActionListener listener;

    public interface OnRecordItemActionListener {
        void onPlayClick(RecordModel model);
        void onShareClick(RecordModel model);
        void onDeleteClick(RecordModel model);
    }

    public RecordAdapter(Context context, List<RecordModel> list, OnRecordItemActionListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_record, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecordModel model = list.get(position);
        holder.txtTitle.setText(model.getTitle());
        holder.txtDuration.setText("Thời lượng: " + model.getLength());

        holder.itemView.setOnClickListener(v -> listener.onPlayClick(model));

        holder.btnMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, v);
            popup.getMenu().add("Chia sẻ tệp");
            popup.getMenu().add("Xóa bản ghi");
            popup.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("Chia sẻ tệp")) {
                    listener.onShareClick(model);
                } else if (item.getTitle().equals("Xóa bản ghi")) {
                    listener.onDeleteClick(model);
                }
                return true;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDuration;
        ImageView btnMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_record_title);
            txtDuration = itemView.findViewById(R.id.txt_record_duration);
            btnMenu = itemView.findViewById(R.id.btn_record_menu);
        }
    }
}