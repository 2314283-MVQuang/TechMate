package vn.edu.dlu.ctk47.techmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CompareAdapter extends RecyclerView.Adapter<CompareAdapter.ViewHolder> {

    private List<Spec> list;

    public CompareAdapter(List<Spec> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_compare, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Spec s = list.get(position);

        h.txtLabel.setText(s.label != null ? s.label : "");
        h.txtValue1.setText(s.value1 != null ? s.value1 : "N/A");
        h.txtValue2.setText(s.value2 != null ? s.value2 : "N/A");

        // Highlight khác biệt an toàn
        LinearLayout row = h.itemView.findViewById(R.id.rowRoot);
        if (s.value1 != null && !s.value1.equals(s.value2)) {
            row.setBackgroundColor(0xFFE0F2FE); // Xanh nhạt khi khác nhau
        } else {
            row.setBackgroundColor(0x00000000); // Trong suốt khi giống nhau
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtLabel, txtValue1, txtValue2;

        public ViewHolder(@NonNull View v) {
            super(v);
            txtLabel = v.findViewById(R.id.txtLabel);
            txtValue1 = v.findViewById(R.id.txtValue1);
            txtValue2 = v.findViewById(R.id.txtValue2);
        }
    }
}