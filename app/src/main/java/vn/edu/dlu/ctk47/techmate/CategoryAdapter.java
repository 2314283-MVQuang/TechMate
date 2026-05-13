package vn.edu.dlu.ctk47.techmate;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import vn.edu.dlu.ctk47.techmate.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<Category> list;
    private final OnCategoryClick listener;

    public CategoryAdapter(List<Category> list, OnCategoryClick listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category cat = list.get(position);
        if (cat != null) {
            Log.d("CategoryAdapter", "Binding category: " + cat.getName());
            holder.txt.setText(cat.getName() != null ? cat.getName() : "Null Name");
            
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClick(cat);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txtCategory);
        }
    }

    public interface OnCategoryClick {
        void onClick(Category category);
    }
}
