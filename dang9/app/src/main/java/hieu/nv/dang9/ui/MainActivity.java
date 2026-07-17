package hieu.nv.dang9.ui;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import hieu.nv.dang9.R;
import hieu.nv.dang9.database.MedicineDBHelper;
import hieu.nv.dang9.model.MedicineModel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MedicineAdapter.OnMedicineDeleteClickListener {
    private MedicineDBHelper dbHelper;
    private RecyclerView rvMeds;
    private MedicineAdapter adapter;
    private List<MedicineModel> medList = new ArrayList<>();
    private FloatingActionButton fabAdd;
    private EditText edtSearchCode;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MedicineDBHelper(this);
        rvMeds = findViewById(R.id.rv_medicines);
        fabAdd = findViewById(R.id.fab_add_medicine);
        edtSearchCode = findViewById(R.id.edt_search_code);
        btnSearch = findViewById(R.id.btn_search);

        rvMeds.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MedicineAdapter(medList, this);
        rvMeds.setAdapter(adapter);

        loadMedicines();

        fabAdd.setOnClickListener(v -> showAddMedicineDialog());

        // 🔥 THỰC HIỆN CHỨC NĂNG: TRUY XUẤT THÔNG TIN MẶT HÀNG THEO MÃ
        btnSearch.setOnClickListener(v -> {
            String code = edtSearchCode.getText().toString().trim();
            if (code.isEmpty()) {
                loadMedicines(); // Nếu để trống thì hiển thị lại toàn bộ danh sách sắp xếp theo hạn dùng
                return;
            }
            MedicineModel result = dbHelper.getMedicineByCode(code);
            if (result != null) {
                medList.clear();
                medList.add(result);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Đã tìm thấy mặt hàng!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Không tồn tại mã mặt hàng này!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMedicines() {
        medList.clear();
        medList.addAll(dbHelper.getAllMedicines());
        adapter.notifyDataSetChanged();
    }

    private void showAddMedicineDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_medicine, null);
        EditText edtCode = view.findViewById(R.id.edt_med_code);
        EditText edtName = view.findViewById(R.id.edt_med_name);
        EditText edtExpiry = view.findViewById(R.id.edt_med_expiry);
        EditText edtLoc = view.findViewById(R.id.edt_med_location);
        EditText edtQty = view.findViewById(R.id.edt_med_qty);

        new AlertDialog.Builder(this)
                .setTitle("Nhập thông tin mặt hàng")
                .setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String code = edtCode.getText().toString().trim();
                    String name = edtName.getText().toString().trim();
                    String expiry = edtExpiry.getText().toString().trim();
                    String loc = edtLoc.getText().toString().trim();
                    String qty = edtQty.getText().toString().trim();

                    if (!code.isEmpty() && !name.isEmpty() && !expiry.isEmpty()) {
                        dbHelper.insertMedicine(name, expiry, Integer.parseInt(qty.isEmpty()?"1":qty), loc, code);
                        loadMedicines();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onDeleteClick(MedicineModel model) {
        dbHelper.deleteMedicine(model.getId());
        loadMedicines();
    }
}