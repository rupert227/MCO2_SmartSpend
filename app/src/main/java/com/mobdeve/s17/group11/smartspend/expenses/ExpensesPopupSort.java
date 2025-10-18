package com.mobdeve.s17.group11.smartspend.expenses;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s17.group11.smartspend.R;

import java.util.Arrays;

public class ExpensesPopupSort {

    private final Activity activity;

    public ExpensesSortAdapter expensesSortAdapter;
    public PopupWindow popupWindow;
    public RecyclerView expensesSortRecyclerView;
    public View view;

    public ExpensesPopupSort(Activity activity) {
        this.activity = activity;

        initViews();
        initRecyclerViews();
        initPopups();
    }

    private void initViews() {
        view = activity.getLayoutInflater().inflate(R.layout.popup_expenses_sort, null);

        expensesSortRecyclerView = view.findViewById(R.id.rv_category);
    }

    private void initRecyclerViews() {
        expensesSortAdapter = new ExpensesSortAdapter();

        Arrays.stream(ExpensesCategory.values()).forEach(category -> expensesSortAdapter.items.add(category.getDisplayName()));

        expensesSortRecyclerView.setLayoutManager(new LinearLayoutManager(
                view.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        ));

        expensesSortRecyclerView.setAdapter(expensesSortAdapter);
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
