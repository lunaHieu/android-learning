package com.example.lab07_nguyenvanhieu.viewmodel;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lab07_nguyenvanhieu.model.StudentRepository;
import com.example.lab07_nguyenvanhieu.model.Student;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

    public class StudentViewModel extends AndroidViewModel {
        private StudentRepository repository;
        private MutableLiveData<List<Student>> studentListLiveData;
        private MutableLiveData<String> messageLiveData;
        private ExecutorService executorService;

        public StudentViewModel(@NonNull Application application) {
            super(application);
            repository = new StudentRepository(application);
            studentListLiveData = new MutableLiveData<>();
            messageLiveData = new MutableLiveData<>();
            executorService = Executors.newSingleThreadExecutor();

            // Load dữ liệu ban đầu
            loadAllStudents();
        }

        // Lấy LiveData của danh sách sinh viên
        public LiveData<List<Student>> getStudentListLiveData() {
            return studentListLiveData;
        }

        // Lấy LiveData của message
        public LiveData<String> getMessageLiveData() {
            return messageLiveData;
        }

        // Load tất cả sinh viên
        public void loadAllStudents() {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    List<Student> students = repository.getAllStudents();
                    studentListLiveData.postValue(students);
                }
            });
        }

        // Thêm sinh viên mới
        public void insertStudent(final Student student) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    long result = repository.insertStudent(student);
                    if (result > 0) {
                        messageLiveData.postValue("Thêm sinh viên thành công!");
                        loadAllStudents(); // Refresh danh sách
                    } else {
                        messageLiveData.postValue("Thêm sinh viên thất bại! Mã sinh viên có thể đã tồn tại.");
                    }
                }
            });
        }

        // Cập nhật sinh viên
        public void updateStudent(final Student student) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    int result = repository.updateStudent(student);
                    if (result > 0) {
                        messageLiveData.postValue("Cập nhật sinh viên thành công!");
                        loadAllStudents(); // Refresh danh sách
                    } else {
                        messageLiveData.postValue("Cập nhật sinh viên thất bại!");
                    }
                }
            });
        }

        // Xóa sinh viên
        public void deleteStudent(final int studentId) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    int result = repository.deleteStudent(studentId);
                    if (result > 0) {
                        messageLiveData.postValue("Xóa sinh viên thành công!");
                        loadAllStudents(); // Refresh danh sách
                    } else {
                        messageLiveData.postValue("Xóa sinh viên thất bại!");
                    }
                }
            });
        }

        // Tìm kiếm sinh viên theo tên
        public void searchStudentByName(final String name) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    List<Student> students;
                    if (name == null || name.trim().isEmpty()) {
                        students = repository.getAllStudents();
                    } else {
                        students = repository.searchStudentByName(name);
                    }
                    studentListLiveData.postValue(students);
                }
            });
        }

        @Override
        protected void onCleared() {
            super.onCleared();
            if (executorService != null && !executorService.isShutdown()) {
                executorService.shutdown();
            }
        }
    }
