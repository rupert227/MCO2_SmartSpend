package com.mobdeve.s17.group11.smartspend.budgets;

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
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesCategory;
import com.mobdeve.s17.group11.smartspend.util.Date;
import com.mobdeve.s17.group11.smartspend.util.DropdownComposite;
import com.mobdeve.s17.group11.smartspend.util.NavigationBar;
import com.mobdeve.s17.group11.smartspend.util.UIUtils;

import java.lang.ref.WeakReference;
import java.util.Arrays;

public class BudgetsNewActivity extends AppCompatActivity {

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
    private TextView tvAmountPrompt;
    private TextView tvCategoryPrompt;
    private TextView tvDateEndPrompt, tvDateStartPrompt;
    private TextView tvDelete;
    private TextView tvHeaderTitle;
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
        tvHeaderTitle = findViewById(R.id.tv_header_title);
        tvNotesPrompt = findViewById(R.id.tv_notes_prompt);

        tvHeaderTitle.setText("Create Budget Entry");
        tvDelete.setVisibility(TextView.GONE);
        btnSave.setText("Create Entry");
    }

    private void initListeners() {
        BudgetsListAdapter budgetsListAdapter = (BudgetsListAdapter) rvBudgetsListRef.get().getAdapter();
        assert budgetsListAdapter != null;

        btnBack.setOnClickListener(view -> {
            finish();
            overridePendingTransition(0, 0);
        });

        btnSave.setOnClickListener(view -> {
            boolean validFields;

            validFields = UIUtils.Validator.validateAmountField(tfAmount, tvAmountPrompt);
            validFields &= UIUtils.Validator.validateCategoryField(tfCategory, tvCategoryPrompt);
            validFields &= UIUtils.Validator.validateDateFields(tfDateStartDay, tfDateStartMonth, tfDateStartYear, tvDateStartPrompt);
            validFields &= UIUtils.Validator.validateDateFields(tfDateEndDay, tfDateEndMonth, tfDateEndYear, tvDateEndPrompt);

            if(!validFields)
                return;

            float amount = Float.parseFloat(tfAmount.getText().toString().trim());
            int categoryID = ExpensesCategory.getExpensesCategoryID(tfCategory.getText().toString().trim());
            int dateEndDay = Integer.parseInt(tfDateEndDay.getText().toString().trim());
            int dateEndMonth = Integer.parseInt(tfDateEndMonth.getText().toString().trim());
            int dateEndYear = Integer.parseInt(tfDateEndYear.getText().toString().trim());
            int dateStartDay = Integer.parseInt(tfDateStartDay.getText().toString().trim());
            int dateStartMonth = Integer.parseInt(tfDateStartMonth.getText().toString().trim());
            int dateStartYear = Integer.parseInt(tfDateStartYear.getText().toString().trim());
            String notes = tfNotes.getText().toString().trim();

            budgetsListAdapter.items.add(0, new BudgetsListItem(
                    categoryID,
                    amount,
                    new Date(dateEndDay, dateEndMonth, dateEndYear),
                    new Date(dateStartDay, dateStartMonth, dateStartYear),
                    notes
            ));

            budgetsListAdapter.notifyItemInserted(0);

            finish();
            overridePendingTransition(0, 0);
            rvBudgetsListRef.get().scrollToPosition(0);

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
