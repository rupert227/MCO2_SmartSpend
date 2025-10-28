package com.mobdeve.s17.group11.smartspend.expenses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s17.group11.smartspend.R;
import com.mobdeve.s17.group11.smartspend.util.FormatHelper;
import com.mobdeve.s17.group11.smartspend.util.DateHelper;

import java.util.ArrayList;
import java.util.List;

public class ExpensesListAdapter extends RecyclerView.Adapter<ExpensesListAdapter.ExpensesListItemViewHolder> {

    private final Context context;

    public ExpensesListAdapter(Context context) {
        this.context = context;
    }

    public List<ExpensesListItem> items = new ArrayList<>();

    @NonNull
    @Override
    public ExpensesListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExpensesListItemViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_expenses_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesListItemViewHolder holder, int position) {
        ExpensesListItem expensesListItem = items.get(position);

        holder.tvCategory.setText(expensesListItem.expensesCategory.getDisplayName());
        holder.tvDate.setText(DateHelper.numericalDateTransform0(expensesListItem.date));
        holder.tvPrice.setText(FormatHelper.floatToPrice(expensesListItem.price));

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ExpensesEditActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ExpensesListItemViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCategory, tvDate, tvPrice;
        public ImageButton btnDelete;

        public ExpensesListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCategory = itemView.findViewById(R.id.tv_header);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }

    }

}