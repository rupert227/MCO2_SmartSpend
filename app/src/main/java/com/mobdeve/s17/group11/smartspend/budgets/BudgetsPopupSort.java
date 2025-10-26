package com.mobdeve.s17.group11.smartspend.budgets;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s17.group11.smartspend.R;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesCategory;

import java.util.Arrays;

public class BudgetsPopupSort {

    private final Activity activity;

    public BudgetSortAdapter budgetSortAdapter;
    public PopupWindow popupWindow;
    public RecyclerView budgetsSortRecyclerView;
    public View view;

    public BudgetsPopupSort(Activity activity) {
        this.activity = activity;

        initViews();
        initRecyclerViews();
        initPopups();
    }

    private void initViews() {
        view = activity.getLayoutInflater().inflate(R.layout.popup_expenses_sort, null);

        budgetsSortRecyclerView = view.findViewById(R.id.rv_category);
    }

    private void initRecyclerViews() {
        budgetSortAdapter = new BudgetSortAdapter();

        Arrays.stream(ExpensesCategory.values()).forEach(category -> budgetSortAdapter.items.add(category.getDisplayName()));

        budgetsSortRecyclerView.setLayoutManager(new LinearLayoutManager(
                view.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        ));

        budgetsSortRecyclerView.setAdapter(budgetSortAdapter);
    }

    private void initPopups() {
        popupWindow = new PopupWindow(
                view,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popupWindow.setAnimationStyle(0);
    }

}
