package hieu.nv.dang2.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import hieu.nv.dang2.R;
import hieu.nv.dang2.model.VideoModel;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private Context context;
    private List<VideoModel> list;
    private OnVideoActionListener listener;

    public interface OnVideoActionListener {
        void onShareVideo(VideoModel model);
        void onDeleteVideo(VideoModel model);
        void onUpdateVideo(VideoModel model);
        void onPlayVideo(VideoModel model);
    }

    public VideoAdapter(Context context, List<VideoModel> list, OnVideoActionListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoModel model = list.get(position);
        holder.txtTitle.setText(model.getTitle());
        holder.txtCategory.setText(model.getCategory());

        // Glide xử lý Uri video trực tiếp để render ảnh đại diện
        Glide.with(context)
                .load(model.getPath())
                .placeholder(android.R.drawable.presence_video_online)
                .error(android.R.drawable.presence_video_away)
                .into(holder.imgThumb);

        holder.itemView.setOnClickListener(v -> listener.onPlayVideo(model));

        holder.btnMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, v);
            popup.getMenu().add("Chia sẻ Video");
            popup.getMenu().add("Sửa thông tin");
            popup.getMenu().add("Xóa Video");

            popup.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("Chia sẻ Video")) {
                    listener.onShareVideo(model);
                } else if (item.getTitle().equals("Sửa thông tin")) {
                    listener.onUpdateVideo(model);
                } else if (item.getTitle().equals("Xóa Video")) {
                    listener.onDeleteVideo(model);
                }
                return true;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumb, btnMenu;
        TextView txtTitle, txtCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Sửa lại đúng tên ID theo file item_video.xml mới
            imgThumb = itemView.findViewById(R.id.img_video_thumbnail);
            btnMenu = itemView.findViewById(R.id.btn_video_menu);
            txtTitle = itemView.findViewById(R.id.txt_video_title);
            txtCategory = itemView.findViewById(R.id.txt_video_category);
        }
    }
}