package hieu.nv.dang4.ui;

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
import hieu.nv.dang4.R;
import hieu.nv.dang4.database.ContactDBHelper;
import hieu.nv.dang4.model.ContactModel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ContactDBHelper dbHelper;
    private RecyclerView rvContacts;
    private ContactAdapter adapter;
    private List<ContactModel> contactList = new ArrayList<>();
    private Button btnMerge, btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new ContactDBHelper(this);
        rvContacts = findViewById(R.id.rv_contacts);
        btnMerge = findViewById(R.id.btn_merge_duplicates);
        btnAdd = findViewById(R.id.btn_open_add_dialog);

        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactAdapter(this, contactList);
        rvContacts.setAdapter(adapter);

        loadContacts();

        btnAdd.setOnClickListener(v -> showAddContactDialog());

        // Kích hoạt tính năng gộp số điện thoại trùng lặp
        btnMerge.setOnClickListener(v -> {
            int count = dbHelper.mergeDuplicateContacts();
            loadContacts();
            if (count > 0) {
                Toast.makeText(MainActivity.this, "Đã hợp nhất thành công " + count + " liên lạc trùng số!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Không phát hiện số trùng lặp!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadContacts() {
        contactList.clear();
        contactList.addAll(dbHelper.getAllContactsAlphabet());
        adapter.notifyDataSetChanged();
    }

    private void showAddContactDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_contact, null);
        EditText edtName = view.findViewById(R.id.edt_name);
        EditText edtPhone = view.findViewById(R.id.edt_phone);
        EditText edtEmail = view.findViewById(R.id.edt_email);
        EditText edtGroup = view.findViewById(R.id.edt_group);

        new AlertDialog.Builder(this)
                .setTitle("Thêm danh bạ mới")
                .setView(view)
                .setPositiveButton("Lưu lại", (dialog, which) -> {
                    String name = edtName.getText().toString().trim();
                    String phone = edtPhone.getText().toString().trim();
                    String email = edtEmail.getText().toString().trim();
                    String group = edtGroup.getText().toString().trim();

                    if (!name.isEmpty() && !phone.isEmpty()) {
                        dbHelper.insertContact(name, phone, email.isEmpty() ? "Không có" : email, group.isEmpty() ? "Mặc định" : group);
                        loadContacts();
                    } else {
                        Toast.makeText(MainActivity.this, "Tên và Số điện thoại không được trống!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}