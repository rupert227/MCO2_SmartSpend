package com.mobdeve.s17.group11.smartspend.budgets;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s17.group11.smartspend.R;

import java.util.ArrayList;
import java.util.List;

public class BudgetSortAdapter extends RecyclerView.Adapter<BudgetSortAdapter.budgetsSortListItemViewHolder> {

    public List<String> items = new ArrayList<>();

    @NonNull
    @Override
    public budgetsSortListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new budgetsSortListItemViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_expenses_sort_list,parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull budgetsSortListItemViewHolder holder, int position) {
        String categoryLabel = items.get(position);

        holder.chOption.setText(categoryLabel);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class budgetsSortListItemViewHolder extends RecyclerView.ViewHolder {

        public CheckBox chOption;
        public budgetsSortListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            chOption = itemView.findViewById(R.id.ch_option);
        }

    }

}