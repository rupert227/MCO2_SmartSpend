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

import java.util.List;

public class BudgetsListAdapter extends RecyclerView.Adapter<BudgetsListAdapter.budgetsListItemViewHolder> {

    public List<BudgetsListItem> items;

    private final Context context;

    public BudgetsListAdapter(Context context) {
        this.context = context;
    }

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

        holder.tvAmount.setText(FormatHelper.floatToPrice(budgetsListItem.amount));
        holder.tvCategory.setText(ExpensesCategory.getExpensesCategoryName(budgetsListItem.budgetCategoryID));

        holder.tvDate.setText(
                DateHelper.numericalDateTransform1(budgetsListItem.startDate)
                        + " - "
                        + DateHelper.numericalDateTransform1(budgetsListItem.endDate)
        );

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, BudgetsEditActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            BudgetsEditActivity.fieldData.amount = budgetsListItem.amount;
            BudgetsEditActivity.fieldData.categoryID = budgetsListItem.budgetCategoryID;
            BudgetsEditActivity.fieldData.endDate = budgetsListItem.endDate;
            BudgetsEditActivity.fieldData.listIndex = holder.getAbsoluteAdapterPosition();
            BudgetsEditActivity.fieldData.notes = budgetsListItem.notes;
            BudgetsEditActivity.fieldData.startDate = budgetsListItem.startDate;
            BudgetsEditActivity.fieldData.use = true;

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class budgetsListItemViewHolder extends RecyclerView.ViewHolder {

        public TextView tvAmount, tvCategory, tvDate;

        public budgetsListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvCategory = itemView.findViewById(R.id.tv_header);
            tvDate = itemView.findViewById(R.id.tv_date);
        }

    }

}
