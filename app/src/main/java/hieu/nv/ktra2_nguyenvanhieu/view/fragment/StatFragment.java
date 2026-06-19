package hieu.nv.ktra2_nguyenvanhieu.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import hieu.nv.ktra2_nguyenvanhieu.R;
import hieu.nv.ktra2_nguyenvanhieu.model.Transaction;
import hieu.nv.ktra2_nguyenvanhieu.viewmodel.TransactionViewModel;

import java.text.DecimalFormat;

public class StatFragment extends Fragment {

    private TextView tvTotalThu, tvTotalChi, tvBalance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stat, container, false);

        tvTotalThu = view.findViewById(R.id.tvTotalThu);
        tvTotalChi = view.findViewById(R.id.tvTotalChi);
        tvBalance = view.findViewById(R.id.tvBalance);

        // Lấy chung ViewModel với ListFragment
        TransactionViewModel viewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);

        // Lắng nghe dữ liệu thay đổi để tính toán lại ngay lập tức
        viewModel.getListTransactions().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                double totalThu = 0;
                double totalChi = 0;

                for (Transaction t : list) {
                    if (t.getType() == 1) {
                        totalThu += t.getAmount();
                    } else {
                        totalChi += t.getAmount();
                    }
                }

                double balance = totalThu - totalChi;

                DecimalFormat formatter = new DecimalFormat("#,### đ");
                tvTotalThu.setText("Tổng Thu: " + formatter.format(totalThu));
                tvTotalChi.setText("Tổng Chi: " + formatter.format(totalChi));
                tvBalance.setText("SỐ DƯ: " + formatter.format(balance));

                // Đổi màu số dư (Âm -> Đỏ, Dương -> Xanh)
                if (balance < 0) {
                    tvBalance.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                } else {
                    tvBalance.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                }
            }
        });

        return view;
    }
}