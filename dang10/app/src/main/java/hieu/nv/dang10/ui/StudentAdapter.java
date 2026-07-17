package hieu.nv.dang10.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hieu.nv.dang10.R;
import hieu.nv.dang10.model.StudentModel;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private List<StudentModel> list;
    private Context ctx;

    public StudentAdapter(List<StudentModel> list, Context ctx) { this.list = list; this.ctx = ctx; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_student, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentModel s = list.get(position);
        holder.txtName.setText(s.getName() + " - " + s.getScore() + "đ");
        holder.txtEmail.setText(s.getEmail());

        // Tính năng tự chọn bổ sung thực tế: Phân cấp học lực tự động hiển thị trực quan
        String rank = s.getScore() >= 8.0 ? "Học lực: GIỎI/XUẤT SẮC" : (s.getScore() >= 6.5 ? "Học lực: KHÁ" : "Học lực: TRUNG BÌNH");
        holder.txtRank.setText(rank);

        // 🔥 THỰC HIỆN CHỨC NĂNG: GỬI THƯ ĐIỆN TỬ THÔNG BÁO KẾT QUẢ HỌC TẬP
        holder.btnSendMail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{s.getEmail()});
            intent.putExtra(Intent.EXTRA_SUBJECT, "[KẾT QUẢ HỌC TẬP] Thông báo bảng điểm định kỳ");
            intent.putExtra(Intent.EXTRA_TEXT, "Thân gửi học sinh " + s.getName() + ",\n\n" +
                    "Nhà trường xin gửi thông báo kết quả học tập chính thức:\n" +
                    "- Điểm tổng kết hệ số: " + s.getScore() + " điểm.\n" +
                    "- Xếp loại học tập: " + rank + ".\n\n" +
                    "Mọi thắc mắc vui lòng liên hệ phòng đào tạo để được giải quyết.");
            ctx.startActivity(Intent.createChooser(intent, "Gửi mail báo điểm qua:"));
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtEmail, txtRank; Button btnSendMail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_stu_name);
            txtEmail = itemView.findViewById(R.id.txt_stu_email);
            txtRank = itemView.findViewById(R.id.txt_stu_rank);
            btnSendMail = itemView.findViewById(R.id.btn_send_mail);
        }
    }
}