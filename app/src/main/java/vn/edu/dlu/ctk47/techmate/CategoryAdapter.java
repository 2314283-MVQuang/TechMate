package vn.edu.dlu.ctk47.techmate;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<String> list;
    private int selectedPos = 0; // Lưu vị trí đang được chọn
    private OnItemClick listener;

    public CategoryAdapter(List<String> list, OnItemClick listener) {
        this.list = list;
        this.listener = listener;
    }

    public void updateData(List<String> newList) {
        this.list = newList;
        this.selectedPos = 0; // Reset về vị trí đầu tiên khi đổi danh mục
        notifyDataSetChanged();
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
        String item = list.get(position);
        holder.txt.setText(item);

        // Đổi màu nếu đang được chọn (Sửa lỗi chữ bị ẩn)
        if (selectedPos == position) {
            holder.txt.setBackgroundResource(R.drawable.bg_chip_selected);
            holder.txt.setTextColor(Color.parseColor("#28C8BE")); // Màu xanh chủ đạo
        } else {
            holder.txt.setBackgroundResource(R.drawable.bg_chip);
            holder.txt.setTextColor(Color.BLACK); // Màu đen khi không chọn
        }

        holder.itemView.setOnClickListener(v -> {
            int oldPos = selectedPos;
            selectedPos = holder.getBindingAdapterPosition();

            // Render lại UI của 2 item bị ảnh hưởng
            notifyItemChanged(oldPos);
            notifyItemChanged(selectedPos);

            if (listener != null) {
                listener.onClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txtCategory);
        }
    }

    public interface OnItemClick {
        void onClick(String category);
    }
}