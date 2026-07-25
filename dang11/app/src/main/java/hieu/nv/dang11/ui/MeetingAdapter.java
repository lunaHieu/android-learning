package hieu.nv.dang11.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hieu.nv.dang11.R;
import hieu.nv.dang11.database.MeetingDBHelper;
import hieu.nv.dang11.model.MeetingModel;
import java.util.List;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder> {
    private List<MeetingModel> list;
    private Context context;
    private MeetingDBHelper dbHelper;

    public MeetingAdapter(List<MeetingModel> list, Context context, MeetingDBHelper dbHelper) {
        this.list = list;
        this.context = context;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_meeting, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MeetingModel m = list.get(position);
        holder.txtTitle.setText(m.getTitle());
        holder.txtInfo.setText("Chủ trì: " + m.getHost() + "\nThời gian: " + m.getTime() + "\nĐịa điểm: " + m.getLocation() + "\nThành viên: " + m.getAttendees());

        // CHỨC NĂNG THEO ĐỀ: Xem trạng thái Đã xem hoặc Chưa xem lịch họp
        if (m.getStatus() == 1) {
            holder.txtStatus.setText("Trạng thái: ĐÃ XEM LỊCH HỌP");
            holder.txtStatus.setTextColor(0xFF4CAF50); // Màu xanh lá mượt mà
        } else {
            holder.txtStatus.setText("Trạng thái: CHƯA XEM THÔNG BÁO");
            holder.txtStatus.setTextColor(0xFFE53935); // Màu đỏ rực báo động
        }

        // Sự kiện nút "Đã xem" để chuyển đổi trạng thái logic cập nhật UI ngay lập tức
        holder.btnToggle.setOnClickListener(v -> {
            dbHelper.updateMeetingStatus(m.getId(), 1);
            if (context instanceof MainActivity) {
                ((MainActivity) context).loadMeetings();
            }
            Toast.makeText(context, "Đã cập nhật trạng thái đọc lịch họp!", Toast.LENGTH_SHORT).show();
        });

        // 🔥 CHỨC NĂNG THEO ĐỀ: GỬI THƯ ĐIỆN TỬ THÔNG BÁO LỊCH HỌP ĐẾN NGƯỜI THAM GIA
        holder.btnInvite.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            // Cắt danh sách chuỗi email ngăn cách bằng dấu phẩy để chèn đồng loạt vào ô To nhận mail
            intent.putExtra(Intent.EXTRA_EMAIL, m.getAttendees().split(","));
            intent.putExtra(Intent.EXTRA_SUBJECT, "[THÔNG BÁO LỊCH HỌP KHẨN] " + m.getTitle());
            intent.putExtra(Intent.EXTRA_TEXT, "Kính mời đồng chí tham dự cuộc họp cơ quan chuẩn bị diễn ra:\n\n" +
                    "- Nội dung: " + m.getTitle() + "\n" +
                    "- Người chủ trì: " + m.getHost() + "\n" +
                    "- Thời gian: " + m.getTime() + "\n" +
                    "- Địa điểm: " + m.getLocation() + "\n\n" +
                    "Yêu cầu các thành viên tham gia họp có mặt đầy đủ, đúng giờ. Xin cảm ơn!");

            try {
                context.startActivity(Intent.createChooser(intent, "Gửi lời mời họp qua Email:"));
            } catch (Exception e) {
                Toast.makeText(context, "Không tìm thấy ứng dụng mail trên hệ thống máy ảo!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtInfo, txtStatus;
        Button btnToggle, btnInvite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_meet_title);
            txtInfo = itemView.findViewById(R.id.txt_meet_info);
            txtStatus = itemView.findViewById(R.id.txt_meet_status);
            btnToggle = itemView.findViewById(R.id.btn_toggle_status);
            btnInvite = itemView.findViewById(R.id.btn_invite_email);
        }
    }
}