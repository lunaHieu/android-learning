package hieu.nv.kiemtra3.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import hieu.nv.kiemtra3.R;
import hieu.nv.kiemtra3.adapter.PublisherAdapter;
import hieu.nv.kiemtra3.database.DatabaseHelper;
import hieu.nv.kiemtra3.model.Publisher;

public class PublisherActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ListView lvPublishers;
    private Button btnAddPublisher;
    private List<Publisher> publisherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publisher);

        dbHelper = new DatabaseHelper(this);
        lvPublishers = findViewById(R.id.lvPublishers);
        btnAddPublisher = findViewById(R.id.btnAddPublisher);

        loadPublishersData();

        btnAddPublisher.setOnClickListener(v -> showPublisherDialog(null));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Trở về");
        }
    }

    private void loadPublishersData() {
        List<Publisher> allData = dbHelper.getAllPublishers();
        publisherList = new ArrayList<>();

        for (Publisher p : allData) {
            if (p.id != 0) {
                publisherList.add(p);
            }
        }

        PublisherAdapter adapter = new PublisherAdapter(this, publisherList, new PublisherAdapter.PublisherActionListener() {
            @Override
            public void onEdit(Publisher publisher) {
                showPublisherDialog(publisher);
            }

            @Override
            public void onDelete(int publisherId) {
                new AlertDialog.Builder(PublisherActivity.this)
                        .setTitle("Xóa Nhà xuất bản")
                        .setMessage("Bạn có chắc muốn xóa? Sách thuộc NXB này sẽ mất liên kết!")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            dbHelper.deletePublisher(publisherId);
                            loadPublishersData();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });
        lvPublishers.setAdapter(adapter);
    }

    private void showPublisherDialog(Publisher pubToEdit) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_publisher);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText edtPubName = dialog.findViewById(R.id.edtPubName);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnSave = dialog.findViewById(R.id.btnSave);

        if (pubToEdit != null) {
            edtPubName.setText(pubToEdit.name);
        }

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            String name = edtPubName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Nhập tên NXB", Toast.LENGTH_SHORT).show();
                return;
            }

            if (pubToEdit == null) {
                dbHelper.addPublisher(name);
            } else {
                dbHelper.updatePublisher(pubToEdit.id, name);
            }
            loadPublishersData();
            dialog.dismiss();
        });

        dialog.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}