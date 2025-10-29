package com.mobdeve.s17.group11.smartspend.budgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s17.group11.smartspend.R;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesCategory;
import com.mobdeve.s17.group11.smartspend.util.DateHelper;
import com.mobdeve.s17.group11.smartspend.util.FormatHelper;

import java.util.ArrayList;
import java.util.List;

public class BudgetsListAdapter extends RecyclerView.Adapter<BudgetsListAdapter.budgetsListItemViewHolder> {

    private final Context context;

    public BudgetsListAdapter(Context context) {
        this.context = context;
    }

    public List<BudgetsListItem> items = new ArrayList<>();

    @NonNull
    @Override
    public budgetsListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new budgetsListItemViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_expenses_list, parent, false)
        );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull budgetsListItemViewHolder holder, int position) {
        BudgetsListItem budgetsListItem = items.get(position);

        holder.tvCategory.setText(ExpensesCategory.getExpenseCategoryName(budgetsListItem.budgetCategoryID));

        holder.tvDate.setText(
                DateHelper.numericalDateTransform1(budgetsListItem.startDate)
                        + " - "
                        + DateHelper.numericalDateTransform1(budgetsListItem.endDate)
        );

        holder.tvPrice.setText(FormatHelper.floatToPrice(budgetsListItem.price));

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, BudgetsEditActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class budgetsListItemViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCategory, tvDate, tvPrice;

        public budgetsListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCategory = itemView.findViewById(R.id.tv_header);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }

    }

}