package com.mobdeve.s17.group11.smartspend.analytics;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.data.PieEntry;
import com.mobdeve.s17.group11.smartspend.R;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesCategory;
import com.mobdeve.s17.group11.smartspend.util.FormatHelper;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("SetTextI18n")
public class AnalyticsListAdapter extends RecyclerView.Adapter<AnalyticsListAdapter.AnalyticsListItemViewHolder> {

    public List<AnalyticsListItem> items = new ArrayList<>();

    private final AnalyticsActivity analyticsActivity;

    public AnalyticsListAdapter(AnalyticsActivity analyticsActivity) {
        this.analyticsActivity = analyticsActivity;
    }

    @NonNull
    @Override
    public AnalyticsListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AnalyticsListItemViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_analytics_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AnalyticsListItemViewHolder holder, int position) {
        AnalyticsListItem analytic = items.get(position);

        holder.itemView.setOnClickListener(null);
        holder.tvAmount.setText(FormatHelper.floatToPrice(analytic.amount));
        holder.tvCategory.setText(ExpensesCategory.getExpensesCategoryName(analytic.expensesCategoryID));
        holder.viBar.setBackgroundTintList(ColorStateList.valueOf(
                ExpensesCategory.getExpensesCategoryColor(analytic.expensesCategoryID)
        ));

        holder.itemView.setOnClickListener(view -> {
            int index = -1;

            for(int i = 0; i < analyticsActivity.pieEntries.size(); i++) {
                PieEntry entry = analyticsActivity.pieEntries.get(i);

                if(entry.getLabel().equalsIgnoreCase(ExpensesCategory.getExpensesCategoryName(analytic.expensesCategoryID))) {
                    index = i;
                    break;
                }
            }

            if(index >= 0) {
                float amount = analyticsActivity.expenseCategoryAmountMap.get(analytic.expensesCategoryID);

                analyticsActivity.pieAnalytics.highlightValue(index, 0);

                analyticsActivity.tvAmount.setText(FormatHelper.floatToPrice(amount));
                analyticsActivity.tvCategory.setText(ExpensesCategory.getExpensesCategoryName(analytic.expensesCategoryID));
                analyticsActivity.tvPercentage.setText(
                        FormatHelper.floatToPrice((amount / analyticsActivity.totalExpensesAmount) * 100.0f) + "%"
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class AnalyticsListItemViewHolder extends RecyclerView.ViewHolder {

        public TextView tvAmount, tvCategory;
        public View viBar;

        public AnalyticsListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAmount = itemView.findViewById(R.id.tv_amount_current);
            tvCategory = itemView.findViewById(R.id.tv_header);
            viBar = itemView.findViewById(R.id.vi_bar);
        }

    }

}
