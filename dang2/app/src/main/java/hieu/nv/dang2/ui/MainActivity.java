package hieu.nv.dang2.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import hieu.nv.dang2.R;
import hieu.nv.dang2.database.VideoDBHelper;
import hieu.nv.dang2.model.VideoModel;
import hieu.nv.dang2.utils.VideoShareUtils;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements VideoAdapter.OnVideoActionListener {
    private VideoDBHelper dbHelper;
    private RecyclerView rvVideos;
    private VideoAdapter adapter;
    private List<VideoModel> videoList = new ArrayList<>();
    private Spinner spinnerSort;
    private FloatingActionButton fabAdd;
    private String currentSortType = "DATE";

    private String tempVideoTitle = "";
    private String tempVideoCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new VideoDBHelper(this);
        rvVideos = findViewById(R.id.rv_videos);
        spinnerSort = findViewById(R.id.spinner_sort_video);
        fabAdd = findViewById(R.id.fab_add_video);

        rvVideos.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new VideoAdapter(this, videoList, this);
        rvVideos.setAdapter(adapter);

        setupSortSpinner();
        fabAdd.setOnClickListener(v -> showAddVideoDialog());
    }

    private void setupSortSpinner() {
        String[] criteria = {"Mới thêm vào", "Tên chữ cái (A-Z)"};
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, criteria);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(sortAdapter);

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSortType = (position == 0) ? "DATE" : "NAME";
                loadVideos();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadVideos() {
        videoList.clear();
        videoList.addAll(dbHelper.getAllVideos(currentSortType));
        adapter.notifyDataSetChanged();
    }

    private void showAddVideoDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_video, null);
        EditText edtTitle = view.findViewById(R.id.edt_video_title);
        EditText edtCat = view.findViewById(R.id.edt_video_category);

        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Chọn Video", (dialog, which) -> {
                    String title = edtTitle.getText().toString().trim();
                    String cat = edtCat.getText().toString().trim();
                    if (!title.isEmpty()) {
                        tempVideoTitle = title;
                        tempVideoCategory = cat.isEmpty() ? "Chưa phân loại" : cat;

                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("video/*");
                        startActivityForResult(intent, 222);
                    } else {
                        Toast.makeText(MainActivity.this, "Vui lòng điền tên video!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 222 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri videoUri = data.getData();
            dbHelper.insertVideo(tempVideoTitle, videoUri.toString(), tempVideoCategory);
            loadVideos();
            Toast.makeText(this, "Thêm video thành công!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onShareVideo(VideoModel model) {
        VideoShareUtils.shareVideoViaIntent(this, model.getPath());
    }

    @Override
    public void onDeleteVideo(VideoModel model) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa Video")
                .setMessage("Bạn muốn gỡ video này khỏi danh sách?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbHelper.deleteVideo(model.getId());
                    loadVideos();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onUpdateVideo(VideoModel model) {
        EditText edtTitle = new EditText(this);
        edtTitle.setText(model.getTitle());
        new AlertDialog.Builder(this)
                .setTitle("Sửa tên video")
                .setView(edtTitle)
                .setPositiveButton("Cập nhật", (dialog, which) -> {
                    String title = edtTitle.getText().toString().trim();
                    if (!title.isEmpty()) {
                        dbHelper.updateVideo(model.getId(), title, model.getCategory());
                        loadVideos();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // --- 💡 TÍNH NĂNG TÙY CHỌN ĂN ĐIỂM CỘNG 10: XEM VIDEO TRỰC TIẾP QUA INTENT VIEW ---
    @Override
    public void onPlayVideo(VideoModel model) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(model.getPath()), "video/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Không tìm thấy ứng dụng xem video phù hợp!", Toast.LENGTH_SHORT).show();
        }
    }
}