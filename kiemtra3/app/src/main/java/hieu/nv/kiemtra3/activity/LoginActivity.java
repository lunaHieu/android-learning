package hieu.nv.kiemtra3.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import hieu.nv.kiemtra3.R;
import hieu.nv.kiemtra3.database.DatabaseHelper;
import hieu.nv.kiemtra3.model.User;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        dbHelper = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = dbHelper.checkLogin(username, password);

            if (user != null) {
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                prefs.edit().putString("ROLE", user.role).apply();

                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, BookActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}