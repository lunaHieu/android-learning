package com.example.vanhieu_lab04_bai1; // QUAN TRỌNG: Phải có dòng này đầu tiên

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Xử lý giao diện Edge-to-Edge (Full màn hình)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Hiển thị Fragment đầu tiên (StudentInfoFragment) khi mở app
        if (savedInstanceState == null) {
            hienThiFragment(new StudentInfoFragment());
        }
    }

    // --- CÁC HÀM HỖ TRỢ CHUYỂN FRAGMENT ---

    // Hàm dùng chung để hiển thị Fragment
    public void hienThiFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Thay thế nội dung trong FrameLayout (id: fragmentContainer) bằng fragment mới
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);

        // Lưu ý: Nếu muốn ấn nút Back để quay lại màn hình trước thì bỏ comment dòng dưới
        // fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    // Chuyển sang màn hình Nhập điểm (ScoreFragment) + Gửi kèm Mã SV, Họ tên
    public void chuyenSangScoreFragment(String maSinhVien, String hoTen) {
        ScoreFragment scoreFragment = new ScoreFragment();

        // Đóng gói dữ liệu vào Bundle
        Bundle bundle = new Bundle();
        bundle.putString("maSinhVien", maSinhVien);
        bundle.putString("hoTen", hoTen);
        scoreFragment.setArguments(bundle);

        // Hiển thị
        hienThiFragment(scoreFragment);
    }

    // Chuyển sang màn hình Kết quả (ResultFragment) + Gửi kèm toàn bộ thông tin
    public void chuyenSangResultFragment(String maSinhVien, String hoTen,
                                         double diemToan, double diemLy, double diemHoa) {
        ResultFragment resultFragment = new ResultFragment();

        // Đóng gói dữ liệu
        Bundle bundle = new Bundle();
        bundle.putString("maSinhVien", maSinhVien);
        bundle.putString("hoTen", hoTen);
        bundle.putDouble("diemToan", diemToan);
        bundle.putDouble("diemLy", diemLy);
        bundle.putDouble("diemHoa", diemHoa);
        resultFragment.setArguments(bundle);

        // Hiển thị
        hienThiFragment(resultFragment);
    }

    // Quay lại màn hình đầu tiên (Nhập thông tin)
    public void quayLaiStudentInfoFragment() {
        hienThiFragment(new StudentInfoFragment());
    }
}