package hieu.nv.nc_flamebase;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import hieu.nv.nc_flamebase.view.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bỏ dòng setContentView đi vì mình không cần hiển thị giao diện mặc định nữa
        // setContentView(R.layout.activity_main);

        // Tạo Intent để chuyển hướng ngay lập tức sang màn hình Đăng nhập
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

        // Gọi finish() để đóng MainActivity lại.
        // Nhờ vậy, khi ở màn hình Login mà người dùng bấm nút Back, App sẽ thoát luôn chứ không quay lại màn hình trắng nữa.
        finish();
    }
}