package vn.edu.dlu.ctk47.techmate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Product {
    public String name;
    public double price;
    public int imageResId; // 🔥 THÊM: Biến lưu ID hình ảnh
    public Map<String, String> specs;

    // 🔥 THÊM: Constructor 3 tham số (Dùng cho Gợi ý SP có truyền ảnh)
    public Product(String name, double price, int imageResId) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.specs = new HashMap<>();
    }

    // Constructor 2 tham số cũ (Giữ lại để các màn hình cũ không bị lỗi đỏ)
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
        // Gán 1 cái ảnh mặc định nào đó để không bị lỗi trống.
        // Bạn có thể đổi 'img_mascot_shopping' thành icon mặc định tùy ý.
        this.imageResId = R.drawable.img_mascot_shopping;
        this.specs = new HashMap<>();
    }

    // 🔥 FIX: so sánh theo nội dung (Giữ nguyên code xịn của bạn)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product p = (Product) o;
        return Double.compare(p.price, price) == 0 &&
                Objects.equals(name, p.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}