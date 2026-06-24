package com.example.kiemtra_nguyenvanhieu.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.kiemtra_nguyenvanhieu.database.DatabaseHelper;
import com.example.kiemtra_nguyenvanhieu.model.Task;

import java.util.ArrayList;
import java.util.List;

public class SharedTaskViewModel extends AndroidViewModel {
    private DatabaseHelper dbHelper;
    private MutableLiveData<List<Task>> taskList = new MutableLiveData<>();
    private MutableLiveData<Task> taskToEdit = new MutableLiveData<>();
    private MutableLiveData<Integer> navigateToTab = new MutableLiveData<>();

    private String currentSearchQuery = "";
    private String currentFilterCategory = "Tất cả";

    public SharedTaskViewModel(@NonNull Application application) {
        super(application);
        dbHelper = new DatabaseHelper(application);
        loadTasks();
    }

    public MutableLiveData<List<Task>> getTaskList() { return taskList; }
    public MutableLiveData<Task> getTaskToEdit() { return taskToEdit; }
    public MutableLiveData<Integer> getNavigateToTab() { return navigateToTab; }

    public void loadTasks() {
        List<Task> allTasks = dbHelper.getAllTasks();
        List<Task> filteredTasks = new ArrayList<>();

        for (Task task : allTasks) {
            boolean matchCategory = currentFilterCategory.equals("Tất cả") || task.getLoaiCongViec().equals(currentFilterCategory);
            boolean matchQuery = currentSearchQuery.isEmpty() || task.getTieuDe().toLowerCase().contains(currentSearchQuery.toLowerCase());

            if (matchCategory && matchQuery) {
                filteredTasks.add(task);
            }
        }
        taskList.setValue(filteredTasks);
    }

    public void filterTasks(String query, String category) {
        this.currentSearchQuery = query;
        this.currentFilterCategory = category;
        loadTasks();
    }

    public void addTask(Task task) {
        dbHelper.insertTask(task);
        loadTasks();
    }

    public void updateTask(Task task) {
        dbHelper.updateTask(task);
        loadTasks();
    }

    public void deleteTask(int taskId) {
        dbHelper.deleteTask(taskId);
        loadTasks();
    }

    public void requestEditTask(Task task) {
        taskToEdit.setValue(task);
        navigateToTab.setValue(1);
    }
}