package vn.edu.dlu.ctk47.techmate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class DetailFragment extends Fragment {

    TextView txtName, txtPrice;
    TextView txtSpecScreen, txtSpecChip;

    Button btnAdd;
    ImageView btnCompare;

    Product product; // 🔥 giữ object thật

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtName = view.findViewById(R.id.txtName);
        txtPrice = view.findViewById(R.id.txtPrice);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnCompare = view.findViewById(R.id.btnCompare);

        // 🔥 SPEC (mới)
        txtSpecScreen = view.findViewById(R.id.txtSpecScreen);
        txtSpecChip = view.findViewById(R.id.txtSpecChip);

        // 🔥 NHẬN OBJECT (fix chuẩn)
        Bundle bundle = getArguments();
        if (bundle != null) {
            product = new Product(
                    bundle.getString("name"),
                    bundle.getDouble("price")
            );

            txtName.setText(product.name);
            txtPrice.setText("$" + product.price);

            // 🔥 DEMO SPEC (sau này DB)
            product.specs.put("Screen", "6.7 OLED");
            product.specs.put("CPU", "A17 Pro");

            // 🔥 BIND SPEC
            txtSpecScreen.setText(product.specs.get("Screen"));
            txtSpecChip.setText(product.specs.get("CPU"));
        }

        // ======================
        // ADD TO CART
        // ======================
        btnAdd.setOnClickListener(v -> {

            CartManager.add(product);

            Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();

            NavHostFragment.findNavController(this)
                    .navigate(R.id.cartFragment);
        });

        // ======================
        // COMPARE (FLOW CHUẨN FIGMA)
        // ======================
        btnCompare.setOnClickListener(v -> {

            CompareManager.clear(); // reset

            CompareManager.add(product); // lưu 1 sản phẩm

            Toast.makeText(getContext(), "Select another product", Toast.LENGTH_SHORT).show();

            // 👉 quay về home để chọn tiếp
            NavHostFragment.findNavController(this)
                    .navigate(R.id.homeFragment);
        });
    }
}