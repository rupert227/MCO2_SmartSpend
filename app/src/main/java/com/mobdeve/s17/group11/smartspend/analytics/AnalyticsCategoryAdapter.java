package com.mobdeve.s17.group11.smartspend.analytics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s17.group11.smartspend.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AnalyticsCategoryAdapter extends RecyclerView.Adapter<AnalyticsCategoryAdapter.ViewHolder> {

    private List<CategoryBreakdownItem> items = new ArrayList<>();

    public static class CategoryBreakdownItem {
        String name;
        float amount;
        int color;

        public CategoryBreakdownItem(String name, float amount, int color) {
            this.name = name;
            this.amount = amount;
            this.color = color;
        }
    }

    public void submitItems(List<CategoryBreakdownItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_analytics_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryBreakdownItem item = items.get(position);

        holder.tvName.setText(item.name);
        holder.tvAmount.setText(String.format(Locale.getDefault(), "%.2f", item.amount));
        holder.vColor.setBackgroundColor(item.color);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAmount;
        View vColor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_category_name);
            tvAmount = itemView.findViewById(R.id.tv_category_amount);
            vColor = itemView.findViewById(R.id.v_category_color);
        }
    }
}