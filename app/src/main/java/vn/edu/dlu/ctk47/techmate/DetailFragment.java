package vn.edu.dlu.ctk47.techmate;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.text.DecimalFormat;

public class DetailFragment extends Fragment {

    // Khai báo các View
    ImageView imgProductDetail;
    TextView txtName, txtPrice, txtBottomPrice, txtSpecCPU;
    TextView btnVar256, btnVar512, btnVar1TB;
    TextView btnColNatural, btnColBlack, btnColBlue;
    Button btnAdd;

    // Đối tượng sản phẩm và biến tính toán giá
    Product product;
    double basePrice = 28990000;
    double currentTotal = 0;

    // Cấu hình chênh lệch giá (Giả định dữ liệu cố định)
    double bump256 = 0;
    double bump512 = 5000000;
    double bump1TB = 11000000;

    double bumpColNatural = 0;
    double bumpColBlack = -400000;
    double bumpColBlue = -600000;

    // Trạng thái lựa chọn hiện tại
    String selectedVariant = "256GB";
    String selectedColor = "Titan Tự nhiên";
    double selectedVarBump = bump256;
    double selectedColBump = bumpColNatural;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ View
        initViews(view);

        // Nhận dữ liệu từ Bundle (NavGraph gửi từ HomeFragment sang)
        Bundle bundle = getArguments();
        if (bundle != null) {
            setupProductData(bundle);
        }

        // Thiết lập sự kiện Click cho cấu hình và màu sắc
        setupSelectionListeners();

        // Xử lý sự kiện nút Mua ngay / Thêm vào giỏ
        btnAdd.setOnClickListener(v -> {
            // Đọc trạng thái từ SharedPreferences để xem đã đăng nhập chưa
            SharedPreferences prefs = requireActivity().getSharedPreferences("TechMatePrefs", android.content.Context.MODE_PRIVATE);
            boolean isUserLoggedIn = prefs.getBoolean("IS_LOGGED_IN", false);

            if (!isUserLoggedIn) {
                showLoginDialog(); // Chưa đăng nhập -> Hiện popup
            } else {
                performAddToCart(); // Đã đăng nhập -> Nhét vào giỏ hàng
            }
        });
    }

    private void initViews(View view) {
        imgProductDetail = view.findViewById(R.id.imgProduct);
        txtName = view.findViewById(R.id.txtDetailName);
        txtPrice = view.findViewById(R.id.txtDetailPrice);
        txtBottomPrice = view.findViewById(R.id.txtBottomPrice);
        txtSpecCPU = view.findViewById(R.id.txtSpecCPU);
        btnAdd = view.findViewById(R.id.btnAdd);

        btnVar256 = view.findViewById(R.id.btnVar256);
        btnVar512 = view.findViewById(R.id.btnVar512);
        btnVar1TB = view.findViewById(R.id.btnVar1TB);

        btnColNatural = view.findViewById(R.id.btnColNatural);
        btnColBlack = view.findViewById(R.id.btnColBlack);
        btnColBlue = view.findViewById(R.id.btnColBlue);
    }

    private void setupProductData(Bundle bundle) {
        String name = bundle.getString("name");
        double price = bundle.getDouble("price");

        // Nhận ID hình ảnh (nếu lỗi thì dùng ảnh mặc định)
        int imageId = bundle.getInt("image", R.drawable.img_mascot_shopping);

        // ==========================================
        // Khởi tạo Product CHUẨN: 1 lần duy nhất
        // ==========================================
        product = new Product(name, price, imageId);

        // Hiển thị ảnh lên màn hình chi tiết
        if (imgProductDetail != null) {
            imgProductDetail.setImageResource(imageId);
        }

        // Nếu giá truyền vào quá thấp (do data cũ test), dùng giá mặc định để hiển thị đẹp
        basePrice = (product.price < 10000) ? 28990000 : product.price;

        txtName.setText(product.name);

        // Giả định thông số CPU
        product.specs.put("CPU", "Apple A17 Pro (3 nm)");
        if (txtSpecCPU != null) txtSpecCPU.setText(product.specs.get("CPU"));

        updateUI();
    }

    private void setupSelectionListeners() {
        btnVar256.setOnClickListener(v -> setVariant("256GB", bump256, btnVar256));
        btnVar512.setOnClickListener(v -> setVariant("512GB", bump512, btnVar512));
        btnVar1TB.setOnClickListener(v -> setVariant("1TB", bump1TB, btnVar1TB));

        btnColNatural.setOnClickListener(v -> setColor("Titan Tự nhiên", bumpColNatural, btnColNatural));
        btnColBlack.setOnClickListener(v -> setColor("Titan Đen", bumpColBlack, btnColBlack));
        btnColBlue.setOnClickListener(v -> setColor("Titan Xanh", bumpColBlue, btnColBlue));
    }

    // --- LOGIC HIỂN THỊ DIALOG ĐĂNG NHẬP ---
    private void showLoginDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_login_request);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Button btnLoginNav = dialog.findViewById(R.id.btnLoginNav);
        Button btnRegisterNav = dialog.findViewById(R.id.btnRegisterNav);
        ImageButton btnClose = dialog.findViewById(R.id.btnClose);

        btnLoginNav.setOnClickListener(v -> {
            dialog.dismiss();
            navigateToAuth("LOGIN");
        });

        btnRegisterNav.setOnClickListener(v -> {
            dialog.dismiss();
            navigateToAuth("REGISTER");
        });

        if (btnClose != null) btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void navigateToAuth(String mode) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.putExtra("AUTH_MODE", mode);
        startActivity(intent);
    }

    private void performAddToCart() {
        // Cập nhật tên sản phẩm kèm biến thể để lưu vào giỏ
        String finalName = product.name + " (" + selectedVariant + " - " + selectedColor + ")";

        // ==========================================
        // KHI THÊM VÀO GIỎ, NHỚ GỬI KÈM HÌNH ẢNH
        // ==========================================
        Product cartItem = new Product(finalName, currentTotal, product.imageResId);

        CartManager.add(cartItem);
        Toast.makeText(getContext(), "Đã thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
        NavHostFragment.findNavController(this).navigate(R.id.cartFragment);
    }

    // --- LOGIC TÍNH GIÁ VÀ CẬP NHẬT UI ---
    private void setVariant(String name, double bump, TextView btnActive) {
        selectedVariant = name;
        selectedVarBump = bump;

        // Reset style các nút dung lượng
        resetVariantStyles();
        btnActive.setBackgroundResource(R.drawable.bg_variant_selected);
        btnActive.setTextColor(Color.parseColor("#D70018"));

        updateUI();
    }

    private void resetVariantStyles() {
        int colorGray = getResources().getColor(android.R.color.darker_gray);
        TextView[] variants = {btnVar256, btnVar512, btnVar1TB};
        for (TextView v : variants) {
            v.setBackgroundResource(R.drawable.bg_variant_unselected);
            v.setTextColor(colorGray);
        }
    }

    private void setColor(String name, double bump, TextView btnActive) {
        selectedColor = name;
        selectedColBump = bump;

        btnColNatural.setBackgroundResource(R.drawable.bg_variant_unselected);
        btnColBlack.setBackgroundResource(R.drawable.bg_variant_unselected);
        btnColBlue.setBackgroundResource(R.drawable.bg_variant_unselected);

        btnActive.setBackgroundResource(R.drawable.bg_variant_selected);

        updateUI();
    }

    private void updateUI() {
        currentTotal = basePrice + selectedVarBump + selectedColBump;

        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String formattedTotal = formatter.format(currentTotal).replace(",", ".") + "₫";

        txtPrice.setText(formattedTotal);
        txtBottomPrice.setText(formattedTotal);

        double currentBaseForColor = basePrice + selectedVarBump;

        String priceNatural = formatter.format(currentBaseForColor + bumpColNatural).replace(",", ".") + "₫";
        String priceBlack = formatter.format(currentBaseForColor + bumpColBlack).replace(",", ".") + "₫";
        String priceBlue = formatter.format(currentBaseForColor + bumpColBlue).replace(",", ".") + "₫";

        setHtmlText(btnColNatural, "Titan Tự nhiên", priceNatural, selectedColor.equals("Titan Tự nhiên"));
        setHtmlText(btnColBlack, "Titan Đen", priceBlack, selectedColor.equals("Titan Đen"));
        setHtmlText(btnColBlue, "Titan Xanh", priceBlue, selectedColor.equals("Titan Xanh"));
    }

    private void setHtmlText(TextView textView, String colorName, String price, boolean isSelected) {
        String colorHex = isSelected ? "#D70018" : "#333333";
        String htmlContent = "<span style=\"color:" + colorHex + ";\">" +
                "<b>" + colorName + "</b><br>" + price + "</span>";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml(htmlContent));
        }
    }
}