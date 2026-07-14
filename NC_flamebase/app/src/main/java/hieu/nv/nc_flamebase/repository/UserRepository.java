package hieu.nv.nc_flamebase.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hieu.nv.nc_flamebase.model.User;

public class UserRepository {
    private final DatabaseReference database;

    public UserRepository() {
        // Tham chiếu đến nút "users" trên Firebase
        database = FirebaseDatabase.getInstance().getReference("users");
    }

    /**
     * Truy vấn người dùng dựa trên email.
     * @param email Email người dùng cần tìm.
     * @param callback Callback để xử lý kết quả trả về.
     */
    public void getUserByEmail(String email, UserCallback callback) {
        // Sử dụng query để lọc theo email
        database.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = null;
                        // Kiểm tra nếu có dữ liệu trả về
                        if (snapshot.exists()) {
                            // Firebase trả về một danh sách (dù chỉ có 1 kết quả), nên cần loop
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                user = userSnapshot.getValue(User.class);
                                break; // Lấy người dùng đầu tiên khớp email và dừng
                            }
                        }
                        // Trả kết quả về cho ViewModel hoặc Activity qua interface
                        callback.onUserRetrieved(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("UserRepository", "Lỗi truy vấn: " + error.getMessage());
                        callback.onError(error.toException());
                    }
                });
    }

    /**
     * Giao diện callback để xử lý kết quả bất đồng bộ từ Firebase.
     */
    public interface UserCallback {
        void onUserRetrieved(User user);   // Thành công (user có thể null nếu không tìm thấy)
        void onError(Exception exception); // Thất bại
    }
}