package vn.edu.dlu.ctk47.techmate;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryIconAdapter extends RecyclerView.Adapter<CategoryIconAdapter.ViewHolder> {

    private List<CategoryModel> list;
    private int selectedPos = 0;
    private OnItemClick listener;

    public CategoryIconAdapter(List<CategoryModel> list, OnItemClick listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_icon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryModel item = list.get(position);
        holder.txtName.setText(item.name);

        // Gắn ảnh từ resource ID
        if (item.iconResId != 0) {
            holder.imgIcon.setImageResource(item.iconResId);
        }

        // Đổi màu viền/nền nếu đang được chọn (để người dùng biết đang ở mục nào)
        if (selectedPos == position) {
            holder.layoutBg.setBackgroundResource(R.drawable.bg_chip_selected);
            holder.txtName.setTextColor(Color.parseColor("#28C8BE")); // Màu primary
            holder.txtName.setTypeface(null, android.graphics.Typeface.BOLD);
        } else {
            holder.layoutBg.setBackgroundResource(R.drawable.bg_chip);
            holder.txtName.setTextColor(Color.parseColor("#1F2937")); // text_primary
            holder.txtName.setTypeface(null, android.graphics.Typeface.NORMAL);
        }

        holder.itemView.setOnClickListener(v -> {
            int oldPos = selectedPos;
            selectedPos = holder.getBindingAdapterPosition();
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
        ImageView imgIcon;
        TextView txtName;
        LinearLayout layoutBg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            txtName = itemView.findViewById(R.id.txtName);
            layoutBg = itemView.findViewById(R.id.layoutBg);
        }
    }

    public interface OnItemClick {
        void onClick(CategoryModel category);
    }
}