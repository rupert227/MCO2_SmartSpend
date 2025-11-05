package com.mobdeve.s17.group11.smartspend.expenses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s17.group11.smartspend.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExpensesSortAdapter extends RecyclerView.Adapter<ExpensesSortAdapter.ExpensesSortListItemViewHolder> {

    public List<Integer> items = new ArrayList<>();
    public Set<Integer> selectedCategories = new HashSet<>();

    @NonNull
    @Override
    public ExpensesSortListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExpensesSortListItemViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_categories_sort_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesSortListItemViewHolder holder, int position) {
        int categoryID = items.get(position);

        holder.chOption.setOnCheckedChangeListener(null);
        holder.chOption.setChecked(selectedCategories.contains(categoryID));

        holder.chOption.setOnCheckedChangeListener((CompoundButton view, boolean isChecked) -> {
            if(isChecked)
                selectedCategories.add(categoryID);
            else
                selectedCategories.remove(categoryID);
        });

        holder.chOption.setText(ExpensesCategory.getExpensesCategoryName(categoryID));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ExpensesSortListItemViewHolder extends RecyclerView.ViewHolder {

        public CheckBox chOption;

        public ExpensesSortListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            chOption = itemView.findViewById(R.id.ch_option);
        }

    }

}
