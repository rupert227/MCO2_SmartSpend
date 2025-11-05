package com.mobdeve.s17.group11.smartspend.budgets;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s17.group11.smartspend.R;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesCategory;
import com.mobdeve.s17.group11.smartspend.util.Algorithm;
import com.mobdeve.s17.group11.smartspend.util.SessionCache;
import com.mobdeve.s17.group11.smartspend.util.SortSettingsVariables;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

@SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
public class BudgetsPopupSort {

    public PopupWindow popupWindow;

    private BudgetsSortAdapter budgetsSortAdapter;
    private Button btnApply;
    private CheckBox cbHighest;
    private CheckBox cbLowest;
    private CheckBox cbNewest;
    private CheckBox cbOldest;
    private EditText tfHighest;
    private EditText tfLowest;
    private ImageButton btnAmountClear;
    private ImageButton btnCategoryClear;
    private ImageButton btnDateClear;
    private RecyclerView budgetsSortRecyclerView;
    private View view;

    private final BudgetsActivity budgetsActivity;
    private final SortSettingsVariables settings = SessionCache.budgetsSortSettings;

    public BudgetsPopupSort(BudgetsActivity budgetsActivity) {
        this.budgetsActivity = budgetsActivity;

        initViews();
        initListeners();
        initRecyclerViews();
        initPopups();
    }

    public void applySettings(SortSettingsVariables settings) {
        float highestAmount = settings.highestAmount;
        float lowestAmount = settings.lowestAmount;

        cbHighest.setChecked(settings.highestFirst);
        cbLowest.setChecked(settings.lowestFirst);
        cbNewest.setChecked(settings.newestFirst);
        cbOldest.setChecked(settings.oldestFirst);

        budgetsSortAdapter.selectedCategories.clear();
        budgetsSortAdapter.selectedCategories.addAll(settings.selectedCategories);
        budgetsSortAdapter.notifyDataSetChanged();

        tfHighest.setText(highestAmount > 0f ? Float.toString(highestAmount) : "");
        tfLowest.setText(lowestAmount > 0f ? Float.toString(lowestAmount) : "");
    }

    public void applySort() {
        btnApply.callOnClick();
    }

    private void initViews() {
        view = budgetsActivity.getLayoutInflater().inflate(R.layout.popup_expenses_sort, null);

        btnAmountClear = view.findViewById(R.id.btn_amount_clear);
        btnApply = view.findViewById(R.id.btn_apply);
        btnCategoryClear = view.findViewById(R.id.btn_category_clear);
        btnDateClear = view.findViewById(R.id.btn_date_clear);
        budgetsSortRecyclerView = view.findViewById(R.id.rv_category);
        cbHighest = view.findViewById(R.id.cb_highest);
        cbLowest = view.findViewById(R.id.cb_lowest);
        cbNewest = view.findViewById(R.id.cb_newest);
        cbOldest = view.findViewById(R.id.cb_oldest);
        tfHighest = view.findViewById(R.id.tf_highest);
        tfLowest = view.findViewById(R.id.tf_lowest);
    }

    private void initListeners() {
        btnAmountClear.setOnClickListener(view -> {
            cbHighest.setChecked(false);
            cbLowest.setChecked(false);

            tfHighest.setText("");
            tfLowest.setText("");
        });

        btnApply.setOnClickListener(view -> {
            String tfHighestInput = tfHighest.getText().toString().trim();
            String tfLowestInput = tfLowest.getText().toString().trim();

            settings.highestAmount = Algorithm.isPositiveDecimal(tfHighestInput)
                    ? Float.parseFloat(tfHighestInput) : 0f;
            settings.lowestAmount = Algorithm.isPositiveDecimal(tfLowestInput)
                    ? Float.parseFloat(tfLowestInput) : 0f;

            settings.highestFirst = cbHighest.isChecked();
            settings.lowestFirst = cbLowest.isChecked();
            settings.newestFirst = cbNewest.isChecked();
            settings.oldestFirst = cbOldest.isChecked();

            settings.selectedCategories.clear();
            settings.selectedCategories.addAll(budgetsSortAdapter.selectedCategories);

            budgetsActivity.budgetsListAdapter.items.clear();

            if(settings.isDefault()) {
                budgetsActivity.budgetsListAdapter.items.addAll(SessionCache.budgetsItems);
                budgetsActivity.budgetsListAdapter.notifyDataSetChanged();

                budgetsActivity.validateUI();
                BudgetsActivity.budgetsPopupSortRef.get().budgetsSortRecyclerView.scrollToPosition(0);
                popupWindow.dismiss();
                return;
            }

            budgetsActivity.budgetsListAdapter.items.addAll(SessionCache.budgetsItems.stream()
                    .filter(item -> {
                        if(!settings.selectedCategories.isEmpty()
                                && !settings.selectedCategories.contains(item.expensesCategoryID))
                            return false;

                        if(settings.highestAmount > 0f && item.amount > settings.highestAmount)
                            return false;

                        return settings.lowestAmount <= 0f || item.amount >= settings.lowestAmount;
                    }).collect(Collectors.toList())
            );

            Comparator<BudgetsListItem> comparator = null;

            if(settings.oldestFirst || settings.newestFirst) {
                comparator = Comparator.comparing(
                        (BudgetsListItem item) -> item.startDate.getUniqueValue(),
                        settings.oldestFirst ? Comparator.naturalOrder() : Comparator.reverseOrder()
                );
            }

            if(settings.lowestFirst || settings.highestFirst) {
                Comparator<BudgetsListItem> amountComparator = Comparator.comparing(
                        (BudgetsListItem item) -> item.amount,
                        settings.lowestFirst ? Comparator.naturalOrder() : Comparator.reverseOrder()
                );

                if(comparator == null)
                    comparator = amountComparator;
                else
                    comparator = comparator.thenComparing(amountComparator);
            }

            if(comparator != null)
                budgetsActivity.budgetsListAdapter.items.sort(comparator);

            budgetsActivity.budgetsListAdapter.notifyDataSetChanged();

            budgetsActivity.validateUI();
            BudgetsActivity.budgetsPopupSortRef.get().budgetsSortRecyclerView.scrollToPosition(0);
            popupWindow.dismiss();
        });

        btnCategoryClear.setOnClickListener(view -> {
            budgetsSortAdapter.selectedCategories.clear();
            budgetsSortAdapter.notifyDataSetChanged();
        });

        btnDateClear.setOnClickListener(view -> {
            cbNewest.setChecked(false);
            cbOldest.setChecked(false);
        });

        cbHighest.setOnCheckedChangeListener((CompoundButton view, boolean isChecked) -> {
            if(isChecked)
                cbLowest.setChecked(false);
        });

        cbLowest.setOnCheckedChangeListener((CompoundButton view, boolean isChecked) -> {
            if(isChecked)
                cbHighest.setChecked(false);
        });

        cbNewest.setOnCheckedChangeListener((CompoundButton view, boolean isChecked) -> {
            if(isChecked)
                cbOldest.setChecked(false);
        });

        cbOldest.setOnCheckedChangeListener((CompoundButton view, boolean isChecked) -> {
            if(isChecked)
                cbNewest.setChecked(false);
        });
    }

    private void initRecyclerViews() {
        budgetsSortAdapter = new BudgetsSortAdapter();

        Arrays.stream(ExpensesCategory.getListOrder()).forEach(categoryID ->
                budgetsSortAdapter.items.add(categoryID)
        );

        budgetsSortRecyclerView.setLayoutManager(new LinearLayoutManager(
                view.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        ));

        budgetsSortRecyclerView.setAdapter(budgetsSortAdapter);
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
