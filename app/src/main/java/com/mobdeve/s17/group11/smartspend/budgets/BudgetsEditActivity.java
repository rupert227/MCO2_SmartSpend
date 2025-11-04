package com.mobdeve.s17.group11.smartspend.budgets;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s17.group11.smartspend.R;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesCategory;
import com.mobdeve.s17.group11.smartspend.util.Date;
import com.mobdeve.s17.group11.smartspend.util.DropdownComposite;
import com.mobdeve.s17.group11.smartspend.util.IntentVariables;
import com.mobdeve.s17.group11.smartspend.util.NavigationBar;
import com.mobdeve.s17.group11.smartspend.util.SessionCache;
import com.mobdeve.s17.group11.smartspend.util.UIUtils;

import java.lang.ref.WeakReference;
import java.util.Arrays;

@SuppressLint("SetTextI18n")
public class BudgetsEditActivity extends AppCompatActivity {

    public static IntentVariables.BudgetEdit intentVariables = new IntentVariables.BudgetEdit();
    public static Runnable exitListener;
    public static WeakReference<RecyclerView> rvBudgetsListRef;

    private Button btnSave;
    private DropdownComposite categoryDropdownComposite = new DropdownComposite();
    private EditText tfAmount;
    private EditText tfCategory;
    private EditText tfDateEndDay, tfDateEndMonth, tfDateEndYear;
    private EditText tfDateStartDay, tfDateStartMonth, tfDateStartYear;
    private EditText tfNotes;
    private ImageButton btnBack;
    private ImageButton btnDateEndCalendar;
    private ImageButton btnDateStartCalendar;
    private TextView tvAmountPrompt;
    private TextView tvCategoryPrompt;
    private TextView tvDateEndPrompt, tvDateStartPrompt;
    private TextView tvDelete;
    private TextView tvNotesPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_budgets_edit);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NavigationBar.init(this);

        initViews();
        initListeners();
        initRecyclerViews();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_header_back);
        btnDateEndCalendar = findViewById(R.id.btn_date_end_calendar);
        btnDateStartCalendar = findViewById(R.id.btn_date_start_calendar);
        btnSave = findViewById(R.id.btn_save);
        tfAmount = findViewById(R.id.tf_amount);
        tfCategory = findViewById(R.id.tf_category);
        tfDateEndDay = findViewById(R.id.tf_date_end_day);
        tfDateEndMonth = findViewById(R.id.tf_date_end_month);
        tfDateEndYear = findViewById(R.id.tf_date_end_year);
        tfDateStartDay = findViewById(R.id.tf_date_start_day);
        tfDateStartMonth = findViewById(R.id.tf_date_start_month);
        tfDateStartYear = findViewById(R.id.tf_date_start_year);
        tfNotes = findViewById(R.id.tf_notes);
        tvAmountPrompt = findViewById(R.id.tv_amount_prompt);
        tvCategoryPrompt = findViewById(R.id.tv_category_prompt);
        tvDateEndPrompt = findViewById(R.id.tv_date_end_prompt);
        tvDateStartPrompt = findViewById(R.id.tv_date_start_prompt);
        tvDelete = findViewById(R.id.tv_delete);
        tvNotesPrompt = findViewById(R.id.tv_notes_prompt);

        tfAmount.setText(Float.toString(intentVariables.budget.amount));
        tfCategory.setText(ExpensesCategory.getExpensesCategoryName(intentVariables.budget.budgetsCategoryID));
        tfDateEndDay.setText(Integer.toString(intentVariables.budget.endDate.day));
        tfDateEndMonth.setText(Integer.toString(intentVariables.budget.endDate.month));
        tfDateEndYear.setText(Integer.toString(intentVariables.budget.endDate.year));
        tfDateStartDay.setText(Integer.toString(intentVariables.budget.startDate.day));
        tfDateStartMonth.setText(Integer.toString(intentVariables.budget.startDate.month));
        tfDateStartYear.setText(Integer.toString(intentVariables.budget.startDate.year));
        tfNotes.setText(intentVariables.budget.notes);
    }

    private void initListeners() {
        BudgetsListAdapter budgetsListAdapter = (BudgetsListAdapter) rvBudgetsListRef.get().getAdapter();
        assert budgetsListAdapter != null;

        btnBack.setOnClickListener(view -> {
            finish();
            overridePendingTransition(0, 0);
        });

        btnDateEndCalendar.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    BudgetsEditActivity.this,

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
                    BudgetsEditActivity.this,

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

        btnSave.setOnClickListener(view -> {
            boolean validFields;

            validFields = UIUtils.Validator.validateAmountField(tfAmount, tvAmountPrompt);
            validFields &= UIUtils.Validator.validateCategoryField(tfCategory, tvCategoryPrompt);
            validFields &= UIUtils.Validator.validateDateFields(tfDateStartDay, tfDateStartMonth, tfDateStartYear, tvDateStartPrompt);
            validFields &= UIUtils.Validator.validateDateFields(tfDateEndDay, tfDateEndMonth, tfDateEndYear, tvDateEndPrompt);

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

            BudgetsListItem budgetsListItem = budgetsListAdapter.items.get(intentVariables.listIndex);

            budgetsListItem.amount = Float.parseFloat(tfAmount.getText().toString().trim());
            budgetsListItem.endDate = endDate;
            budgetsListItem.startDate = startDate;
            budgetsListItem.budgetsCategoryID = ExpensesCategory.getExpensesCategoryID(tfCategory.getText().toString().trim());
            budgetsListItem.notes = tfNotes.getText().toString().trim();

            SessionCache.budgetsDatabase.updateBudget(budgetsListItem.sqlRowID, budgetsListItem);
            budgetsListAdapter.notifyItemChanged(intentVariables.listIndex);

            finish();
            overridePendingTransition(0, 0);

            if(exitListener != null)
                exitListener.run();
        });

        tvDelete.setOnClickListener(view -> {
            UIUtils.Dialog.showPrompt0(
                    view,
                    null,
                    "Delete Budget Entry",
                    "Are you sure you want to delete this entry?\nThis action cannot be undone.",
                    "Cancel",
                    "Delete",
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.btn_background_neutral)).getDefaultColor(),
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.btn_background_danger)).getDefaultColor(),
                    null,
                    btn1View -> {
                        SessionCache.budgetsDatabase.deleteBudget(intentVariables.budget.sqlRowID);
                        budgetsListAdapter.items.remove(intentVariables.listIndex);
                        budgetsListAdapter.notifyItemRemoved(intentVariables.listIndex);

                        finish();
                        overridePendingTransition(0, 0);

                        if(exitListener != null)
                            exitListener.run();
                    }
            );
        });
    }

    private void initRecyclerViews() {
        Arrays.stream(ExpensesCategory.getListOrder()).forEach(categoryID ->
            categoryDropdownComposite.items.add(ExpensesCategory.getExpensesCategoryName(categoryID))
        );

        UIUtils.CompositeInstantiator.categoryDropdown(categoryDropdownComposite, tfCategory);
    }

}
