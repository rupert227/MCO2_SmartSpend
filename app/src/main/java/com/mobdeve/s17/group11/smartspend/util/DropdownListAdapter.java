package com.mobdeve.s17.group11.smartspend.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s17.group11.smartspend.R;

import java.util.ArrayList;
import java.util.List;

public class DropdownListAdapter extends RecyclerView.Adapter<DropdownListAdapter.DropdownListItemViewHolder> {

    public List<String> items = new ArrayList<>();
    public View.OnClickListener itemClick = null;

    @NonNull
    @Override
    public DropdownListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DropdownListItemViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_dropdown_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull DropdownListItemViewHolder holder, int position) {
        String categoryLabel = items.get(position);

        holder.tvLabel.setText(categoryLabel);

        holder.itemView.setOnClickListener(view -> {
            if(itemClick != null)
                itemClick.onClick(view);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class DropdownListItemViewHolder extends RecyclerView.ViewHolder {

        public TextView tvLabel;
        public DropdownListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLabel = itemView.findViewById(R.id.tv_label);
        }

    }

}
