package vn.edu.dlu.ctk47.techmate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartFragment extends Fragment {

    public CartFragment() {
        super(R.layout.fragment_cart);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.rvCart);
        TextView txtTotal = view.findViewById(R.id.txtTotal);
        Button btnCheckout = view.findViewById(R.id.btnCheckout);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        CartAdapter adapter = new CartAdapter(CartManager.getCart(), () -> {
            txtTotal.setText("Total: $" + CartManager.getTotal());
        });

        rv.setAdapter(adapter);

        // Set total ban đầu
        txtTotal.setText("Total: $" + CartManager.getTotal());

        // 👉 Checkout
        btnCheckout.setOnClickListener(v -> {
            NavController nav = Navigation.findNavController(v);
            nav.navigate(R.id.checkoutFragment);
        });
    }
}