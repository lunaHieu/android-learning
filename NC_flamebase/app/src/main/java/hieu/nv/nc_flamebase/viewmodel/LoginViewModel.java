package hieu.nv.nc_flamebase.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import hieu.nv.nc_flamebase.model.User;
import hieu.nv.nc_flamebase.repository.UserRepository;

public class LoginViewModel extends ViewModel {
    private final UserRepository userRepository;

    // LiveData để Activity quan sát kết quả đăng nhập
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    // LiveData để thông báo lỗi (nếu có)
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public LoginViewModel() {
        // Khởi tạo Repository
        userRepository = new UserRepository();
    }

    // Getter để Activity có thể Observe (quan sát)
    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    /**
     * Hàm xử lý logic đăng nhập
     * @param email Email người dùng nhập
     * @param password Mật khẩu người dùng nhập
     */
    // Trong LoginViewModel.java
    public LiveData<User> login(String email, String password) {
        userRepository.getUserByEmail(email, new UserRepository.UserCallback() {
            @Override
            public void onUserRetrieved(User user) {
                // Xử lý 2 giá trị tại đây
                if (user != null && user.getPassword().equals(password)) {
                    userLiveData.setValue(user); // Đúng cả 2 -> trả về user
                } else {
                    userLiveData.setValue(null); // Sai hoặc không tồn tại -> trả về null
                }
            }

            @Override
            public void onError(Exception exception) {
                userLiveData.setValue(null);
            }
        });
        return userLiveData; // Trả về LiveData để Activity "quan sát"
    }
}