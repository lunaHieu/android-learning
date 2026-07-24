package hieu.nv.dang1.ui;

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
import hieu.nv.dang1.R;
import hieu.nv.dang1.model.ImageModel;
import java.io.File;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context context;
    private List<ImageModel> list;
    private OnImageActionListener actionListener;

    public interface OnImageActionListener {
        void onShareClick(ImageModel model);
        void onDeleteClick(ImageModel model);
        void onUpdateClick(ImageModel model);
    }

    public ImageAdapter(Context context, List<ImageModel> list, OnImageActionListener actionListener) {
        this.context = context;
        this.list = list;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageModel model = list.get(position);
        holder.txtTitle.setText(model.getTitle());

        // Dùng Glide nạp ảnh mượt từ đường dẫn file
        Glide.with(context)
                .load(model.getPath()) // Truyền trực tiếp chuỗi Uri, không dùng new File() nữa
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(holder.imgPhoto);

        // Xử lý PopupMenu khi click vào icon ba chấm
        holder.btnMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, v);
            popup.getMenu().add("Chia sẻ lên MXH");
            popup.getMenu().add("Sửa tên ảnh");
            popup.getMenu().add("Xóa ảnh");

            popup.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("Chia sẻ lên MXH")) {
                    actionListener.onShareClick(model);
                } else if (item.getTitle().equals("Sửa tên ảnh")) {
                    actionListener.onUpdateClick(model);
                } else if (item.getTitle().equals("Xóa ảnh")) {
                    actionListener.onDeleteClick(model);
                }
                return true;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto, btnMenu;
        TextView txtTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_view_photo);
            btnMenu = itemView.findViewById(R.id.img_btn_menu);
            txtTitle = itemView.findViewById(R.id.txt_image_title);
        }
    }
}