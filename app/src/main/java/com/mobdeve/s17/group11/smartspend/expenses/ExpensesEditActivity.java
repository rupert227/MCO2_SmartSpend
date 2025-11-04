package com.mobdeve.s17.group11.smartspend.expenses;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
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
import com.mobdeve.s17.group11.smartspend.util.Date;
import com.mobdeve.s17.group11.smartspend.util.DropdownComposite;
import com.mobdeve.s17.group11.smartspend.util.IntentVariables;
import com.mobdeve.s17.group11.smartspend.util.NavigationBar;
import com.mobdeve.s17.group11.smartspend.util.SessionCache;
import com.mobdeve.s17.group11.smartspend.util.UIUtils;

import java.lang.ref.WeakReference;
import java.util.Arrays;

public class ExpensesEditActivity extends AppCompatActivity {

    public static IntentVariables.ExpenseEdit intentVariables = new IntentVariables.ExpenseEdit();
    public static Runnable exitListener;
    public static WeakReference<RecyclerView> rvExpensesListRef;

    private Button btnSave;
    private DropdownComposite categoryDropdownComposite = new DropdownComposite();
    private EditText tfAmount;
    private EditText tfCategory;
    private EditText tfDateDay, tfDateMonth, tfDateYear;
    private EditText tfLocation;
    private EditText tfNotes;
    private ImageButton btnBack;
    private TextView tvAmountPrompt;
    private TextView tvCategoryPrompt;
    private TextView tvDatePrompt;
    private TextView tvDelete;
    private TextView tvLocationPrompt;
    private TextView tvNotesPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_expenses_edit);

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
        btnSave = findViewById(R.id.btn_save);
        tfAmount = findViewById(R.id.tf_amount);
        tfCategory = findViewById(R.id.tf_category);
        tfDateDay = findViewById(R.id.tf_date_day);
        tfDateMonth = findViewById(R.id.tf_date_month);
        tfDateYear = findViewById(R.id.tf_date_year);
        tfLocation = findViewById(R.id.tf_location);
        tfNotes = findViewById(R.id.tf_notes);
        tvAmountPrompt = findViewById(R.id.tv_amount_prompt);
        tvCategoryPrompt = findViewById(R.id.tv_category_prompt);
        tvDatePrompt = findViewById(R.id.tv_date_prompt);
        tvDelete = findViewById(R.id.tv_delete);
        tvLocationPrompt = findViewById(R.id.tv_location_prompt);
        tvNotesPrompt = findViewById(R.id.tv_notes_prompt);

        tfAmount.setText(Float.toString(intentVariables.expense.amount));
        tfCategory.setText(ExpensesCategory.getExpensesCategoryName(intentVariables.expense.expensesCategoryID));
        tfDateDay.setText(Integer.toString(intentVariables.expense.date.day));
        tfDateMonth.setText(Integer.toString(intentVariables.expense.date.month));
        tfDateYear.setText(Integer.toString(intentVariables.expense.date.year));
        tfLocation.setText(intentVariables.expense.location);
        tfNotes.setText(intentVariables.expense.notes);
    }

    private void initListeners() {
        ExpensesListAdapter expensesListAdapter = (ExpensesListAdapter) rvExpensesListRef.get().getAdapter();
        assert expensesListAdapter != null;

        btnBack.setOnClickListener(view -> {
            finish();
            overridePendingTransition(0, 0);
        });

        btnSave.setOnClickListener(view -> {
            boolean validFields;

            validFields = UIUtils.Validator.validateAmountField(tfAmount, tvAmountPrompt);
            validFields &= UIUtils.Validator.validateCategoryField(tfCategory, tvCategoryPrompt);
            validFields &= UIUtils.Validator.validateDateFields(tfDateDay, tfDateMonth, tfDateYear, tvDatePrompt);

            if(!validFields)
                return;

            ExpensesListItem expensesListItem = expensesListAdapter.items.get(intentVariables.listIndex);

            expensesListItem.amount = Float.parseFloat(tfAmount.getText().toString().trim());

            expensesListItem.date = new Date(
                    Integer.parseInt(tfDateDay.getText().toString().trim()),
                    Integer.parseInt(tfDateMonth.getText().toString().trim()),
                    Integer.parseInt(tfDateYear.getText().toString().trim())
            );

            expensesListItem.expensesCategoryID = ExpensesCategory.getExpensesCategoryID(tfCategory.getText().toString().trim());
            expensesListItem.location = tfLocation.getText().toString().trim();
            expensesListItem.notes = tfNotes.getText().toString().trim();

            SessionCache.expensesDatabase.updateExpense(expensesListItem.sqlRowID, expensesListItem);
            expensesListAdapter.notifyItemChanged(intentVariables.listIndex);

            finish();
            overridePendingTransition(0, 0);

            if(exitListener != null)
                exitListener.run();
        });

        tvDelete.setOnClickListener(view -> {
            UIUtils.Dialog.showPrompt0(
                    view,
                    null,
                    "Delete Expense Entry",
                    "Are you sure you want to delete this entry?\nThis action cannot be undone.",
                    "Cancel",
                    "Delete",
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.btn_background_neutral)).getDefaultColor(),
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.btn_background_danger)).getDefaultColor(),
                    null,
                    btn1View -> {
                        SessionCache.expensesDatabase.deleteExpense(intentVariables.expense.sqlRowID);
                        expensesListAdapter.items.remove(intentVariables.expense);
                        expensesListAdapter.notifyItemRemoved(intentVariables.listIndex);

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
