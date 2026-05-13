package vn.edu.dlu.ctk47.techmate;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import vn.edu.dlu.ctk47.techmate.model.Product;

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

        if (p != null) {
            // Log để kiểm tra dữ liệu
            Log.d("ProductAdapter", "Binding product: " + p.getName() + " | Price: " + p.getPrice());
            
            holder.txtName.setText(p.getName() != null ? p.getName() : "Empty Name");
            holder.txtPrice.setText("$" + p.getPrice());

            if (p.getImages() != null && !p.getImages().isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load(p.getImages().get(0))
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .into(holder.imgProduct);
            }

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClick(p);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;
        ImageView imgProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }
    }

    public interface OnItemClick {
        void onClick(Product product);
    }
}
