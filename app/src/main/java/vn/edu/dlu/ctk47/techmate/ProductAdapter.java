package vn.edu.dlu.ctk47.techmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final List<Product> list;
    private final OnItemClick listener;

    public ProductAdapter(List<Product> list, OnItemClick listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = list.get(position);

        holder.txtName.setText(p.name);
        holder.txtPrice.setText("$" + p.price);

        // 🔥 CLICK (QUAN TRỌNG NHẤT)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(p);
            }
        });

        // ❌ KHÔNG DÙNG LONG CLICK nữa
        holder.itemView.setOnLongClickListener(null);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
        }
    }

    public interface OnItemClick {
        void onClick(Product product);
        void onLongClick(Product product); // vẫn giữ để tránh lỗi compile
    }
}