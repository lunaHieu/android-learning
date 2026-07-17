package hieu.nv.dang8.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hieu.nv.dang8.R;
import hieu.nv.dang8.database.UsageDBHelper;
import hieu.nv.dang8.model.AppUsageModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements UsageAdapter.OnAppMonitorClickListener {
    private UsageDBHelper dbHelper;
    private RecyclerView rvUsage;
    private UsageAdapter adapter;
    private List<AppUsageModel> usageList = new ArrayList<>();

    private TextView txtAppLabel, txtTimer;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Xin quyền gửi tin nhắn SMS hệ thống ngay khi mở app
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 888);
        }

        dbHelper = new UsageDBHelper(this);
        txtAppLabel = findViewById(R.id.txt_current_app_label);
        txtTimer = findViewById(R.id.txt_countdown_timer);
        rvUsage = findViewById(R.id.rv_usage_limits);

        rvUsage.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsageAdapter(usageList, this);
        rvUsage.setAdapter(adapter);

        usageList.addAll(dbHelper.getAllLimits());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStartMonitorClick(AppUsageModel model) {
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Hủy bộ đếm của app cũ nếu đang chạy
        }

        txtAppLabel.setText("Monitoring: " + model.getAppName());

        // Mẹo đi thi: Đổi số phút thành số giây (nhân 1000 thay vì 60000) để đồng hồ chạy nhanh, giáo viên thấy kết quả gộp luôn mà không cần chờ 30 phút.
        long timeInMillis = model.getTimeLimit() * 1000L;

        countDownTimer = new CountDownTimer(timeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int totalSeconds = (int) (millisUntilFinished / 1000);
                int hours = totalSeconds / 3600;
                int minutes = (totalSeconds % 3600) / 60;
                int seconds = totalSeconds % 60;

                txtTimer.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds));
            }

            @Override
            public void onFinish() {
                txtTimer.setText("00:00:00");
                Toast.makeText(MainActivity.this, "❌ TIME OUT! Limit reached for " + model.getAppName(), Toast.LENGTH_LONG).show();

                // Kích hoạt hàm bắn SMS khẩn cấp cho phụ huynh
                sendAlertSMS(model.getParentPhone(), model.getAppName());
            }
        }.start();
    }

    // --- 🔥 THUẬT TOÁN TỰ ĐỘNG GỬI SMS CHO PHỤ HUYNH KHI HẾT GIỜ ---
    private void sendAlertSMS(String phoneNumber, String appName) {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                SmsManager smsManager = SmsManager.getDefault();
                String message = "[SocialGuard Alert] Your child has exceeded the daily usage limit for " + appName + "!";

                // Thực thi gửi tin nhắn văn bản ngầm ra ngoài hệ thống
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                Toast.makeText(this, "📩 Security SMS sent to parent: " + phoneNumber, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SMS Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS check emulator network!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}