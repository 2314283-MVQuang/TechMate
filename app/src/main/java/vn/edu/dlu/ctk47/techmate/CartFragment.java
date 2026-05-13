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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    public CartFragment() {
        super(R.layout.fragment_cart);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Đã đổi ID cho khớp với giao diện XML mới
        RecyclerView rvCart = view.findViewById(R.id.rvCartItems);
        TextView txtTotal = view.findViewById(R.id.txtTotalPrice);
        Button btnCheckout = view.findViewById(R.id.btnCheckout);
        RecyclerView rvSuggested = view.findViewById(R.id.rvSuggestedItems);

        // =====================================
        // PHẦN 1: GIỎ HÀNG CỦA BẠN
        // =====================================
        rvCart.setLayoutManager(new LinearLayoutManager(getContext()));

        CartAdapter adapter = new CartAdapter(CartManager.getCart(), () -> {
            updateTotalUI(txtTotal); // Cập nhật lại hàm Total
        });
        rvCart.setAdapter(adapter);

        updateTotalUI(txtTotal); // Gọi lần đầu để hiển thị tổng tiền

        // 👉 Chuyển hướng sang Checkout
        btnCheckout.setOnClickListener(v -> {
            NavController nav = Navigation.findNavController(v);
            nav.navigate(R.id.checkoutFragment);
        });

        // =====================================
        // PHẦN 2: CÓ THỂ BẠN CŨNG THÍCH (Gợi ý SP)
        // =====================================
        if (rvSuggested != null) {
            // Hiển thị dạng lưới 2 cột
            rvSuggested.setLayoutManager(new GridLayoutManager(getContext(), 2));

            // 1. Tạo danh sách sản phẩm giống hệt Hình 2 của bạn
            List<Product> suggestedList = new ArrayList<>();

            suggestedList.add(new Product("iPhone 15 Pro", 28990000, R.drawable.img_iphone_15_pro));
            suggestedList.add(new Product("S24 Ultra", 31990000, R.drawable.img_s24_ultra));
            suggestedList.add(new Product("MacBook Air M3", 27990000, R.drawable.img_macbook_air_m3));
            suggestedList.add(new Product("AirPods Pro 2", 5990000, R.drawable.img_airpods_pro_2));
            suggestedList.add(new Product("Xiaomi 14", 22990000, R.drawable.img_xiaomi_14));
            suggestedList.add(new Product("iPad Pro M4", 28990000, R.drawable.img_ipad_pro_m4));
            suggestedList.add(new Product("Apple Watch Series 9", 10490000, R.drawable.img_apple_watch)); // Sản phẩm thứ 7

            // 2. Gắn Adapter để hiển thị lên màn hình
            // LƯU Ý: Nếu Adapter ở trang chủ của bạn tên khác (VD: HomeAdapter, ItemAdapter...)
            // thì bạn hãy đổi chữ 'ProductAdapter' bên dưới cho khớp nhé!
            ProductAdapter suggestedAdapter = new ProductAdapter(suggestedList, new ProductAdapter.OnItemClick() {
                @Override
                public void onClick(Product product) {
                    // Xử lý khi bấm vào sản phẩm gợi ý (có thể mở trang chi tiết)
                    // Hiện tại tạm thời để trống
                }

                @Override
                public void onLongClick(Product product) {
                    // Không làm gì cả vì bạn đã bỏ Long Click
                }
            });
            rvSuggested.setAdapter(suggestedAdapter);
        }
    }

    // Hàm phụ trợ để tính và hiển thị Tổng tiền chuẩn Việt Nam
    private void updateTotalUI(TextView txtTotal) {
        double total = CartManager.getTotal();
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String formattedTotal = formatter.format(total).replace(",", ".") + " đ";
        txtTotal.setText(formattedTotal);
    }
}