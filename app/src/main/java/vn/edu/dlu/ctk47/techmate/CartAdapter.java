package vn.edu.dlu.ctk47.techmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat; // <-- Đã thêm thư viện
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartItem> list;
    private OnCartChange listener;

    public CartAdapter(List<CartItem> list, OnCartChange listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        CartItem item = list.get(position);

        h.txtName.setText(item.product.name);

        // =====================================
        // SỬA LỖI $2.899E7 THÀNH 28.990.000 đ
        // =====================================
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String formattedPrice = formatter.format(item.product.price).replace(",", ".") + " đ";
        h.txtPrice.setText(formattedPrice);

        h.txtQty.setText(String.valueOf(item.quantity));

        // ➕ Tăng số lượng
        h.btnPlus.setOnClickListener(v -> {
            int pos = h.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                list.get(pos).quantity++;
                notifyItemChanged(pos);
                notifyTotal();
            }
        });

        // ➖ Giảm số lượng
        h.btnMinus.setOnClickListener(v -> {
            int pos = h.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                if (list.get(pos).quantity > 1) {
                    list.get(pos).quantity--;
                    notifyItemChanged(pos);
                    notifyTotal();
                }
            }
        });

        // ❌ Xóa khỏi giỏ
        h.btnDelete.setOnClickListener(v -> {
            int pos = h.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                CartManager.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, list.size()); // Cập nhật lại vị trí
                notifyTotal();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtQty;
        ImageView img, btnDelete;
        TextView btnPlus, btnMinus;

        public ViewHolder(@NonNull View v) {
            super(v);
            txtName = v.findViewById(R.id.txtName);
            txtPrice = v.findViewById(R.id.txtPrice);
            txtQty = v.findViewById(R.id.txtQty);
            img = v.findViewById(R.id.imgProduct);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnPlus = v.findViewById(R.id.btnPlus);
            btnMinus = v.findViewById(R.id.btnMinus);
        }
    }

    public interface OnCartChange {
        void onChange();
    }

    private void notifyTotal() {
        if (listener != null) {
            listener.onChange();
        }
    }
}