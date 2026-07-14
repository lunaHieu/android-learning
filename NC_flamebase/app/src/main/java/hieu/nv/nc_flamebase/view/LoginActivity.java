package hieu.nv.nc_flamebase.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import hieu.nv.nc_flamebase.R;
import hieu.nv.nc_flamebase.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewForgotPassword;
    private ProgressBar progressBarLogin;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        progressBarLogin = findViewById(R.id.progressBarLogin);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        buttonLogin.setOnClickListener(v -> handleLogin());

        textViewForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void handleLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBarLogin.setVisibility(View.VISIBLE);
        buttonLogin.setEnabled(false);

        // Gọi ViewModel xử lý 2 giá trị
        loginViewModel.login(email, password).observe(this, user -> {
            progressBarLogin.setVisibility(View.GONE);
            buttonLogin.setEnabled(true);

            if (user != null) {
                // Đăng nhập thành công (vì ViewModel đã check pass rồi)
                navigateToRoleBasedActivity(user);
            } else {
                // Nếu user null nghĩa là sai email hoặc sai pass
                Toast.makeText(this, "Tài khoản hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // SỬA TẠI ĐÂY: Phải là (User user) chứ không phải (Object user)
    private void navigateToRoleBasedActivity(hieu.nv.nc_flamebase.model.User user) {
        Intent intent;
        if ("admin".equals(user.getRole())) {
            intent = new Intent(this, AdminActivity.class);
            Toast.makeText(this, "Chào mừng Admin", Toast.LENGTH_SHORT).show();
        } else {
            intent = new Intent(this, CustomerActivity.class);
            Toast.makeText(this, "Chào mừng Khách hàng", Toast.LENGTH_SHORT).show();
        }
        startActivity(intent);
        finish();
    }
}