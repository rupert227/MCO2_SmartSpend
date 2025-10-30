package com.mobdeve.s17.group11.smartspend.expenses;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s17.group11.smartspend.R;
import com.mobdeve.s17.group11.smartspend.util.Date;
import com.mobdeve.s17.group11.smartspend.util.DateHelper;
import com.mobdeve.s17.group11.smartspend.util.DropdownComposite;
import com.mobdeve.s17.group11.smartspend.util.NavigationBar;
import com.mobdeve.s17.group11.smartspend.util.UIUtils;

import java.lang.ref.WeakReference;
import java.util.Arrays;

public class ExpensesNewActivity extends AppCompatActivity {

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
    private TextView tvHeaderTitle;
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
        tvHeaderTitle = findViewById(R.id.tv_header_title);
        tvLocationPrompt = findViewById(R.id.tv_location_prompt);
        tvNotesPrompt = findViewById(R.id.tv_notes_prompt);

        tvHeaderTitle.setText("Create Expense Entry");
        tvDelete.setVisibility(TextView.GONE);
        btnSave.setText("Create Entry");

        Date currentDate = DateHelper.getCurrentDate();

        tfDateDay.setText(Integer.toString(currentDate.day));
        tfDateMonth.setText(Integer.toString(currentDate.month));
        tfDateYear.setText(Integer.toString(currentDate.year));
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

            float amount = Float.parseFloat(tfAmount.getText().toString().trim());
            int categoryID = ExpensesCategory.getExpensesCategoryID(tfCategory.getText().toString().trim());
            int dateDay = Integer.parseInt(tfDateDay.getText().toString().trim());
            int dateMonth = Integer.parseInt(tfDateMonth.getText().toString().trim());
            int dateYear = Integer.parseInt(tfDateYear.getText().toString().trim());
            String location = tfLocation.getText().toString().trim();
            String notes = tfNotes.getText().toString().trim();

            expensesListAdapter.items.add(0, new ExpensesListItem(
                    categoryID,
                    amount,
                    new Date(dateDay, dateMonth, dateYear),
                    location,
                    notes
            ));

            expensesListAdapter.notifyItemInserted(0);

            finish();
            overridePendingTransition(0, 0);
            rvExpensesListRef.get().scrollToPosition(0);

            if(exitListener != null)
                exitListener.run();
        });
    }

    private void initRecyclerViews() {
        Arrays.stream(ExpensesCategory.getListOrder()).forEach(categoryID ->
            categoryDropdownComposite.items.add(ExpensesCategory.getExpensesCategoryName(categoryID))
        );

        UIUtils.CompositeInstantiator.categoryDropdown(categoryDropdownComposite, tfCategory);
    }

}
