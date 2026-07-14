package com.example.vanhieu_lab04_bai1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class ResultFragment extends Fragment {

    // --- BẠN ĐANG THIẾU DÒNG NÀY ---
    private TextView tvMaSV, tvHoTen, tvDiemToan, tvDiemLy, tvDiemHoa, tvTB, tvXepLoai;
    // --------------------------------

    private Button btnQuayLai;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        // Ánh xạ (Code của bạn đúng, nhưng cần khai báo biến ở trên thì mới hết đỏ)
        tvMaSV = view.findViewById(R.id.textViewMaSinhVien);
        tvHoTen = view.findViewById(R.id.textViewHoTen);
        tvDiemToan = view.findViewById(R.id.textViewDiemToan);
        tvDiemLy = view.findViewById(R.id.textViewDiemLy);
        tvDiemHoa = view.findViewById(R.id.textViewDiemHoa);
        tvTB = view.findViewById(R.id.textViewDiemTrungBinh);
        tvXepLoai = view.findViewById(R.id.textViewXepLoai);
        btnQuayLai = view.findViewById(R.id.buttonQuayLai);

        // Nhận dữ liệu
        Bundle bundle = getArguments();
        if (bundle != null) {
            String msv = bundle.getString("maSinhVien");
            String ten = bundle.getString("hoTen");
            double t = bundle.getDouble("diemToan");
            double l = bundle.getDouble("diemLy");
            double h = bundle.getDouble("diemHoa");

            double tb = (t + l + h) / 3;

            tvMaSV.setText("Mã SV: " + msv);
            tvHoTen.setText("Họ tên: " + ten);
            tvDiemToan.setText("Toán: " + t);
            tvDiemLy.setText("Lý: " + l);
            tvDiemHoa.setText("Hóa: " + h);
            tvTB.setText(String.format("Trung bình: %.2f", tb));

            String xepLoai = tb >= 5 ? "Đạt" : "Trượt";
            if (tb >= 8) xepLoai = "Giỏi";
            else if (tb >= 6.5) xepLoai = "Khá";
            else if (tb >= 5) xepLoai = "Trung bình";
            else xepLoai = "Yếu";

            tvXepLoai.setText("Xếp loại: " + xepLoai);
        }

        btnQuayLai.setOnClickListener(v -> {
            MainActivity main = (MainActivity) getActivity();
            if (main != null) main.quayLaiStudentInfoFragment();
        });

        return view;
    }
}