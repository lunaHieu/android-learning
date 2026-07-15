package hieu.nv.nguyenvanhieu_128.ui;

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
import hieu.nv.nguyenvanhieu_128.R;
import hieu.nv.nguyenvanhieu_128.database.TopicDBHelper;
import hieu.nv.nguyenvanhieu_128.model.TopicModel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TopicAdapter.OnTopicActionListener {
    private TopicDBHelper dbHelper;
    private RecyclerView rv;
    private TopicAdapter adapter;
    private List<TopicModel> topicList = new ArrayList<>();

    private EditText edtSearch;
    private Button btnSearch, btnOpenAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new TopicDBHelper(this);
        rv = findViewById(R.id.rv_topics);
        edtSearch = findViewById(R.id.edt_search_keyword);
        btnSearch = findViewById(R.id.btn_search_topic);
        btnOpenAdd = findViewById(R.id.btn_open_add_dialog);

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TopicAdapter(topicList, this, this);
        rv.setAdapter(adapter);

        loadAllData();

        btnOpenAdd.setOnClickListener(v -> showTopicDialog(null));

        btnSearch.setOnClickListener(v -> {
            String kw = edtSearch.getText().toString().trim();
            if (kw.isEmpty()) {
                loadAllData();
            } else {
                topicList.clear();
                topicList.addAll(dbHelper.searchTopics(kw));
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void loadAllData() {
        topicList.clear();
        topicList.addAll(dbHelper.getAllTopics());
        adapter.notifyDataSetChanged();
    }

    private void showTopicDialog(TopicModel existingModel) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_topic, null);
        EditText edtId = view.findViewById(R.id.edt_dlg_id);
        EditText edtTitle = view.findViewById(R.id.edt_dlg_title);
        EditText edtHost = view.findViewById(R.id.edt_dlg_host);
        EditText edtBudget = view.findViewById(R.id.edt_dlg_budget);
        EditText edtYear = view.findViewById(R.id.edt_dlg_year);
        EditText edtResult = view.findViewById(R.id.edt_dlg_result);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (existingModel != null) {
            builder.setTitle("Cập nhật đề tài khoa học");
            edtId.setText(existingModel.getId());
            edtId.setEnabled(false);
            edtTitle.setText(existingModel.getTitle());
            edtHost.setText(existingModel.getHost());
            edtBudget.setText(String.valueOf(existingModel.getBudget()));
            edtYear.setText(String.valueOf(existingModel.getYear()));
            edtResult.setText(existingModel.getResult());

            builder.setNeutralButton("Xóa đề tài này", (dialog, which) -> {
                dbHelper.deleteTopic(existingModel.getId());
                loadAllData();
                Toast.makeText(MainActivity.this, "Đã xóa thành công!", Toast.LENGTH_SHORT).show();
            });
        } else {
            builder.setTitle("Thêm đề tài nghiên cứu mới");
        }

        builder.setView(view)
                .setPositiveButton("Lưu Lại", (dialog, which) -> {
                    String id = edtId.getText().toString().trim();
                    String title = edtTitle.getText().toString().trim();
                    String host = edtHost.getText().toString().trim();
                    String budgetStr = edtBudget.getText().toString().trim();
                    String yearStr = edtYear.getText().toString().trim();
                    String result = edtResult.getText().toString().trim();

                    if (!id.isEmpty() && !title.isEmpty() && !host.isEmpty()) {
                        double budget = budgetStr.isEmpty() ? 0 : Double.parseDouble(budgetStr);
                        int year = yearStr.isEmpty() ? 2026 : Integer.parseInt(yearStr);
                        String resVal = result.isEmpty() ? "Chưa nghiệm thu" : result;

                        if (existingModel != null) {
                            dbHelper.updateTopic(id, title, host, budget, year, resVal);
                        } else {
                            dbHelper.insertTopic(id, title, host, budget, year, resVal);
                        }
                        loadAllData();
                    } else {
                        Toast.makeText(MainActivity.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onTopicClick(TopicModel model) {
        showTopicDialog(model);
    }
}