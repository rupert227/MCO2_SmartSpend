package com.mobdeve.s17.group11.smartspend.expenses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s17.group11.smartspend.R;
import com.mobdeve.s17.group11.smartspend.util.DateHelper;
import com.mobdeve.s17.group11.smartspend.util.FormatHelper;

import java.util.ArrayList;
import java.util.List;

public class ExpensesListAdapter extends RecyclerView.Adapter<ExpensesListAdapter.ExpensesListItemViewHolder> {

    public List<ExpensesListItem> items = new ArrayList<>();

    private final Context context;

    public ExpensesListAdapter(Context context) {
        this.context = context;
    }

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
        ExpensesListItem expense = items.get(position);

        holder.itemView.setOnClickListener(null);
        holder.tvAmount.setText(FormatHelper.floatToPrice(expense.amount));
        holder.tvCategory.setText(ExpensesCategory.getExpensesCategoryName(expense.expensesCategoryID));
        holder.tvDate.setText(DateHelper.numericalDateTransform0(expense.date));

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ExpensesEditActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            ExpensesEditActivity.expenseEdit = expense;

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ExpensesListItemViewHolder extends RecyclerView.ViewHolder {

        public TextView tvAmount, tvCategory, tvDate;

        public ExpensesListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvCategory = itemView.findViewById(R.id.tv_header);
            tvDate = itemView.findViewById(R.id.tv_date);
        }

    }

}
