package com.example.kiemtra_nguyenvanhieu.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiemtra_nguyenvanhieu.R;
import com.example.kiemtra_nguyenvanhieu.model.Task;
import com.example.kiemtra_nguyenvanhieu.viewmodel.SharedTaskViewModel;

import java.util.ArrayList;
import java.util.List;

public class TaskListFragment extends Fragment {
    private SharedTaskViewModel viewModel;
    private TaskAdapter adapter;
    private TextView tvCompletedCount;
    private EditText etSearch;
    private Spinner spinnerFilter;

    private String currentQuery = "";
    private String currentCategory = "Tất cả";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewTasks);
        tvCompletedCount = view.findViewById(R.id.tvCompletedCount);
        etSearch = view.findViewById(R.id.etSearch);
        spinnerFilter = view.findViewById(R.id.spinnerFilter);

        String[] filterCategories = {"Tất cả", "Học tập", "Cá nhân", "Công việc", "Khác"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, filterCategories);
        spinnerFilter.setAdapter(spinnerAdapter);

        adapter = new TaskAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedTaskViewModel.class);

        viewModel.getTaskList().observe(getViewLifecycleOwner(), tasks -> {
            adapter.setTasks(tasks);
            int countCompleted = 0;
            for (Task task : tasks) {
                if (task.getTrangThai() == 1) countCompleted++;
            }
            tvCompletedCount.setText("Đã hoàn thành: " + countCompleted + " / " + tasks.size());
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentQuery = s.toString();
                viewModel.filterTasks(currentQuery, currentCategory);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCategory = filterCategories[position];
                viewModel.filterTasks(currentQuery, currentCategory);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }
    class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
        private List<Task> tasks;
        public TaskAdapter(List<Task> tasks) { this.tasks = tasks; }
        public void setTasks(List<Task> tasks) {
            this.tasks = tasks;
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
            return new TaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
            Task task = tasks.get(position);
            holder.tvTitle.setText(task.getTieuDe());
            holder.tvCategoryAndDate.setText(task.getLoaiCongViec() + " | " + task.getNgayTao());

            holder.cbStatus.setOnCheckedChangeListener(null);
            holder.cbStatus.setChecked(task.getTrangThai() == 1);

            holder.cbStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
                task.setTrangThai(isChecked ? 1 : 0);
                viewModel.updateTask(task);
            });

            holder.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Xác nhận").setMessage("Bạn có chắc muốn xóa?")
                        .setPositiveButton("Xóa", (dialog, which) -> viewModel.deleteTask(task.getMa()))
                        .setNegativeButton("Hủy", null).show();
            });

            holder.itemView.setOnClickListener(v -> viewModel.requestEditTask(task));
        }

        @Override
        public int getItemCount() { return tasks.size(); }

        class TaskViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvCategoryAndDate;
            CheckBox cbStatus;
            ImageButton btnDelete;

            public TaskViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvCategoryAndDate = itemView.findViewById(R.id.tvCategoryAndDate);
                cbStatus = itemView.findViewById(R.id.cbStatus);
                btnDelete = itemView.findViewById(R.id.btnDelete);
            }
        }
    }
}