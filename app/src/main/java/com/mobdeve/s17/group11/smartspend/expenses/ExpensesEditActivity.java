package com.mobdeve.s17.group11.smartspend.expenses;

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
import com.mobdeve.s17.group11.smartspend.util.Date;
import com.mobdeve.s17.group11.smartspend.util.DropdownComposite;
import com.mobdeve.s17.group11.smartspend.util.NavigationBar;
import com.mobdeve.s17.group11.smartspend.util.SessionCache;
import com.mobdeve.s17.group11.smartspend.util.UIUtils;

import java.lang.ref.WeakReference;
import java.util.Arrays;

@SuppressLint("SetTextI18n")
public class ExpensesEditActivity extends AppCompatActivity {

    public static ExpensesListItem expenseEdit;
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
    private ImageButton btnDateCalendar;
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
        btnDateCalendar = findViewById(R.id.btn_date_calendar);
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

        tfAmount.setText(Float.toString(expenseEdit.amount));
        tfCategory.setText(ExpensesCategory.getExpensesCategoryName(expenseEdit.expensesCategoryID));
        tfDateDay.setText(Integer.toString(expenseEdit.date.day));
        tfDateMonth.setText(Integer.toString(expenseEdit.date.month));
        tfDateYear.setText(Integer.toString(expenseEdit.date.year));
        tfLocation.setText(expenseEdit.location);
        tfNotes.setText(expenseEdit.notes);
    }

    private void initListeners() {
        ExpensesListAdapter expensesListAdapter = (ExpensesListAdapter) rvExpensesListRef.get().getAdapter();
        assert expensesListAdapter != null;

        btnBack.setOnClickListener(view -> {
            finish();
            overridePendingTransition(0, 0);
        });

        btnDateCalendar.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ExpensesEditActivity.this,

                    (DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) -> {
                        tfDateDay.setText(Integer.toString(selectedDay));
                        tfDateMonth.setText(Integer.toString(selectedMonth + 1));
                        tfDateYear.setText(Integer.toString(selectedYear));
                    },

                    Integer.parseInt(tfDateYear.getText().toString().trim()),
                    Integer.parseInt(tfDateMonth.getText().toString().trim()) - 1,
                    Integer.parseInt(tfDateDay.getText().toString().trim())
            );

            datePickerDialog.show();
        });

        btnSave.setOnClickListener(view -> {
            boolean validFields;

            validFields = UIUtils.Validator.validateAmountField(tfAmount, tvAmountPrompt);
            validFields &= UIUtils.Validator.validateCategoryField(tfCategory, tvCategoryPrompt);
            validFields &= UIUtils.Validator.validateDateFields(tfDateDay, tfDateMonth, tfDateYear, tvDatePrompt);

            if(!validFields)
                return;

            expenseEdit.amount = Float.parseFloat(tfAmount.getText().toString().trim());

            expenseEdit.date = new Date(
                    Integer.parseInt(tfDateDay.getText().toString().trim()),
                    Integer.parseInt(tfDateMonth.getText().toString().trim()),
                    Integer.parseInt(tfDateYear.getText().toString().trim())
            );

            expenseEdit.expensesCategoryID = ExpensesCategory.getExpensesCategoryID(tfCategory.getText().toString().trim());
            expenseEdit.location = tfLocation.getText().toString().trim();
            expenseEdit.notes = tfNotes.getText().toString().trim();

            SessionCache.expensesDatabase.updateExpense(expenseEdit.sqlRowID, expenseEdit);

            ExpensesActivity.expensesPopupSortRef.get().applySort();

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
                        SessionCache.expensesDatabase.deleteExpense(expenseEdit.sqlRowID);
                        SessionCache.expensesItems.remove(expenseEdit.listIndex);

                        for(int i = expenseEdit.listIndex; i < SessionCache.expensesItems.size(); i++)
                            SessionCache.expensesItems.get(i).listIndex = i;

                        ExpensesActivity.expensesPopupSortRef.get().applySort();

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
