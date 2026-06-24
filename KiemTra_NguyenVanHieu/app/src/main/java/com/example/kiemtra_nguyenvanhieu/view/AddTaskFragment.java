package com.example.kiemtra_nguyenvanhieu.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.kiemtra_nguyenvanhieu.R;
import com.example.kiemtra_nguyenvanhieu.model.Task;
import com.example.kiemtra_nguyenvanhieu.viewmodel.SharedTaskViewModel;


import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class AddTaskFragment extends Fragment {
    private SharedTaskViewModel viewModel;
    private EditText etTitle, etDescription;
    private Spinner spinnerCategory;
    private Button btnSave;
    private Task currentEditingTask = null; // Biến lưu task đang sửa (nếu có)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);

        etTitle = view.findViewById(R.id.etTitle);
        etDescription = view.findViewById(R.id.etDescription);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        btnSave = view.findViewById(R.id.btnSave);

        // Setup Spinner
        String[] categories = {"Học tập", "Cá nhân", "Công việc", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedTaskViewModel.class);

        // Lắng nghe xem có task nào cần sửa được gửi tới không
        viewModel.getTaskToEdit().observe(getViewLifecycleOwner(), task -> {
            if (task != null) {
                currentEditingTask = task;
                etTitle.setText(task.getTieuDe());
                etDescription.setText(task.getMoTa());
                btnSave.setText("Cập nhật công việc");
                int spinnerPosition = adapter.getPosition(task.getLoaiCongViec());
                spinnerCategory.setSelection(spinnerPosition);
            }
        });

        btnSave.setOnClickListener(v -> saveTask());

        return view;
    }

    private void saveTask() {
        String title = etTitle.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập tiêu đề!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentEditingTask == null) {
            // Thêm mới
            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            Task newTask = new Task(title, desc, category, date, 0);
            viewModel.addTask(newTask);
            Toast.makeText(getContext(), "Đã thêm công việc!", Toast.LENGTH_SHORT).show();
        } else {
            // Cập nhật
            currentEditingTask.setTieuDe(title);
            currentEditingTask.setMoTa(desc);
            currentEditingTask.setLoaiCongViec(category);
            viewModel.updateTask(currentEditingTask);
            Toast.makeText(getContext(), "Đã cập nhật công việc!", Toast.LENGTH_SHORT).show();
            currentEditingTask = null; // Xóa trạng thái edit
            btnSave.setText("Lưu công việc");
        }

        // Reset form và ra lệnh chuyển về tab danh sách
        etTitle.setText("");
        etDescription.setText("");
        viewModel.getTaskToEdit().setValue(null); // Reset event
        viewModel.getNavigateToTab().setValue(0);
    }
}