package com.example.vanhieu_lab02;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTitle, textViewResult, textViewInstruction;
    private Button buttonClick, buttonSubmit, buttonStartProgress;
    private ImageView imageViewDemo;
    private EditText editTextName;
    private CheckBox checkBoxJava, checkBoxKotlin, checkBoxFlutter;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonMale, radioButtonFemale;
    private ProgressBar progressBarLoading;
    private LinearLayout layoutResult;

    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeView();
        setupEventListeners();
    }

    private void initializeView() {
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewResult = findViewById(R.id.textViewResult);
        textViewInstruction = findViewById(R.id.textViewInstruction);

        // Ánh xạ layout chứa kết quả
        layoutResult = findViewById(R.id.layoutResult);

        buttonClick = findViewById(R.id.buttonClick);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonStartProgress = findViewById(R.id.buttonStartProgress);

        imageViewDemo = findViewById(R.id.imageViewDemo);

        editTextName = findViewById(R.id.editTextName);

        checkBoxJava = findViewById(R.id.checkBoxJava);
        checkBoxKotlin = findViewById(R.id.checkBoxkotlin);
        checkBoxFlutter = findViewById(R.id.checkBoxFlutter);

        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);

        progressBarLoading = findViewById(R.id.progressBarLoading);
    }

    private void setupEventListeners() {
        buttonClick.setOnClickListener(v -> {
            textViewTitle.setText("Bạn đã nhấn nút!");
            textViewTitle.setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_dark));
            Toast.makeText(MainActivity.this, "Button được click!", Toast.LENGTH_SHORT).show();
        });

        imageViewDemo.setOnClickListener(new View.OnClickListener() {
            private int imageIndex = 0;
            // Đổi ảnh ở đây để demo
            private int[] images = {
                    android.R.drawable.ic_menu_camera,
                    android.R.drawable.ic_menu_gallery,
                    android.R.drawable.ic_menu_edit,
                    android.R.drawable.ic_menu_view
            };

            @Override
            public void onClick(View v) {
                imageIndex = (imageIndex + 1) % images.length;
                imageViewDemo.setImageResource(images[imageIndex]);
                Toast.makeText(MainActivity.this, "Đã thay đổi hình ảnh!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonSubmit.setOnClickListener(v -> collectAndDisplayData());

        // CheckBox Listeners
        CompoundButton.OnCheckedChangeListener checkListener = (buttonView, isChecked) -> {
            if (isChecked) Toast.makeText(MainActivity.this, "Đã chọn " + buttonView.getText(), Toast.LENGTH_SHORT).show();
        };
        checkBoxJava.setOnCheckedChangeListener(checkListener);
        checkBoxKotlin.setOnCheckedChangeListener(checkListener);
        checkBoxFlutter.setOnCheckedChangeListener(checkListener);

        radioGroupGender.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonMale) {
                Toast.makeText(MainActivity.this, "Đã chọn Nam", Toast.LENGTH_SHORT).show();
            } else if (checkedId == R.id.radioButtonFemale) {
                Toast.makeText(MainActivity.this, "Đã chọn Nữ", Toast.LENGTH_SHORT).show();
            }
        });

        buttonStartProgress.setOnClickListener(v -> startProgressBar());
    }

    private void collectAndDisplayData() {
        String name = editTextName.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên!", Toast.LENGTH_SHORT).show();
            editTextName.setError("Tên không được để trống");
            return;
        }

        StringBuilder languages = new StringBuilder();
        if (checkBoxJava.isChecked()) languages.append("Java, ");
        if (checkBoxKotlin.isChecked()) languages.append("Kotlin, ");
        if (checkBoxFlutter.isChecked()) languages.append("Flutter, ");

        if (languages.length() > 0) {
            languages.setLength(languages.length() - 2);
        }

        String gender = "Chưa chọn";
        int selectedId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedId == R.id.radioButtonMale) gender = "Nam";
        else if (selectedId == R.id.radioButtonFemale) gender = "Nữ";

        String result = "=== THÔNG TIN ĐÃ NHẬP ===\n\n" +
                "Tên: " + name + "\n" +
                "Giới tính: " + gender + "\n" +
                "Ngôn ngữ: " + (languages.length() > 0 ? languages.toString() : "Chưa chọn");

        textViewResult.setText(result);

        layoutResult.setVisibility(View.VISIBLE);

        Toast.makeText(this, "Đã ghi nhận thông tin!", Toast.LENGTH_SHORT).show();
    }

    private void startProgressBar() {
        progressStatus = 0;
        progressBarLoading.setProgress(0);
        buttonStartProgress.setEnabled(false);
        buttonStartProgress.setText("Đang xử lý...");

        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus += 1;
                handler.post(() -> progressBarLoading.setProgress(progressStatus));
                try { Thread.sleep(30); } catch (InterruptedException e) { e.printStackTrace(); }
            }
            handler.post(() -> {
                Toast.makeText(MainActivity.this, "Hoàn thành 100%!", Toast.LENGTH_SHORT).show();
                buttonStartProgress.setEnabled(true);
                buttonStartProgress.setText("Bắt đầu tiến trình");
            });
        }).start();
    }
}