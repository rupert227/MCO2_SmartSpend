package com.mobdeve.s17.group11.smartspend.expenses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s17.group11.smartspend.NavigationBar;
import com.mobdeve.s17.group11.smartspend.R;
import com.mobdeve.s17.group11.smartspend.util.DataGenerator;

public class ExpensesActivity extends AppCompatActivity {

    public ExpensesPopupSort expensesPopupSort;
    public ExpensesListAdapter expensesListAdapter;
    public ImageButton btnAdd;
    public ImageButton btnSort;
    public RecyclerView expensesListRecyclerView;

    public TextView tvPromptEmptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_expenses);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ll_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        NavigationBar.init(this);
        expensesPopupSort = new ExpensesPopupSort(this);

        initViews();
        initListeners();
        initRecyclerViews();
    }

    private void initViews() {
        btnAdd = findViewById(R.id.btn_add);
        btnSort = findViewById(R.id.btn_header_sort);
        expensesListRecyclerView = findViewById(R.id.rv_expenses);
        tvPromptEmptyList = findViewById(R.id.tv_empty_list);
    }

    private void initListeners() {
        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, ExpensesNewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });

        btnSort.setOnClickListener(view -> {
            if(!expensesPopupSort.popupWindow.isShowing()) {
                expensesPopupSort.popupWindow.showAsDropDown(view);
            } else {
                expensesPopupSort.popupWindow.dismiss();
            }
        });
    }

    private void initRecyclerViews() {
        expensesListAdapter = new ExpensesListAdapter(this);

        expensesListAdapter.items = DataGenerator.getExpenseDataList();

        if(!expensesListAdapter.items.isEmpty()) {
            expensesListRecyclerView.setVisibility(View.VISIBLE);
            tvPromptEmptyList.setVisibility(View.GONE);
        } else {
            expensesListRecyclerView.setVisibility(View.GONE);
            tvPromptEmptyList.setVisibility(View.VISIBLE);
        }

        expensesListRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false));

        expensesListRecyclerView.setAdapter(expensesListAdapter);
    }

}
