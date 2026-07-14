package com.example.vanhieu_lab04_bai1; // QUAN TRỌNG

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class ScoreFragment extends Fragment {

    private EditText editTextDiemToan, editTextDiemLy, editTextDiemHoa;
    private Button buttonTinhDiem;
    private String maSinhVien, hoTen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);

        // Nhận dữ liệu từ StudentInfoFragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            maSinhVien = bundle.getString("maSinhVien");
            hoTen = bundle.getString("hoTen");
        }

        editTextDiemToan = view.findViewById(R.id.editTextDiemToan);
        editTextDiemLy = view.findViewById(R.id.editTextDiemLy);
        editTextDiemHoa = view.findViewById(R.id.editTextDiemHoa);
        buttonTinhDiem = view.findViewById(R.id.buttonTinhDiem);

        buttonTinhDiem.setOnClickListener(v -> xuLyTinhDiem());

        return view;
    }

    private void xuLyTinhDiem() {
        String t = editTextDiemToan.getText().toString();
        String l = editTextDiemLy.getText().toString();
        String h = editTextDiemHoa.getText().toString();

        if (t.isEmpty() || l.isEmpty() || h.isEmpty()) {
            Toast.makeText(getActivity(), "Nhập đủ điểm", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double dToan = Double.parseDouble(t);
            double dLy = Double.parseDouble(l);
            double dHoa = Double.parseDouble(h);

            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.chuyenSangResultFragment(maSinhVien, hoTen, dToan, dLy, dHoa);
            }

        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Điểm phải là số", Toast.LENGTH_SHORT).show();
        }
    }
}