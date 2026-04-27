package vn.edu.dlu.ctk47.techmate;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static List<CartItem> cartList = new ArrayList<>();

    // ➕ Add (gộp nếu trùng)
    public static void add(Product p) {
        for (CartItem item : cartList) {
            if (item.product.name.equals(p.name)) {
                item.quantity++;
                return;
            }
        }
        cartList.add(new CartItem(p, 1));
    }

    public static List<CartItem> getCart() {
        return cartList;
    }

    public static double getTotal() {
        double total = 0;
        for (CartItem item : cartList) {
            total += item.product.price * item.quantity;
        }
        return total;
    }

    public static void remove(int position) {
        cartList.remove(position);
    }

    public static void clear() {
        cartList.clear();
    }
}