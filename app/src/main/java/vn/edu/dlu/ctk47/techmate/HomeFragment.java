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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    ImageView btnCompareFloat;
    RecyclerView rvProducts, rvBrands;
    ProductAdapter productAdapter;
    CategoryAdapter brandAdapter; // Adapter cũ dùng cho dải Hãng (chỉ có chữ)

    // Dữ liệu bộ lọc và sản phẩm
    List<Product> allProducts = new ArrayList<>();
    List<Product> displayProducts = new ArrayList<>();
    Map<String, List<String>> brandData = new HashMap<>();

    // Trạng thái hiện tại
    String currentCategory = "All";
    String currentBrand = "All";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCompareFloat = view.findViewById(R.id.btnCompareFloat);
        setupCompareButton(view);

        // 1. Chuẩn bị dữ liệu (Mock Data)
        setupData();

        // 2. Setup RV Danh mục chính (Sử dụng Adapter có Icon mới, 2 dòng cuộn ngang)
        RecyclerView rvCat = view.findViewById(R.id.rvCategories);
        rvCat.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false));

        // Tạo danh sách với Ảnh + Tên (Đảm bảo bạn đã có các file ic_cat_... trong thư mục drawable)
        List<CategoryModel> mainCategories = Arrays.asList(
                new CategoryModel("All", R.drawable.ic_cat_all),
                new CategoryModel("Phones", R.drawable.ic_cat_phone),
                new CategoryModel("Laptops", R.drawable.ic_cat_laptop),
                new CategoryModel("Tablets", R.drawable.ic_cat_tablet),
                new CategoryModel("Audio", R.drawable.ic_cat_audio),
                new CategoryModel("Watches", R.drawable.ic_cat_watch),
                new CategoryModel("Cameras", R.drawable.ic_cat_camera),
                new CategoryModel("TVs", R.drawable.ic_cat_tv),
                new CategoryModel("Accessories", R.drawable.ic_cat_accessory),
                new CategoryModel("Gaming", R.drawable.ic_cat_gaming)
        );

        // Gắn CategoryIconAdapter
        rvCat.setAdapter(new CategoryIconAdapter(mainCategories, categoryModel -> {
            currentCategory = categoryModel.name; // Lấy tên danh mục
            currentBrand = "All"; // Reset brand về "All" khi đổi danh mục chính

            // Xử lý danh sách Hãng (Brands) hiển thị bên dưới
            List<String> brands;
            if (currentCategory.equals("All")) {
                brands = Arrays.asList("All");
            } else {
                brands = brandData.get(currentCategory);
                if (brands == null) brands = Arrays.asList("All");
            }

            brandAdapter.updateData(brands);
            filterProducts(); // Gọi hàm lọc sản phẩm
        }));

        // 3. Setup RV Danh mục phụ (Hãng - 1 dòng, cuộn ngang, chỉ có chữ)
        rvBrands = view.findViewById(R.id.rvBrands);
        rvBrands.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Mặc định lúc mới vào là chọn "All" -> Hãng cũng chỉ có "All"
        List<String> initialBrands = Arrays.asList("All");
        brandAdapter = new CategoryAdapter(initialBrands, brand -> {
            currentBrand = brand;
            filterProducts(); // Lọc lại sản phẩm khi đổi hãng
        });
        rvBrands.setAdapter(brandAdapter);

        // 4. Setup RV Sản phẩm (Lưới 2 cột, cuộn dọc)
        rvProducts = view.findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));

        productAdapter = new ProductAdapter(displayProducts, new ProductAdapter.OnItemClick() {
            @Override
            public void onClick(Product product) {
                // Nếu đang trong luồng Compare (đã có 1 sản phẩm)
                if (CompareManager.hasOne()) {
                    CompareManager.add(product);
                    Navigation.findNavController(view).navigate(R.id.compareFragment);
                    return;
                }
                // Nếu click bình thường -> Vào chi tiết
                Bundle bundle = new Bundle();
                bundle.putString("name", product.name);
                bundle.putDouble("price", product.price);
                bundle.putInt("image", product.imageResId);
                Navigation.findNavController(view).navigate(R.id.detailFragment, bundle);
            }

            @Override
            public void onLongClick(Product product) {}
        });
        rvProducts.setAdapter(productAdapter);

        // Lọc dữ liệu lần đầu tiên để hiển thị lên màn hình
        filterProducts();
        updateCompareUI();
    }

    private void filterProducts() {
        displayProducts.clear();
        for (Product p : allProducts) {
            String pCategory = p.specs.get("Category");
            String pBrand = p.specs.get("Brand");

            boolean matchCategory = currentCategory.equals("All") || currentCategory.equals(pCategory);
            boolean matchBrand = currentBrand.equals("All") || currentBrand.equals(pBrand);

            if (matchCategory && matchBrand) {
                displayProducts.add(p);
            }
        }
        productAdapter.notifyDataSetChanged();
    }

    private void setupData() {
        // Dữ liệu Hãng theo từng danh mục
        brandData.put("Phones", Arrays.asList("All", "Apple", "Samsung", "Oppo", "Xiaomi"));
        brandData.put("Laptops", Arrays.asList("All", "MacBook", "Asus", "Dell", "HP"));
        brandData.put("Tablets", Arrays.asList("All", "iPad", "Galaxy Tab"));
        brandData.put("Audio", Arrays.asList("All", "AirPods", "Sony", "JBL", "Marshall"));
        brandData.put("Watches", Arrays.asList("All", "Apple Watch", "Garmin", "Samsung"));
        brandData.put("Cameras", Arrays.asList("All", "Sony", "Canon", "Nikon"));
        brandData.put("TVs", Arrays.asList("All", "LG", "Samsung", "Sony"));
        brandData.put("Accessories", Arrays.asList("All", "Cables", "Chargers", "Cases"));
        brandData.put("Gaming", Arrays.asList("All", "PlayStation", "Xbox", "Nintendo"));

        // ==============================================================
        // ĐÃ SỬA: Thay đổi Constructor thành 3 tham số (Tên, Giá, Hình ảnh)
        // Cập nhật giá tiền thành tiền Việt Nam (đồng)
        // ==============================================================
        Product p1 = new Product("iPhone 15 Pro", 28990000, R.drawable.img_iphone_15_pro);
        p1.specs.put("Category", "Phones"); p1.specs.put("Brand", "Apple");

        Product p2 = new Product("S24 Ultra", 31990000, R.drawable.img_s24_ultra);
        p2.specs.put("Category", "Phones"); p2.specs.put("Brand", "Samsung");

        Product p3 = new Product("MacBook Air M3", 27990000, R.drawable.img_macbook_air_m3);
        p3.specs.put("Category", "Laptops"); p3.specs.put("Brand", "MacBook");

        Product p4 = new Product("AirPods Pro 2", 5990000, R.drawable.img_airpods_pro_2);
        p4.specs.put("Category", "Audio"); p4.specs.put("Brand", "AirPods");

        Product p5 = new Product("Xiaomi 14", 22990000, R.drawable.img_xiaomi_14);
        p5.specs.put("Category", "Phones"); p5.specs.put("Brand", "Xiaomi");

        Product p6 = new Product("iPad Pro M4", 28990000, R.drawable.img_ipad_pro_m4);
        p6.specs.put("Category", "Tablets"); p6.specs.put("Brand", "iPad");

        Product p7 = new Product("Apple Watch Series 9", 10490000, R.drawable.img_apple_watch);
        p7.specs.put("Category", "Watches"); p7.specs.put("Brand", "Apple Watch");

        allProducts.addAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7));
    }

    private void setupCompareButton(View view) {
        btnCompareFloat.setOnClickListener(v -> {
            if (CompareManager.get().size() < 2) {
                Toast.makeText(getContext(), "Select 2 products first", Toast.LENGTH_SHORT).show();
                return;
            }
            Navigation.findNavController(view).navigate(R.id.compareFragment);
        });
    }

    private void updateCompareUI() {
        int size = CompareManager.get().size();
        btnCompareFloat.setAlpha(size > 0 ? 1f : 0.4f);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCompareUI();
    }
}