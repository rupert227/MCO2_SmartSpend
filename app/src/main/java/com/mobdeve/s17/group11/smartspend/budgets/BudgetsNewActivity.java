package com.mobdeve.s17.group11.smartspend.budgets;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mobdeve.s17.group11.smartspend.R;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesCategory;
import com.mobdeve.s17.group11.smartspend.util.DropdownComposite;
import com.mobdeve.s17.group11.smartspend.util.NavigationBar;
import com.mobdeve.s17.group11.smartspend.util.UIUtils;

import java.util.Arrays;

public class BudgetsNewActivity extends AppCompatActivity {

    private Button btnSave;
    private DropdownComposite categoryDropdownComposite = new DropdownComposite();
    private EditText tfCategory;
    private ImageButton btnBack;
    private TextView tvDelete;
    private TextView tvHeaderTitle;

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

        tvHeaderTitle.setText("Create Budget Entry");
        tvDelete.setVisibility(TextView.GONE);
        btnSave.setText("Create Entry");
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_header_back);
        btnSave = findViewById(R.id.btn_save);
        tfCategory = findViewById(R.id.tf_category);
        tvDelete = findViewById(R.id.tv_delete);
        tvHeaderTitle = findViewById(R.id.tv_header_title);
    }

    private void initListeners() {
        btnBack.setOnClickListener(view -> {
            finish();
            overridePendingTransition(0, 0);
        });

        btnSave.setOnClickListener(view -> {
            finish();
            overridePendingTransition(0, 0);
        });
    }

    private void initRecyclerViews() {
        Arrays.stream(ExpensesCategory.getListOrder()).forEach(categoryID -> {
            categoryDropdownComposite.items.add(ExpensesCategory.getExpenseCategoryName(categoryID));
        });

        UIUtils.CompositeInstantiator.categoryDropdown(categoryDropdownComposite, tfCategory);
    }

}
