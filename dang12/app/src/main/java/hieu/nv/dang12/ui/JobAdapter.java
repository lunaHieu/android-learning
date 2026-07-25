package hieu.nv.dang12.ui;

import android.app.AlertDialog;
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
import hieu.nv.dang12.R;
import hieu.nv.dang12.database.JobDBHelper;
import hieu.nv.dang12.model.JobModel;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {
    private List<JobModel> list;
    private Context context;
    private JobDBHelper dbHelper;

    public JobAdapter(List<JobModel> list, Context context, JobDBHelper dbHelper) {
        this.list = list;
        this.context = context;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_job, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JobModel j = list.get(position);
        holder.txtTitle.setText(j.getTitle());
        holder.txtDetails.setText("Vai trò: " + j.getRole() + "\nThời gian: " + j.getTimeStart() + " -> " + j.getTimeEnd() + "\nThành viên: " + j.getEmail());

        // CHỨC NĂNG THEO ĐỀ: Hiển thị đúng 3 màu trạng thái xử lý trực quan
        String currentStatus = j.getStatus().toLowerCase();
        holder.txtStatus.setText("Trạng thái: " + currentStatus.toUpperCase());
        if ("đã xử lý".equals(currentStatus)) {
            holder.txtStatus.setTextColor(0xFF4CAF50); // Màu xanh hoàn thành
        } else if ("đang xử lý".equals(currentStatus)) {
            holder.txtStatus.setTextColor(0xFFFB8C00); // Màu cam đang chạy
        } else {
            holder.txtStatus.setTextColor(0xFFE53935); // Màu đỏ chưa động vào
        }

        // CHỨC NĂNG THEO ĐỀ: Thực hiện báo cáo tiến độ công việc (Hiện popup lựa chọn)
        holder.btnReport.setOnClickListener(v -> {
            String[] options = {"đang xử lý", "đã xử lý"};
            new AlertDialog.Builder(context)
                    .setTitle("Báo cáo tiến độ công việc")
                    .setItems(options, (dialog, which) -> {
                        dbHelper.updateJobStatus(j.getId(), options[which]);
                        if (context instanceof MainActivity) { ((MainActivity) context).loadJobs(); }
                        Toast.makeText(context, "Đã gửi báo cáo trạng thái thành công!", Toast.LENGTH_SHORT).show();
                    }).show();
        });

        // 🔥 CHỨC NĂNG THEO ĐỀ: GỬI THƯ ĐIỆN TỬ THÔNG BÁO CÔNG VIỆC MỚI ĐẾN NGƯỜI THAM GIA
        holder.btnEmail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{j.getEmail()});
            intent.putExtra(Intent.EXTRA_SUBJECT, "[HỆ THỐNG ĐIỀU HÀNH] Giao nhiệm vụ: " + j.getTitle());
            intent.putExtra(Intent.EXTRA_TEXT, "Thông báo phân công nhiệm vụ mới trên hệ thống:\n\n" +
                    "- Nội dung công việc: " + j.getTitle() + "\n" +
                    "- Vai trò của đồng chí: " + j.getRole() + "\n" +
                    "- Thời gian bắt đầu: " + j.getTimeStart() + "\n" +
                    "- Hạn kết thúc (Deadline): " + j.getTimeEnd() + "\n\n" +
                    "Yêu cầu đồng chí nghiêm túc thực hiện đúng tiến độ và cập nhật báo cáo trạng thái trên phần mềm.");
            try {
                context.startActivity(Intent.createChooser(intent, "Gửi Gmail giao việc:"));
            } catch (Exception e) {
                Toast.makeText(context, "Không có ứng dụng gửi email trên máy ảo!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDetails, txtStatus; Button btnReport, btnEmail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_job_title);
            txtDetails = itemView.findViewById(R.id.txt_job_details);
            txtStatus = itemView.findViewById(R.id.txt_job_status);
            btnReport = itemView.findViewById(R.id.btn_report_progress);
            btnEmail = itemView.findViewById(R.id.btn_send_job_email);
        }
    }
}