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
import com.mobdeve.s17.group11.smartspend.util.SessionCache;

import java.util.ArrayList;
import java.util.List;

public class BudgetsListAdapter extends RecyclerView.Adapter<BudgetsListAdapter.budgetsListItemViewHolder> {

    public List<BudgetsListItem> items = new ArrayList<>();

    private final Context context;

    public BudgetsListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public budgetsListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new budgetsListItemViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_budgets_list, parent, false)
        );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull budgetsListItemViewHolder holder, int position) {
        BudgetsListItem budget = items.get(position);

        holder.itemView.setOnClickListener(null);
        holder.tvCurrentAmount.setText(FormatHelper.floatToPrice(budget.currentAmount));

        holder.tvCategory.setText(ExpensesCategory.getExpensesCategoryName(budget.expensesCategoryID));

        holder.tvDate.setText(
                DateHelper.numericalDateTransform1(budget.startDate)
                        + " - "
                        + DateHelper.numericalDateTransform1(budget.endDate)
        );

        holder.tvMaxAmount.setText(FormatHelper.floatToPrice(budget.maxAmount));

        if(budget.currentAmount <= budget.maxAmount)
            holder.tvCurrentAmount.setTextColor(SessionCache.Color.tvLabel1);
        else
            holder.tvCurrentAmount.setTextColor(SessionCache.Color.tvDanger);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, BudgetsEditActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            BudgetsEditActivity.budgetEdit = budget;

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class budgetsListItemViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCategory, tvCurrentAmount, tvDate, tvMaxAmount;

        public budgetsListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCurrentAmount = itemView.findViewById(R.id.tv_amount_current);
            tvCategory = itemView.findViewById(R.id.tv_header);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvMaxAmount = itemView.findViewById(R.id.tv_amount_max);
        }

    }

}
