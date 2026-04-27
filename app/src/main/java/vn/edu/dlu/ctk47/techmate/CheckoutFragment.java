package vn.edu.dlu.ctk47.techmate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CheckoutFragment extends Fragment {

    public CheckoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView txtTotal = view.findViewById(R.id.txtTotal);
        Button btnPlaceOrder = view.findViewById(R.id.btnPlaceOrder);

        // Hiển thị tổng tiền
        txtTotal.setText("Total: $" + CartManager.getTotal());

        // Xử lý đặt hàng
        btnPlaceOrder.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Order placed!", Toast.LENGTH_SHORT).show();

            // 👉 Clear giỏ hàng sau khi đặt
            CartManager.clear();

            // 👉 (tuỳ chọn) quay về Home
            requireActivity().onBackPressed();
        });
    }
}