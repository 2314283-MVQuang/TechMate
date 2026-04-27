package vn.edu.dlu.ctk47.techmate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Product {
    public String name;
    public double price;
    public Map<String, String> specs;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
        this.specs = new HashMap<>();
    }

    // 🔥 FIX: so sánh theo nội dung
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