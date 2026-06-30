package hieu.nv.lab13_nguyenvanhieu;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> playlist = new ArrayList<>();
    private ArrayList<String> songNames = new ArrayList<>(); // Danh sách chứa tên bài hát
    private TextView tvSongName;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSongName = findViewById(R.id.tvSongName);
        tvSongName.setSelected(true); // Bật tính năng chạy chữ (marquee)

        Button btnPick = findViewById(R.id.buttonPick);
        Button btnPlayPause = findViewById(R.id.buttonStartPause);
        Button btnNext = findViewById(R.id.buttonNext);
        Button btnPrev = findViewById(R.id.buttonPrev);
        Button btnStop = findViewById(R.id.buttonStop);

        // ĐĂNG KÝ BẮT SÓNG: Lắng nghe Service báo cáo tên bài hát
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("UPDATE_SONG_NAME")) {
                    String name = intent.getStringExtra("SONG_NAME");
                    tvSongName.setText("Đang phát: " + name);
                }
            }
        };
        // Dùng code an toàn cho Android 14 của bạn
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, new IntentFilter("UPDATE_SONG_NAME"), Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(receiver, new IntentFilter("UPDATE_SONG_NAME"));
        }

        // Xử lý khi chọn nhạc
        ActivityResultLauncher<Intent> audioPicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        playlist.clear();
                        songNames.clear();

                        if (result.getData().getClipData() != null) {
                            int count = result.getData().getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                Uri uri = result.getData().getClipData().getItemAt(i).getUri();
                                getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                playlist.add(uri.toString());
                                songNames.add(getFileName(uri)); // Lấy tên thật của file
                            }
                            Toast.makeText(this, "Đã tải " + count + " bài!", Toast.LENGTH_SHORT).show();
                        } else if (result.getData().getData() != null) {
                            Uri uri = result.getData().getData();
                            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            playlist.add(uri.toString());
                            songNames.add(getFileName(uri)); // Lấy tên thật của file
                            Toast.makeText(this, "Đã tải 1 bài!", Toast.LENGTH_SHORT).show();
                        }

                        // Gửi danh sách qua Service
                        Intent intent = new Intent(MainActivity.this, MusicService.class);
                        intent.setAction("PLAY_NEW_LIST");
                        intent.putStringArrayListExtra("PLAYLIST", playlist);
                        intent.putStringArrayListExtra("SONG_NAMES", songNames); // Gửi thêm danh sách tên
                        startService(intent);
                    }
                }
        );

        btnPick.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("audio/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            audioPicker.launch(intent);
        });

        btnPlayPause.setOnClickListener(v -> sendActionToService("PAUSE_RESUME"));
        btnNext.setOnClickListener(v -> sendActionToService("NEXT"));
        btnPrev.setOnClickListener(v -> sendActionToService("PREV"));
        btnStop.setOnClickListener(v -> {
            stopService(new Intent(MainActivity.this, MusicService.class));
            tvSongName.setText("Đã dừng nhạc");
        });
    }

    private void sendActionToService(String action) {
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        intent.setAction(action);
        startService(intent);
    }

    // Hàm chuyên dụng để trích xuất tên File từ URI
    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = "Bài hát không tên";
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver); // Phải hủy đăng ký khi tắt app để tránh tốn Ram
    }
}