package com.mobdeve.s17.group11.smartspend.budgets;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mobdeve.s17.group11.smartspend.NavigationBar;
import com.mobdeve.s17.group11.smartspend.R;

public class BudgetsNewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_budgets_edit);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ll_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NavigationBar.init(this);

        TextView tvHeaderTitle = findViewById(R.id.tv_header_title);
        ImageButton btnBack = findViewById(R.id.btn_header_back);
        Button btnSave = findViewById(R.id.btn_save);
        TextView tvDelete = findViewById(R.id.tv_delete);

        tvHeaderTitle.setText("Create Budget Entry");
        tvDelete.setVisibility(TextView.GONE);
        btnSave.setText("Create Entry");

        btnBack.setOnClickListener(view -> {
            finish();
            overridePendingTransition(0, 0);
        });

        btnSave.setOnClickListener(view -> {
            finish();
            overridePendingTransition(0, 0);
        });

    }

}
