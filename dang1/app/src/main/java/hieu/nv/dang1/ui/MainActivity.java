package hieu.nv.dang1.ui;

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
import hieu.nv.dang1.R;
import hieu.nv.dang1.database.ImageDBHelper;
import hieu.nv.dang1.model.ImageModel;
import hieu.nv.dang1.utils.ShareUtils;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ImageAdapter.OnImageActionListener {
    private ImageDBHelper dbHelper;
    private RecyclerView rvImages;
    private ImageAdapter adapter;
    private List<ImageModel> imageList = new ArrayList<>();
    private Spinner spinnerAlbum;
    private FloatingActionButton fabAdd;
    private int currentAlbumId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new ImageDBHelper(this);
        rvImages = findViewById(R.id.rv_images);
        spinnerAlbum = findViewById(R.id.spinner_album_filter);
        fabAdd = findViewById(R.id.fab_add_photo);

        rvImages.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new ImageAdapter(this, imageList, this);
        rvImages.setAdapter(adapter);

        setupAlbumSpinner();

        fabAdd.setOnClickListener(v -> showAddImageDialog());
    }

    private void setupAlbumSpinner() {
        List<String> albums = dbHelper.getAllAlbumNames();
        albums.add("🔒 Kho bí mật (Mã PIN)");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, albums);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlbum.setAdapter(spinnerAdapter);

        spinnerAlbum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedName = albums.get(position);
                if (selectedName.contains("Kho bí mật")) {
                    checkSecretAccess();
                } else {
                    currentAlbumId = dbHelper.getAlbumIdByName(selectedName);
                    loadImages();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadImages() {
        imageList.clear();
        imageList.addAll(dbHelper.getImagesByAlbum(currentAlbumId));
        adapter.notifyDataSetChanged();
    }

    private void checkSecretAccess() {
        EditText edtPin = new EditText(this);
        edtPin.setHint("Nhập mã PIN gồm 4 số");
        new AlertDialog.Builder(this)
                .setTitle("Xác thực Album bảo mật")
                .setView(edtPin)
                .setPositiveButton("Xác nhận", (dialog, which) -> {
                    if (edtPin.getText().toString().equals("1234")) {
                        currentAlbumId = 999; // ID tượng trưng của Kho bảo mật
                        loadImages();
                    } else {
                        Toast.makeText(MainActivity.this, "Sai mã PIN!", Toast.LENGTH_SHORT).show();
                        spinnerAlbum.setSelection(0);
                    }
                })
                .setNegativeButton("Hủy", (dialog, which) -> spinnerAlbum.setSelection(0))
                .show();
    }

    private String tempTitle = "";

    private void showAddImageDialog() {
        if (currentAlbumId == 999) {
            Toast.makeText(this, "Không thể thêm trực tiếp vào kho bảo mật!", Toast.LENGTH_SHORT).show();
            return;
        }
        EditText edtTitle = new EditText(this);
        edtTitle.setHint("Nhập tên/tiêu đề ảnh");

        new AlertDialog.Builder(this)
                .setTitle("Thêm ảnh mới")
                .setView(edtTitle)
                .setPositiveButton("Chọn ảnh từ máy", (dialog, which) -> {
                    String title = edtTitle.getText().toString().trim();
                    if (!title.isEmpty()) {
                        tempTitle = title;
                        // Mở trình chọn ảnh hệ thống, máy ảo tự động hiển thị kho ảnh có sẵn
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, 111);
                    } else {
                        Toast.makeText(MainActivity.this, "Vui lòng nhập tên ảnh!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            String pathString = imageUri.toString(); // Hệ thống tự sinh chuỗi Uri (ví dụ: content://media/external/images/media/1)

            dbHelper.insertImage(tempTitle, pathString, currentAlbumId);
            loadImages();
            Toast.makeText(this, "Đã thêm ảnh vào Album thành công!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onShareClick(ImageModel model) {
        ShareUtils.shareImageViaIntent(this, model.getPath());
    }

    @Override
    public void onDeleteClick(ImageModel model) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn chắc chắn muốn xóa ảnh này chứ?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbHelper.deleteImage(model.getId());
                    loadImages();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onUpdateClick(ImageModel model) {
        EditText edtNewTitle = new EditText(this);
        edtNewTitle.setText(model.getTitle());
        new AlertDialog.Builder(this)
                .setTitle("Sửa tên ảnh")
                .setView(edtNewTitle)
                .setPositiveButton("Cập nhật", (dialog, which) -> {
                    String newTitle = edtNewTitle.getText().toString().trim();
                    if (!newTitle.isEmpty()) {
                        dbHelper.updateImage(model.getId(), newTitle);
                        loadImages();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}