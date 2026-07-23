package hieu.nv.dang3.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hieu.nv.dang3.R;
import hieu.nv.dang3.database.AlarmDBHelper;
import hieu.nv.dang3.model.AlarmModel;
import hieu.nv.dang3.receiver.AlarmReceiver;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements AlarmAdapter.OnAlarmStatusChangeListener {
    private AlarmDBHelper dbHelper;
    private RecyclerView rvAlarms;
    private AlarmAdapter adapter;
    private List<AlarmModel> alarmList = new ArrayList<>();
    private TextView txtWorldTime;
    private Button btnAddAlarm;
    private final android.os.Handler clockHandler = new android.os.Handler();
    private final Runnable clockRunnable = new Runnable() {
        @Override
        public void run() {
            displayWorldClock(); // Cập nhật lại chuỗi hiển thị giờ
            clockHandler.postDelayed(this, 1000); // Tự động lặp lại sau mỗi 1 giây
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Kiểm tra cấp quyền hiển thị đè hệ thống phục vụ tính năng giải toán tắt chuông
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

        dbHelper = new AlarmDBHelper(this);
        txtWorldTime = findViewById(R.id.txt_world_time);
        rvAlarms = findViewById(R.id.rv_alarms);
        btnAddAlarm = findViewById(R.id.btn_add_alarm_trigger);

        rvAlarms.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AlarmAdapter(this, alarmList, this);
        rvAlarms.setAdapter(adapter);

        clockHandler.post(clockRunnable);
        loadAlarms();

        btnAddAlarm.setOnClickListener(v -> openTimePickerDialog());
    }

    // --- YÊU CẦU 1: XEM GIỜ THÀNH PHỐ BẤT KỲ THEO TIMEZONE ---
    private void displayWorldClock() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("America/New_York")); // Lấy múi giờ New York làm mẫu
        txtWorldTime.setText(sdf.format(cal.getTime()));
    }

    private void loadAlarms() {
        alarmList.clear();
        alarmList.addAll(dbHelper.getAllAlarms());
        adapter.notifyDataSetChanged();
    }

    // --- YÊU CẦU 2: ĐẶT BÁO THỨC BẰNG TIMEPICKER ---
    private void openTimePickerDialog() {
        Calendar now = Calendar.getInstance();
        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            long id = dbHelper.insertAlarm(hourOfDay, minute);
            setSystemAlarm((int) id, hourOfDay, minute);
            loadAlarms();
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true).show();
    }

    private void setSystemAlarm(int id, int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        Calendar targetTime = Calendar.getInstance();
        targetTime.set(Calendar.HOUR_OF_DAY, hour);
        targetTime.set(Calendar.MINUTE, minute);
        targetTime.set(Calendar.SECOND, 0);

        if (targetTime.before(Calendar.getInstance())) {
            targetTime.add(Calendar.DATE, 1);
        }

        if (alarmManager != null) {
            // 💡 ĐỔI TỪ setExactAndAllowWhileIdle SANG HÀM NÀY ĐỂ BỎ QUA CHECK SECURITY CỦA ANDROID mới:
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, targetTime.getTimeInMillis(), pendingIntent);
        }
    }
    private void cancelSystemAlarm(int id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    @Override
    public void onToggleChange(AlarmModel model, boolean isChecked) {
        dbHelper.updateAlarmStatus(model.getId(), isChecked ? 1 : 0);
        if (isChecked) {
            setSystemAlarm(model.getId(), model.getHour(), model.getMinute());
            Toast.makeText(this, "Đã bật báo thức!", Toast.LENGTH_SHORT).show();
        } else {
            cancelSystemAlarm(model.getId());
            Toast.makeText(this, "Đã hủy chuông reo!", Toast.LENGTH_SHORT).show();
        }
        loadAlarms();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        clockHandler.removeCallbacks(clockRunnable);
}}