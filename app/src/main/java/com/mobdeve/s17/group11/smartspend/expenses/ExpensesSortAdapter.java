package com.mobdeve.s17.group11.smartspend.expenses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s17.group11.smartspend.R;

import java.util.ArrayList;
import java.util.List;

public class ExpensesSortAdapter extends RecyclerView.Adapter<ExpensesSortAdapter.ExpensesSortListItemViewHolder> {

    public List<String> items = new ArrayList<>();

    @NonNull
    @Override
    public ExpensesSortListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExpensesSortListItemViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_expenses_sort_list,parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesSortListItemViewHolder holder, int position) {
        String categoryLabel = items.get(position);

        holder.chOption.setText(categoryLabel);
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