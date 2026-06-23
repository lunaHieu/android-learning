package hieu.nv.ktra2_nguyenvanhieu.view.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hieu.nv.ktra2_nguyenvanhieu.R;
import hieu.nv.ktra2_nguyenvanhieu.model.Transaction;
import hieu.nv.ktra2_nguyenvanhieu.view.adapter.TransactionAdapter;
import hieu.nv.ktra2_nguyenvanhieu.viewmodel.TransactionViewModel;

import java.io.OutputStream;
import java.util.List;

public class ListFragment extends Fragment {

    private TransactionViewModel viewModel;
    private TransactionAdapter adapter;
    private String[] categories = {"Ăn uống", "Sinh hoạt", "Nhà ở", "Sức khỏe", "Di chuyển", "Giải trí", "Giáo dục", "Khác"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TransactionAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);

        viewModel.getListTransactions().observe(getViewLifecycleOwner(), list -> adapter.setList(list));
        viewModel.getMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty())
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.fabAdd).setOnClickListener(v -> showTransactionDialog(null));
        adapter.setOnItemLongClickListener(transaction -> {
        });

        registerForContextMenu(recyclerView);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_export_csv) {
            exportToCSV();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getPositionItemLongClick();
        Transaction t = adapter.getTransactionAt(position);

        if (item.getItemId() == 111) {
            showTransactionDialog(t);
            return true;
        } else if (item.getItemId() == 112) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Xóa Giao Dịch")
                    .setMessage("Bạn chắc chắn muốn xóa: " + t.getTitle() + "?")
                    .setPositiveButton("Xóa", (dialog, which) -> viewModel.delete(t.getId()))
                    .setNegativeButton("Hủy", null)
                    .show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void showTransactionDialog(Transaction currentTransaction) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_transaction, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        RadioGroup rgType = dialogView.findViewById(R.id.rgType);
        EditText etTitle = dialogView.findViewById(R.id.etTitle);
        EditText etAmount = dialogView.findViewById(R.id.etAmount);
        Spinner spCategory = dialogView.findViewById(R.id.spCategory);
        EditText etDate = dialogView.findViewById(R.id.etDate);
        EditText etNote = dialogView.findViewById(R.id.etNote);

        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(spinAdapter);

        if (currentTransaction != null) {
            builder.setTitle("Sửa Giao Dịch");
            if (currentTransaction.getType() == 2) rgType.check(R.id.rbChi);
            etTitle.setText(currentTransaction.getTitle());
            etAmount.setText(String.valueOf(currentTransaction.getAmount()));
            etDate.setText(currentTransaction.getDate());
            etNote.setText(currentTransaction.getNote());
            for (int i = 0; i < categories.length; i++) {
                if (categories[i].equals(currentTransaction.getCategory())) {
                    spCategory.setSelection(i);
                    break;
                }
            }
        } else {
            builder.setTitle("Thêm Giao Dịch");
        }

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String title = etTitle.getText().toString();
            String amountStr = etAmount.getText().toString();
            String date = etDate.getText().toString();
            String note = etNote.getText().toString();
            String category = spCategory.getSelectedItem().toString();
            int type = rgType.getCheckedRadioButtonId() == R.id.rbThu ? 1 : 2;

            if (title.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            Transaction t = new Transaction();
            t.setTitle(title);
            t.setAmount(Double.parseDouble(amountStr));
            t.setCategory(category);
            t.setDate(date);
            t.setNote(note);
            t.setType(type);

            if (currentTransaction != null) {
                t.setId(currentTransaction.getId());
                viewModel.update(t);
            } else {
                viewModel.insert(t);
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
    private void exportToCSV() {
        List<Transaction> list = viewModel.getListTransactions().getValue();
        if (list == null || list.isEmpty()) {
            Toast.makeText(getContext(), "Không có dữ liệu để xuất!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            StringBuilder csvData = new StringBuilder();
            csvData.append("ID,Loại,Tiêu đề,Số tiền,Danh mục,Ngày,Ghi chú\n");

            for (Transaction t : list) {
                String typeStr = t.getType() == 1 ? "Thu" : "Chi";
                csvData.append(t.getId()).append(",")
                        .append(typeStr).append(",")
                        .append("\"").append(t.getTitle()).append("\",")
                        .append(t.getAmount()).append(",")
                        .append(t.getCategory()).append(",")
                        .append(t.getDate()).append(",")
                        .append("\"").append(t.getNote()).append("\"\n");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, "ChiTieu_" + System.currentTimeMillis() + ".csv");
                values.put(MediaStore.MediaColumns.MIME_TYPE, "text/csv");
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/QuanLyChiTieu");

                Uri uri = requireContext().getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                if (uri != null) {
                    OutputStream out = requireContext().getContentResolver().openOutputStream(uri);
                    out.write(csvData.toString().getBytes());
                    out.close();
                    Toast.makeText(getContext(), "Đã xuất file thành công vào thư mục Downloads!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getContext(), "Tính năng xuất file yêu cầu Android 10 trở lên!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Lỗi khi xuất file!", Toast.LENGTH_SHORT).show();
        }
    }
}