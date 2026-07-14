package com.example.vanhieu_lab04_bai1; // QUAN TRỌNG

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class StudentInfoFragment extends Fragment {

    private EditText editTextMaSinhVien;
    private EditText editTextHoTen;
    private Button buttonTiep;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_info, container, false);

        editTextMaSinhVien = view.findViewById(R.id.editTextMaSinhVien);
        editTextHoTen = view.findViewById(R.id.editTextHoTen);
        buttonTiep = view.findViewById(R.id.buttonTiep);

        buttonTiep.setOnClickListener(v -> xuLyChuyenFragment());

        return view;
    }

    private void xuLyChuyenFragment() {
        String maSinhVien = editTextMaSinhVien.getText().toString().trim();
        String hoTen = editTextHoTen.getText().toString().trim();

        if (maSinhVien.isEmpty() || hoTen.isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi hàm bên MainActivity để chuyển trang
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.chuyenSangScoreFragment(maSinhVien, hoTen);
        }
    }
}