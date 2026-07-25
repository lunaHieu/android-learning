package hieu.nv.dang11.ui;

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
import hieu.nv.dang11.R;
import hieu.nv.dang11.database.MeetingDBHelper;
import hieu.nv.dang11.model.MeetingModel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MeetingDBHelper dbHelper;
    private RecyclerView rvMeetings;
    private MeetingAdapter adapter;
    private List<MeetingModel> meetingList = new ArrayList<>();
    private Button btnOpenAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MeetingDBHelper(this);
        rvMeetings = findViewById(R.id.rv_meetings);
        btnOpenAdd = findViewById(R.id.btn_open_add_meeting);

        rvMeetings.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MeetingAdapter(meetingList, this, dbHelper);
        rvMeetings.setAdapter(adapter);

        loadMeetings();

        btnOpenAdd.setOnClickListener(v -> showAddMeetingDialog());
    }

    public void loadMeetings() {
        meetingList.clear();
        meetingList.addAll(dbHelper.getAllMeetings());
        adapter.notifyDataSetChanged();
    }

    private void showAddMeetingDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_meeting, null);
        EditText edtTitle = view.findViewById(R.id.edt_meet_title);
        EditText edtAttendees = view.findViewById(R.id.edt_meet_attendees);
        EditText edtHost = view.findViewById(R.id.edt_meet_host);
        EditText edtTime = view.findViewById(R.id.edt_meet_time);
        EditText edtLocation = view.findViewById(R.id.edt_meet_location);

        new AlertDialog.Builder(this)
                .setTitle("Lập lịch cuộc họp mới")
                .setView(view)
                .setPositiveButton("Lưu Lịch", (dialog, which) -> {
                    String title = edtTitle.getText().toString().trim();
                    String attendees = edtAttendees.getText().toString().trim();
                    String host = edtHost.getText().toString().trim();
                    String time = edtTime.getText().toString().trim();
                    String location = edtLocation.getText().toString().trim();

                    if (!title.isEmpty() && !attendees.isEmpty() && !host.isEmpty()) {
                        dbHelper.insertMeeting(title, attendees, host, time.isEmpty() ? "Chưa định ngày" : time, location.isEmpty() ? "Phòng họp trực tuyến" : location);
                        loadMeetings();
                        Toast.makeText(MainActivity.this, "Đã khởi tạo lịch họp thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Các trường Tiêu đề, Thành viên, Chủ trì không được bỏ trống!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}