package com.example.kiemtra_nguyenvanhieu.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.example.kiemtra_nguyenvanhieu.viewmodel.SharedTaskViewModel;
import com.example.kiemtra_nguyenvanhieu.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private SharedTaskViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Layout chứa TabLayout id: tabLayout và ViewPager2 id: viewPager

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        // Khởi tạo Adapter cho ViewPager
        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return position == 0 ? new TaskListFragment() : new AddTaskFragment();
            }
            @Override
            public int getItemCount() { return 2; }
        });

        // Kết nối TabLayout và ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(position == 0 ? "Danh sách" : "Thêm công việc");
        }).attach();

        // Lắng nghe ViewModel để chuyển tab tự động (Ví dụ: bấm Lưu xong tự nhảy về tab Danh sách)
        viewModel = new ViewModelProvider(this).get(SharedTaskViewModel.class);
        viewModel.getNavigateToTab().observe(this, tabIndex -> {
            if (tabIndex != null) {
                viewPager.setCurrentItem(tabIndex, true);
                viewModel.getNavigateToTab().setValue(null); // Tránh bị nhảy tab liên tục khi xoay màn hình
            }
        });
    }
}