package vn.edu.dlu.ctk47.techmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartItem> list;
    private OnCartChange listener;

    // Constructor
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
        h.txtPrice.setText("$" + item.product.price);
        h.txtQty.setText(String.valueOf(item.quantity));

        // ➕
        h.btnPlus.setOnClickListener(v -> {
            int pos = h.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                list.get(pos).quantity++;
                notifyItemChanged(pos);
                notifyTotal();
            }
        });

        // ➖
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

        // ❌
        h.btnDelete.setOnClickListener(v -> {
            int pos = h.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                CartManager.remove(pos);
                notifyItemRemoved(pos);
                notifyTotal();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // ViewHolder
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

    // Callback để update total
    public interface OnCartChange {
        void onChange();
    }

    private void notifyTotal() {
        if (listener != null) {
            listener.onChange();
        }
    }
}