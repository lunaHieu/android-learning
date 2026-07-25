package hieu.nv.dang12.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hieu.nv.dang12.R;
import hieu.nv.dang12.database.JobDBHelper;
import hieu.nv.dang12.model.JobModel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private JobDBHelper dbHelper;
    private RecyclerView rvJobs;
    private JobAdapter adapter;
    private List<JobModel> jobList = new ArrayList<>();
    private Button btnOpenAdd, btnTodo, btnDoing, btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new JobDBHelper(this);
        rvJobs = findViewById(R.id.rv_jobs);
        btnOpenAdd = findViewById(R.id.btn_open_add_job);
        btnTodo = findViewById(R.id.btn_filter_todo);
        btnDoing = findViewById(R.id.btn_filter_doing);
        btnDone = findViewById(R.id.btn_filter_done);

        rvJobs.setLayoutManager(new LinearLayoutManager(this));
        adapter = new JobAdapter(jobList, this, dbHelper);
        rvJobs.setAdapter(adapter);

        loadJobs();

        btnOpenAdd.setOnClickListener(v -> showAddJobDialog());

        // 💡 THỰC THI LOGIC CHỨC NĂNG TÙY CHỌN BỔ SUNG: BỘ LỌC TABS NHANH THEO TRẠNG THÁI
        btnTodo.setOnClickListener(v -> loadFilteredJobs("chưa xử lý"));
        btnDoing.setOnClickListener(v -> loadFilteredJobs("đang xử lý"));
        btnDone.setOnClickListener(v -> loadFilteredJobs("đã xử lý"));
    }

    public void loadJobs() {
        jobList.clear();
        jobList.addAll(dbHelper.getAllJobs());
        adapter.notifyDataSetChanged();
    }

    private void loadFilteredJobs(String status) {
        jobList.clear();
        jobList.addAll(dbHelper.getJobsByStatus(status));
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Đã lọc danh sách: " + status.toUpperCase(), Toast.LENGTH_SHORT).show();
    }

    private void showAddJobDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_job, null);
        EditText edtTitle = view.findViewById(R.id.edt_job_title);
        EditText edtRole = view.findViewById(R.id.edt_job_role);
        EditText edtEmail = view.findViewById(R.id.edt_job_email);
        EditText edtStart = view.findViewById(R.id.edt_job_start);
        EditText edtEnd = view.findViewById(R.id.edt_job_end);

        new AlertDialog.Builder(this)
                .setTitle("Khởi tạo nhiệm vụ công việc mới")
                .setView(view)
                .setPositiveButton("Giao Việc", (dialog, which) -> {
                    String title = edtTitle.getText().toString().trim();
                    String role = edtRole.getText().toString().trim();
                    String email = edtEmail.getText().toString().trim();
                    String start = edtStart.getText().toString().trim();
                    String end = edtEnd.getText().toString().trim();

                    if (!title.isEmpty() && !role.isEmpty() && !email.isEmpty()) {
                        dbHelper.insertJob(title, role, start.isEmpty()?"2026-05-27":start, end.isEmpty()?"2026-06-30":end, email);
                        loadJobs();
                        Toast.makeText(MainActivity.this, "Đã lưu và phân công việc thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Không được bỏ trống Tiêu đề, Vai trò và Email!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}