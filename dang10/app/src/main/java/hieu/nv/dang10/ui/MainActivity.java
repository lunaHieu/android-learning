package hieu.nv.dang10.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hieu.nv.dang10.R;
import hieu.nv.dang10.database.StudentDBHelper;
import hieu.nv.dang10.model.StudentModel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private StudentDBHelper dbHelper;
    private RecyclerView rv;
    private StudentAdapter adapter;
    private List<StudentModel> list = new ArrayList<>();
    private TextView txtAvg;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new StudentDBHelper(this);
        rv = findViewById(R.id.rv_students);
        txtAvg = findViewById(R.id.txt_class_avg);
        btnAdd = findViewById(R.id.btn_add_student);

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentAdapter(list, this);
        rv.setAdapter(adapter);

        refreshData();

        btnAdd.setOnClickListener(v -> showAddDialog());
    }

    private void refreshData() {
        list.clear();
        list.addAll(dbHelper.getAllStudents());
        adapter.notifyDataSetChanged();

        // Cập nhật điểm trung bình của toàn lớp lên giao diện
        double avg = dbHelper.getAverageScore();
        txtAvg.setText(String.format("Điểm trung bình toàn khóa: %.2f điểm", avg));
    }

    private void showAddDialog() {
        // 1. Nạp file XML vào view của Dialog
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_student, null);

        // 2. Ánh xạ chính xác các ô nhập liệu từ file XML vừa tạo
        final EditText edtName = dialogView.findViewById(R.id.edt_student_name);
        final EditText edtScore = dialogView.findViewById(R.id.edt_student_score);
        final EditText edtEmail = dialogView.findViewById(R.id.edt_student_email);

        new AlertDialog.Builder(this)
                .setTitle("Nhập thông tin học sinh")
                .setView(dialogView) // Đẩy giao diện có 3 ô nhập vào hộp thoại
                .setPositiveButton("Lưu lại", (dialog, which) -> {
                    String name = edtName.getText().toString().trim();
                    String scoreStr = edtScore.getText().toString().trim();
                    String email = edtEmail.getText().toString().trim();

                    // Kiểm tra người dùng nhập đủ 3 trường mới cho lưu
                    if (!name.isEmpty() && !scoreStr.isEmpty() && !email.isEmpty()) {
                        double score = Double.parseDouble(scoreStr);

                        // Chèn dữ liệu thực tế người dùng vừa gõ vào SQLite
                        dbHelper.insertStudent(name, score, email);

                        // Cập nhật lại danh sách hiển thị và tính lại điểm TB toàn lớp
                        refreshData();
                    } else {
                        Toast.makeText(MainActivity.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
    }
