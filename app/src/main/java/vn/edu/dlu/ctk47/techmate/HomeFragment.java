package vn.edu.dlu.ctk47.techmate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    ImageView btnCompareFloat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCompareFloat = view.findViewById(R.id.btnCompareFloat);

        // 👉 CLICK FLOAT BUTTON
        btnCompareFloat.setOnClickListener(v -> {

            if (CompareManager.get().size() < 2) {
                Toast.makeText(getContext(), "Select 2 products first", Toast.LENGTH_SHORT).show();
                return;
            }

            Navigation.findNavController(view).navigate(R.id.compareFragment);
        });

        // CATEGORY
        RecyclerView rvCat = view.findViewById(R.id.rvCategories);
        rvCat.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        // PRODUCT
        RecyclerView rvProd = view.findViewById(R.id.rvProducts);
        rvProd.setLayoutManager(new GridLayoutManager(getContext(), 2));

        List<Product> products = new ArrayList<>();

        Product p1 = new Product("iPhone 15 Pro", 999);
        p1.specs.put("CPU", "A17 Pro");
        p1.specs.put("RAM", "8GB");

        Product p2 = new Product("S24 Ultra", 1199);
        p2.specs.put("CPU", "Snapdragon 8 Gen 3");
        p2.specs.put("RAM", "12GB");

        products.add(p1);
        products.add(p2);
        products.add(new Product("MacBook Air", 1299));
        products.add(new Product("AirPods Pro", 249));

        // 🔥 ADAPTER (FLOW MỚI)
        ProductAdapter adapter = new ProductAdapter(products, new ProductAdapter.OnItemClick() {

            @Override
            public void onClick(Product product) {

                // 🔥 NẾU ĐÃ CÓ 1 SP → ADD + COMPARE
                if (CompareManager.hasOne()) {

                    CompareManager.add(product);

                    Navigation.findNavController(view)
                            .navigate(R.id.compareFragment);

                    return;
                }

                // 👉 CHƯA CHỌN → VÀO DETAIL
                Bundle bundle = new Bundle();
                bundle.putString("name", product.name);
                bundle.putDouble("price", product.price);

                Navigation.findNavController(view)
                        .navigate(R.id.detailFragment, bundle);
            }

            @Override
            public void onLongClick(Product product) {
                // ❌ KHÔNG DÙNG NỮA
            }
        });

        rvProd.setAdapter(adapter);

        updateCompareUI();
    }

    private void updateCompareUI() {
        int size = CompareManager.get().size();

        // 👉 có 1 sản phẩm → nút sáng lên
        btnCompareFloat.setAlpha(size > 0 ? 1f : 0.4f);
    }
    @Override
    public void onResume() {
        super.onResume();
        updateCompareUI();
    }
}