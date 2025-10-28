package com.mobdeve.s17.group11.smartspend.budgets;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mobdeve.s17.group11.smartspend.util.NavigationBar;
import com.mobdeve.s17.group11.smartspend.R;
import com.mobdeve.s17.group11.smartspend.util.UIUtils;

public class BudgetsEditActivity extends AppCompatActivity {

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

        ImageButton btnBack = findViewById(R.id.btn_header_back);
        Button btnSave = findViewById(R.id.btn_save);
        TextView tvDelete = findViewById(R.id.tv_delete);

        btnBack.setOnClickListener(view -> {
            finish();
            overridePendingTransition(0, 0);
        });

        btnSave.setOnClickListener(view -> {
            finish();
            overridePendingTransition(0, 0);
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
                    null
            );
        });
    }

}
