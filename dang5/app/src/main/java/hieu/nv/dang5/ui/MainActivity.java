package hieu.nv.dang5.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import hieu.nv.dang5.R;
import hieu.nv.dang5.database.RecordDBHelper;
import hieu.nv.dang5.model.RecordModel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements RecordAdapter.OnRecordItemActionListener {
    private RecordDBHelper dbHelper;
    private RecyclerView rvRecords;
    private RecordAdapter adapter;
    private List<RecordModel> recordList = new ArrayList<>();

    private FloatingActionButton fabRecord;
    private TextView txtTimer;

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private boolean isRecording = false;
    private String recordFilePath = "";

    private int secondsElapsed = 0;
    private final Handler timerHandler = new Handler();
    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            secondsElapsed++;
            int mins = secondsElapsed / 60;
            int secs = secondsElapsed % 60;
            txtTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", mins, secs));
            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new RecordDBHelper(this);
        rvRecords = findViewById(R.id.rv_records);
        fabRecord = findViewById(R.id.fab_record_action);
        txtTimer = findViewById(R.id.txt_timer_display);

        rvRecords.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecordAdapter(this, recordList, this);
        rvRecords.setAdapter(adapter);

        loadRecords();

        fabRecord.setOnClickListener(v -> {
            if (checkPermissions()) {
                toggleRecording();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 555);
            }
        });
    }

    private void loadRecords() {
        recordList.clear();
        recordList.addAll(dbHelper.getAllRecords());
        adapter.notifyDataSetChanged();
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void toggleRecording() {
        if (!isRecording) {
            // Khởi tạo tệp tin lưu trong bộ nhớ Cache nội bộ của ứng dụng để tránh lỗi phân quyền
            recordFilePath = getExternalCacheDir().getAbsolutePath() + "/Rec_" + System.currentTimeMillis() + ".3gp";
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(recordFilePath);

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                isRecording = true;
                secondsElapsed = 0;
                txtTimer.setText("00:00");
                timerHandler.post(timerRunnable);
                fabRecord.setImageResource(android.R.drawable.ic_media_pause);
                Toast.makeText(this, "Đang ghi âm...", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi khởi tạo Micro!", Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                isRecording = false;
                timerHandler.removeCallbacks(timerRunnable);
                fabRecord.setImageResource(android.R.drawable.ic_btn_speak_now);

                String lengthStr = txtTimer.getText().toString();
                dbHelper.insertRecord("Bản ghi âm #" + (recordList.size() + 1), recordFilePath, lengthStr);
                loadRecords();
                Toast.makeText(this, "Đã lưu bản ghi âm!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPlayClick(RecordModel model) {
        File file = new File(model.getPath());
        if (!file.exists()) {
            Toast.makeText(this, "Tệp tin âm thanh không tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            Toast.makeText(this, "Dừng phát", Toast.LENGTH_SHORT).show();
            return;
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(model.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this, "▶️ Đang nghe lại: " + model.getTitle(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi phát tệp tin!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onShareClick(RecordModel model) {
        try {
            File file = new File(model.getPath());
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("audio/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Chia sẻ âm thanh qua:"));
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi chia sẻ tệp!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleteClick(RecordModel model) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa bản ghi")
                .setMessage("Bạn chắc chắn muốn xóa tệp ghi âm này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbHelper.deleteRecord(model.getId());
                    File file = new File(model.getPath());
                    if (file.exists()) file.delete(); // Xóa file vật lý khỏi bộ nhớ
                    loadRecords();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}