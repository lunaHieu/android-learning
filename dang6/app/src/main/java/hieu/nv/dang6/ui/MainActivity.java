package hieu.nv.dang6.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hieu.nv.dang6.R;
import hieu.nv.dang6.database.ExpenseDBHelper;
import hieu.nv.dang6.model.ExpenseModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ExpenseDBHelper dbHelper;
    private RecyclerView rvTrans;
    private ExpenseAdapter adapter;
    private List<ExpenseModel> transList = new ArrayList<>();

    private EditText edtTitle, edtAmount;
    private Button btnIncome, btnExpense, btnOpenSaving;
    private View barIncome, barExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new ExpenseDBHelper(this);
        edtTitle = findViewById(R.id.edt_finance_title);
        edtAmount = findViewById(R.id.edt_finance_amount);
        btnIncome = findViewById(R.id.btn_add_income);
        btnExpense = findViewById(R.id.btn_add_expense);
        btnOpenSaving = findViewById(R.id.btn_open_saving_dialog);
        barIncome = findViewById(R.id.view_bar_income);
        barExpense = findViewById(R.id.view_bar_expense);
        rvTrans = findViewById(R.id.rv_transactions);

        rvTrans.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExpenseAdapter(transList);
        rvTrans.setAdapter(adapter);

        refreshUI();

        btnIncome.setOnClickListener(v -> addTransaction("THU"));
        btnExpense.setOnClickListener(v -> addTransaction("CHI"));
        btnOpenSaving.setOnClickListener(v -> showSavingDialog());
    }

    private void addTransaction(String type) {
        String title = edtTitle.getText().toString().trim();
        String amountStr = edtAmount.getText().toString().trim();

        if (!title.isEmpty() && !amountStr.isEmpty()) {
            dbHelper.insertTransaction(title, Double.parseDouble(amountStr), type);
            edtTitle.setText("");
            edtAmount.setText("");
            refreshUI();
        } else {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
        }
    }

    // --- 🔥 LOGIC 1: THUẬT TOÁN TỰ CO DÃN THANH BIỂU ĐỒ THEO % DỮ LIỆU ---
    private void refreshUI() {
        transList.clear();
        transList.addAll(dbHelper.getAllTransactions());
        adapter.notifyDataSetChanged();

        double totalIncome = dbHelper.getTotalAmountByType("THU");
        double totalExpense = dbHelper.getTotalAmountByType("CHI");
        double sum = totalIncome + totalExpense;

        if (sum > 0) {
            float incomePercent = (float) ((totalIncome / sum) * 100);
            float expensePercent = (float) ((totalExpense / sum) * 100);

            // Cập nhật lại độ dài (weight) của 2 thanh chữ nhật XML dựa trên % thực tế
            LinearLayout.LayoutParams pIncome = (LinearLayout.LayoutParams) barIncome.getLayoutParams();
            pIncome.weight = incomePercent;
            barIncome.setLayoutParams(pIncome);

            LinearLayout.LayoutParams pExpense = (LinearLayout.LayoutParams) barExpense.getLayoutParams();
            pExpense.weight = expensePercent;
            barExpense.setLayoutParams(pExpense);
        } else {
            // Nếu chưa có data, chia đều mỗi thanh 50%
            ((LinearLayout.LayoutParams) barIncome.getLayoutParams()).weight = 50;
            ((LinearLayout.LayoutParams) barExpense.getLayoutParams()).weight = 50;
        }
    }

    // --- 🔥 LOGIC 2: ĐỐI THOẠI TÍNH LÃI SUẤT TIẾT KIỆM NGÂN HÀNG ---
    private void showSavingDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_saving, null);
        EditText edtBank = view.findViewById(R.id.edt_bank_name);
        EditText edtCapital = view.findViewById(R.id.edt_saving_capital);
        EditText edtRate = view.findViewById(R.id.edt_saving_rate);
        EditText edtMonths = view.findViewById(R.id.edt_saving_months);

        new AlertDialog.Builder(this)
                .setTitle("Mở sổ tiết kiệm mới")
                .setView(view)
                .setPositiveButton("Tính toán & Lưu", (dialog, which) -> {
                    String bank = edtBank.getText().toString().trim();
                    String capStr = edtCapital.getText().toString().trim();
                    String rateStr = edtRate.getText().toString().trim();
                    String monthStr = edtMonths.getText().toString().trim();

                    if (!bank.isEmpty() && !capStr.isEmpty() && !rateStr.isEmpty() && !monthStr.isEmpty()) {
                        double capital = Double.parseDouble(capStr);
                        double annualRate = Double.parseDouble(rateStr) / 100; // Đổi 6.5% thành 0.065
                        int months = Integer.parseInt(monthStr);

                        // Áp dụng công thức tính toán lãi kép vạn năng
                        double finalAmount = capital * Math.pow(1 + (annualRate / 12), months);
                        double interestEarned = finalAmount - capital;

                        dbHelper.insertSaving(bank, capital, annualRate, months);

                        // Hiển thị hộp thoại kết quả lãi suất trực quan cho giảng viên xem
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("💰 KẾT QUẢ DỰ TÍNH LÃI")
                                .setMessage("Ngân hàng: " + bank + "\n" +
                                        "Tiền gốc: " + capStr + "đ\n" +
                                        "Tổng tiền nhận lại sau " + months + " tháng: \n" +
                                        String.format(Locale.getDefault(), "%,.2f", finalAmount) + "đ\n" +
                                        "Trong đó tiền lãi thuần: " + String.format(Locale.getDefault(), "%,.2f", interestEarned) + "đ")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}