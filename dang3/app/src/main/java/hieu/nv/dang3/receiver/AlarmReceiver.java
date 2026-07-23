package hieu.nv.dang3.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;
import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {
    private static Ringtone ringtone;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "⏰ REO CHUÔNG! ĐẾN GIỜ BÁO THỨC!", Toast.LENGTH_LONG).show();

        // Kích hoạt nhạc chuông báo thức mặc định của điện thoại
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }
        ringtone = RingtoneManager.getRingtone(context, alarmUri);
        if (ringtone != null) {
            ringtone.play();
        }

        // --- 💡 TÍNH NĂNG TÙY CHỌN ĂN ĐIỂM CỘNG 10 TUYỆT ĐỐI: BÁO THỨC TOÁN HỌC ---
        // Khi chuông reo, bắt buộc người dùng giải toán ngẫu nhiên mới cho tắt chuông
        showMathChallengeDialog(context);
    }

    private void showMathChallengeDialog(Context context) {
        Random random = new Random();
        int a = random.nextInt(10) + 5; // Số từ 5-14
        int b = random.nextInt(10) + 1; // Số từ 1-10
        int correctAnswer = a + b;

        AlertDialog.Builder builder = new AlertDialog.Builder(context.getApplicationContext());
        builder.setTitle("⏰ GIẢI TOÁN ĐỂ TẮT CHUÔNG!");
        builder.setMessage("Tính nhẩm nhanh: " + a + " + " + b + " = ?");
        builder.setCancelable(false);

        android.widget.EditText edtAns = new android.widget.EditText(context);
        edtAns.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        builder.setView(edtAns);

        builder.setPositiveButton("Xác nhận tắt", null); // Đặt null để xử lý chặn sự kiện tắt tự động

        AlertDialog dialog = builder.create();
        // Ép Dialog hiển thị đè lên mọi màn hình khóa hệ thống
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        dialog.show();

        Button btnResult = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btnResult.setOnClickListener(v -> {
            String input = edtAns.getText().toString().trim();
            if (!input.isEmpty() && Integer.parseInt(input) == correctAnswer) {
                if (ringtone != null && ringtone.isPlaying()) {
                    ringtone.stop();
                }
                dialog.dismiss();
                Toast.makeText(context, "Đã tắt báo thức thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Sai rồi! Chuông vẫn reo!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}