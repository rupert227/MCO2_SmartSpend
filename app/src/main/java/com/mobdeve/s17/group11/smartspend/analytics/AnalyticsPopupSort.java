package com.mobdeve.s17.group11.smartspend.analytics;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.mikephil.charting.data.PieEntry;
import com.mobdeve.s17.group11.smartspend.R;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesCategory;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesListItem;
import com.mobdeve.s17.group11.smartspend.util.Date;
import com.mobdeve.s17.group11.smartspend.util.DateHelper;
import com.mobdeve.s17.group11.smartspend.util.PopupSettingsVariables;
import com.mobdeve.s17.group11.smartspend.util.SessionCache;
import com.mobdeve.s17.group11.smartspend.util.UIUtils;

import java.util.Arrays;

@SuppressLint({"InflateParams", "NotifyDataSetChanged", "SetTextI18n"})
public class AnalyticsPopupSort {

    public PopupWindow popupWindow;

    private Button btnApply;
    private EditText tfDateEndDay, tfDateEndMonth, tfDateEndYear;
    private EditText tfDateStartDay, tfDateStartMonth, tfDateStartYear;
    private ImageButton btnDateClear;
    private ImageButton btnDateEndCalendar;
    private ImageButton btnDateStartCalendar;
    private TextView tvDateEndPrompt;
    private TextView tvDateStartPrompt;
    private View view;

    private final AnalyticsActivity analyticsActivity;
    private final PopupSettingsVariables.Analytics settings = SessionCache.analyticsSortSettings;

    public AnalyticsPopupSort(AnalyticsActivity analyticsActivity) {
        this.analyticsActivity = analyticsActivity;

        initViews();
        initListeners();
        initPopups();
    }

    private void initViews() {
        view = analyticsActivity.getLayoutInflater().inflate(R.layout.popup_analytics_sort, null);

        btnApply = view.findViewById(R.id.btn_apply);
        btnDateClear = view.findViewById(R.id.btn_date_clear);
        btnDateEndCalendar = view.findViewById(R.id.btn_date_end_calendar);
        btnDateStartCalendar = view.findViewById(R.id.btn_date_start_calendar);
        tfDateEndDay = view.findViewById(R.id.tf_date_end_day);
        tfDateEndMonth = view.findViewById(R.id.tf_date_end_month);
        tfDateEndYear = view.findViewById(R.id.tf_date_end_year);
        tfDateStartDay = view.findViewById(R.id.tf_date_start_day);
        tfDateStartMonth = view.findViewById(R.id.tf_date_start_month);
        tfDateStartYear = view.findViewById(R.id.tf_date_start_year);
        tvDateEndPrompt = view.findViewById(R.id.tv_date_end_prompt);
        tvDateStartPrompt = view.findViewById(R.id.tv_date_start_prompt);
    }

    private void initListeners() {
        btnApply.setOnClickListener(view -> {
            boolean validFields;

            validFields = UIUtils.Validator.validateDateFields(
                    tfDateStartDay, tfDateStartMonth, tfDateStartYear, tvDateStartPrompt
            );

            validFields &= UIUtils.Validator.validateDateFields(
                    tfDateEndDay, tfDateEndMonth, tfDateEndYear, tvDateEndPrompt
            );

            if(!validFields)
                return;

            Date endDate = new Date(
                    Integer.parseInt(tfDateEndDay.getText().toString().trim()),
                    Integer.parseInt(tfDateEndMonth.getText().toString().trim()),
                    Integer.parseInt(tfDateEndYear.getText().toString().trim())
            );

            Date startDate = new Date(
                    Integer.parseInt(tfDateStartDay.getText().toString().trim()),
                    Integer.parseInt(tfDateStartMonth.getText().toString().trim()),
                    Integer.parseInt(tfDateStartYear.getText().toString().trim())
            );

            if(endDate.getUniqueValue() < startDate.getUniqueValue()) {
                tvDateEndPrompt.setText("End Date < Start Date");
                tvDateEndPrompt.setVisibility(TextView.VISIBLE);
                return;
            }

            tvDateEndPrompt.setVisibility(TextView.GONE);

            settings.endDate = endDate;
            settings.startDate = startDate;

            analyticsActivity.tvCaption.setText(
                    DateHelper.numericalDateTransform1(settings.startDate)
                    + " - "
                    + DateHelper.numericalDateTransform1(settings.endDate)
            );

            analyticsActivity.totalExpensesAmount = 0;

            analyticsActivity.analyticsListAdapter.items.clear();
            analyticsActivity.expenseCategoryAmountMap.clear();

            analyticsActivity.pieColors.clear();
            analyticsActivity.pieEntries.clear();

            for(ExpensesListItem expense : SessionCache.expensesItems) {
                if(expense.date.getUniqueValue() < settings.startDate.getUniqueValue()
                        || expense.date.getUniqueValue() > settings.endDate.getUniqueValue())
                    continue;

                if(analyticsActivity.expenseCategoryAmountMap.containsKey(expense.expensesCategoryID)) {
                    analyticsActivity.expenseCategoryAmountMap.put(expense.expensesCategoryID,
                            analyticsActivity.expenseCategoryAmountMap.get(expense.expensesCategoryID)
                                    + expense.amount
                    );
                } else {
                    if(expense.amount > 0)
                        analyticsActivity.expenseCategoryAmountMap.put(
                                expense.expensesCategoryID, expense.amount
                        );
                }

                analyticsActivity.totalExpensesAmount += expense.amount;
            }

            Arrays.stream(ExpensesCategory.getListOrder()).forEach(expensesCategoryID -> {
                if(analyticsActivity.expenseCategoryAmountMap.containsKey(expensesCategoryID)) {
                    float amount = analyticsActivity.expenseCategoryAmountMap.get(expensesCategoryID);

                    analyticsActivity.analyticsListAdapter.items.add(
                            new AnalyticsListItem(expensesCategoryID, amount)
                    );

                    analyticsActivity.pieColors.add(
                            255 << 24 | ExpensesCategory.getExpensesCategoryColor(expensesCategoryID)
                    );

                    analyticsActivity.pieEntries.add(
                            new PieEntry(amount, ExpensesCategory.getExpensesCategoryName(expensesCategoryID))
                    );
                }
            });

            analyticsActivity.analyticsListAdapter.notifyDataSetChanged();

            analyticsActivity.pieAnalytics.notifyDataSetChanged();
            analyticsActivity.pieAnalytics.invalidate();
            analyticsActivity.pieAnalytics.highlightValue(null);

            analyticsActivity.useUnselectedCategoryDetails();
            analyticsActivity.validateUI();

            popupWindow.dismiss();
        });

        btnDateClear.setOnClickListener(view -> {
            findDateRange();
        });

        btnDateEndCalendar.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    analyticsActivity,

                    (DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) -> {
                        tfDateEndDay.setText(Integer.toString(selectedDay));
                        tfDateEndMonth.setText(Integer.toString(selectedMonth + 1));
                        tfDateEndYear.setText(Integer.toString(selectedYear));
                    },

                    Integer.parseInt(tfDateEndYear.getText().toString().trim()),
                    Integer.parseInt(tfDateEndMonth.getText().toString().trim()) - 1,
                    Integer.parseInt(tfDateEndDay.getText().toString().trim())
            );

            datePickerDialog.show();
        });

        btnDateStartCalendar.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    analyticsActivity,

                    (DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) -> {
                        tfDateStartDay.setText(Integer.toString(selectedDay));
                        tfDateStartMonth.setText(Integer.toString(selectedMonth + 1));
                        tfDateStartYear.setText(Integer.toString(selectedYear));
                    },

                    Integer.parseInt(tfDateStartYear.getText().toString().trim()),
                    Integer.parseInt(tfDateStartMonth.getText().toString().trim()) - 1,
                    Integer.parseInt(tfDateStartDay.getText().toString().trim())
            );

            datePickerDialog.show();
        });
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

    public void applySettings(PopupSettingsVariables.Analytics settings) {
        if(!settings.hasSetOnce) {
            findDateRange();
            settings.hasSetOnce = true;
            return;
        }

        tfDateEndDay.setText(Integer.toString(settings.endDate.day));
        tfDateEndMonth.setText(Integer.toString(settings.endDate.month));
        tfDateEndYear.setText(Integer.toString(settings.endDate.year));
        tfDateStartDay.setText(Integer.toString(settings.startDate.day));
        tfDateStartMonth.setText(Integer.toString(settings.startDate.month));
        tfDateStartYear.setText(Integer.toString(settings.startDate.year));
    }

    public void applySort() {
        btnApply.callOnClick();
    }

    private void findDateRange() {
        Date endDate = DateHelper.getCurrentDate();
        Date startDate = DateHelper.getCurrentDate();

        if(!SessionCache.expensesItems.isEmpty()) {
            int endDateVal = SessionCache.expensesItems.get(0).date.getUniqueValue();
            int startDateVal = SessionCache.expensesItems.get(0).date.getUniqueValue();

            for(ExpensesListItem expense : SessionCache.expensesItems) {
                if(expense.date.getUniqueValue() < startDateVal)
                    startDateVal = expense.date.getUniqueValue();

                if(expense.date.getUniqueValue() > endDateVal)
                    endDateVal = expense.date.getUniqueValue();
            }

            endDate = new Date(endDateVal);
            startDate = new Date(startDateVal);
        }

        tfDateEndDay.setText(Integer.toString(endDate.day));
        tfDateEndMonth.setText(Integer.toString(endDate.month));
        tfDateEndYear.setText(Integer.toString(endDate.year));
        tfDateStartDay.setText(Integer.toString(startDate.day));
        tfDateStartMonth.setText(Integer.toString(startDate.month));
        tfDateStartYear.setText(Integer.toString(startDate.year));
    }

}
