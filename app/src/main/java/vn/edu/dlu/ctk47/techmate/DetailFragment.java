package vn.edu.dlu.ctk47.techmate;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kotlin.Unit;
import vn.edu.dlu.ctk47.techmate.model.Product;
import vn.edu.dlu.ctk47.techmate.firebase.ProductRepository;

public class DetailFragment extends Fragment {

    private TextView txtName, txtPrice, txtBrand, txtDescription;
    private TextView txtSpecScreen, txtSpecChip, lblColors;
    private LinearLayout containerColors;
    private ViewPager2 viewPagerImage;
    private TabLayout tabIndicator;
    private Button btnAdd;
    private ImageView btnCompare;

    private Product product;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Ánh xạ View
        viewPagerImage = view.findViewById(R.id.viewPagerImage);
        tabIndicator = view.findViewById(R.id.tabIndicator);
        txtName = view.findViewById(R.id.txtName);
        txtPrice = view.findViewById(R.id.txtPrice);
        txtBrand = view.findViewById(R.id.txtBrand);
        txtDescription = view.findViewById(R.id.txtDescription);
        txtSpecScreen = view.findViewById(R.id.txtSpecScreen);
        txtSpecChip = view.findViewById(R.id.txtSpecChip);
        lblColors = view.findViewById(R.id.lblColors);
        containerColors = view.findViewById(R.id.containerColors);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnCompare = view.findViewById(R.id.btnCompare);

        // 2. Nhận ID sản phẩm từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String productId = bundle.getString("id");
            if (productId != null) {
                loadProductDetails(productId);
            }
        }

        // 3. Sự kiện Thêm vào giỏ hàng
        btnAdd.setOnClickListener(v -> {
            if (product != null) {
                CartManager.add(product);
                Toast.makeText(getContext(), "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigate(R.id.cartFragment);
            }
        });

        // 4. Sự kiện So sánh
        btnCompare.setOnClickListener(v -> {
            if (product != null) {
                CompareManager.add(product);
                Toast.makeText(getContext(), "Đã chọn để so sánh. Hãy chọn thêm 1 sản phẩm khác!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigate(R.id.homeFragment);
            }
        });
    }

    private void loadProductDetails(String productId) {
        ProductRepository.INSTANCE.getProductById(productId, p -> {
            if (p != null) {
                this.product = p;
                displayProduct();
                
                // Lấy tên thương hiệu từ Brand ID
                if (p.getBrandId() != null) {
                    loadBrandName(p.getBrandId());
                }
            } else {
                Toast.makeText(getContext(), "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            }
            return Unit.INSTANCE;
        });
    }

    private void loadBrandName(String brandId) {
        ProductRepository.INSTANCE.getBrandById(brandId, brand -> {
            if (brand != null && brand.getName() != null) {
                txtBrand.setText(brand.getName());
            } else {
                txtBrand.setText("Unknown Brand");
            }
            return Unit.INSTANCE;
        });
    }

    private void displayProduct() {
        if (product == null) return;
        
        txtName.setText(product.getName());
        txtPrice.setText(String.format(Locale.getDefault(), "$%.2f", product.getPrice()));
        txtDescription.setText(product.getDescription());
        
        // Setup Image Slider
        List<String> images = product.getImages();
        if (images == null || images.isEmpty()) {
            images = new ArrayList<>();
            images.add("https://via.placeholder.com/500"); 
        }
        
        ImageSliderAdapter adapter = new ImageSliderAdapter(images);
        viewPagerImage.setAdapter(adapter);

        new TabLayoutMediator(tabIndicator, viewPagerImage, (tab, position) -> {}).attach();

        // HIỂN THỊ MÀU SẮC DYNAMIC
        setupColors();

        // Hiển thị Specs
        if (product.getSpecs() != null) {
            String screen = product.getSpecs().get("Screen");
            txtSpecScreen.setText(screen != null ? screen : "N/A");
            
            String chip = product.getSpecs().get("CPU");
            txtSpecChip.setText(chip != null ? chip : "N/A");
        }

        txtBrand.setText("..."); 
    }

    private void setupColors() {
        containerColors.removeAllViews();
        List<String> colors = product.getColors();

        if (colors == null || colors.isEmpty()) {
            lblColors.setVisibility(View.GONE);
            containerColors.setVisibility(View.GONE);
        } else {
            lblColors.setVisibility(View.VISIBLE);
            containerColors.setVisibility(View.VISIBLE);

            for (String colorName : colors) {
                TextView chip = new TextView(getContext());
                chip.setText(colorName);
                chip.setPadding(32, 16, 32, 16);
                chip.setBackgroundResource(R.drawable.bg_chip); // Dùng lại drawable cũ
                
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, 
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 16, 0);
                chip.setLayoutParams(params);
                chip.setTextColor(Color.BLACK);
                chip.setTextSize(14);

                containerColors.addView(chip);
            }
        }
    }
}
