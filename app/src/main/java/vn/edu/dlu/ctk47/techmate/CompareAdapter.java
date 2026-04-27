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

        h.txtLabel.setText(s.label);
        h.txtValue1.setText(s.value1);
        h.txtValue2.setText(s.value2);

        // 🔥 highlight nếu khác nhau
        if (!String.valueOf(s.value1).equals(String.valueOf(s.value2))) {
            h.rowRoot.setBackgroundColor(0xFFE0F2FE);
        } else {
            h.rowRoot.setBackgroundColor(0x00000000);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtLabel, txtValue1, txtValue2;
        LinearLayout rowRoot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtLabel = itemView.findViewById(R.id.txtLabel);
            txtValue1 = itemView.findViewById(R.id.txtValue1);
            txtValue2 = itemView.findViewById(R.id.txtValue2);
            rowRoot = itemView.findViewById(R.id.rowRoot);
        }
    }
}