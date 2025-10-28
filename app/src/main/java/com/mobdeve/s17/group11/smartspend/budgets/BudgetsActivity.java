package com.mobdeve.s17.group11.smartspend.budgets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s17.group11.smartspend.util.NavigationBar;
import com.mobdeve.s17.group11.smartspend.R;
import com.mobdeve.s17.group11.smartspend.util.DataGenerator;

public class BudgetsActivity extends AppCompatActivity {

    public BudgetsPopupSort budgetsPopupSort;
    public BudgetsListAdapter budgetsListAdapter;
    public ImageButton btnAdd;
    public ImageButton btnSort;
    public RecyclerView budgetsListRecyclerView;

    public TextView tvPromptEmptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_budgets);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NavigationBar.init(this);
        budgetsPopupSort = new BudgetsPopupSort(this);

        initViews();
        initListeners();
        initRecyclerViews();
    }

    private void initViews() {
        btnAdd = findViewById(R.id.btn_add);
        btnSort = findViewById(R.id.btn_header_sort);
        budgetsListRecyclerView = findViewById(R.id.rv_budgets);
        tvPromptEmptyList = findViewById(R.id.tv_empty_list);
    }

    private void initListeners() {
        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, BudgetsNewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });

        btnSort.setOnClickListener(view -> {
            if(!budgetsPopupSort.popupWindow.isShowing()) {
                budgetsPopupSort.popupWindow.showAsDropDown(view);
            } else {
                budgetsPopupSort.popupWindow.dismiss();
            }
        });
    }

    private void initRecyclerViews() {
        budgetsListAdapter = new BudgetsListAdapter(this);

        budgetsListAdapter.items = DataGenerator.getBudgetDataList();

        if(!budgetsListAdapter.items.isEmpty()) {
            budgetsListRecyclerView.setVisibility(View.VISIBLE);
            tvPromptEmptyList.setVisibility(View.GONE);
        } else {
            budgetsListRecyclerView.setVisibility(View.GONE);
            tvPromptEmptyList.setVisibility(View.VISIBLE);
        }

        budgetsListRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false));

        budgetsListRecyclerView.setAdapter(budgetsListAdapter);
    }

}
