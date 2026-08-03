package com.example.lab05_bai4_vanhieu;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2; // Import ViewPager2

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ViewPager2 viewPager;
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
        viewPager = findViewById(R.id.viewPager);
        List<OnboardingItem> list = new ArrayList<>();
        list.add(new OnboardingItem(R.drawable.viewpager4, "ViewPager2-1", "Đây là ViewPager2-1"));
        list.add(new OnboardingItem(R.drawable.viewpager5, "ViewPager2-2", "Đây là ViewPager2-2"));
        list.add(new OnboardingItem(R.drawable.viewpager6, "ViewPager2-3", "Đây là ViewPager2-3"));
        OnboardingAdapter adapter = new OnboardingAdapter(list);
        viewPager.setAdapter(adapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
    }
}