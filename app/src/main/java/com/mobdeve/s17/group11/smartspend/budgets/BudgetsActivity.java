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

import com.mobdeve.s17.group11.smartspend.R;
import com.mobdeve.s17.group11.smartspend.util.DataGenerator;
import com.mobdeve.s17.group11.smartspend.util.NavigationBar;
import com.mobdeve.s17.group11.smartspend.util.SessionCache;
import com.mobdeve.s17.group11.smartspend.util.UIUtils;

import java.lang.ref.WeakReference;

public class BudgetsActivity extends AppCompatActivity {

    private BudgetsPopupSort budgetsPopupSort;
    private BudgetsListAdapter budgetsListAdapter;
    private ImageButton btnAdd;
    private ImageButton btnSort;
    private RecyclerView rvBudgetsList;

    private TextView tvPromptEmptyList;

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
        rvBudgetsList = findViewById(R.id.rv_budgets);
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
                UIUtils.Appearance.dimBehind(budgetsPopupSort.popupWindow.getContentView(), view, 0.1f);
            } else {
                budgetsPopupSort.popupWindow.dismiss();
            }
        });

        Runnable formExitListener = () -> {
            if(!budgetsListAdapter.items.isEmpty()) {
                rvBudgetsList.setVisibility(View.VISIBLE);
                tvPromptEmptyList.setVisibility(View.GONE);
            } else {
                rvBudgetsList.setVisibility(View.GONE);
                tvPromptEmptyList.setVisibility(View.VISIBLE);
            }
        };

        BudgetsEditActivity.exitListener = formExitListener;
        BudgetsNewActivity.exitListener = formExitListener;
    }

    private void initRecyclerViews() {
        budgetsListAdapter = new BudgetsListAdapter(this);

        budgetsListAdapter.items = SessionCache.budgetsItems;
        budgetsListAdapter.items.addAll(DataGenerator.getBudgetDataList());

        if(!budgetsListAdapter.items.isEmpty()) {
            rvBudgetsList.setVisibility(View.VISIBLE);
            tvPromptEmptyList.setVisibility(View.GONE);
        } else {
            rvBudgetsList.setVisibility(View.GONE);
            tvPromptEmptyList.setVisibility(View.VISIBLE);
        }

        rvBudgetsList.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        ));

        rvBudgetsList.setAdapter(budgetsListAdapter);

        BudgetsEditActivity.rvBudgetsListRef = new WeakReference<>(rvBudgetsList);
        BudgetsNewActivity.rvBudgetsListRef = new WeakReference<>(rvBudgetsList);
    }

}
