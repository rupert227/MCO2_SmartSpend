package com.mobdeve.s17.group11.smartspend.expenses;

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

public class ExpensesActivity extends AppCompatActivity {

    public ExpensesListAdapter expensesListAdapter;

    private ExpensesPopupSort expensesPopupSort;
    private ImageButton btnAdd;
    private ImageButton btnSort;
    private RecyclerView rvExpensesList;
    private TextView tvPromptEmptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_expenses);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_root), (v, insets) -> {
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
        rvExpensesList = findViewById(R.id.rv_expenses);
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
                UIUtils.Appearance.dimBehind(expensesPopupSort.popupWindow.getContentView(), view, 0.1f);
            } else {
                expensesPopupSort.popupWindow.dismiss();
            }
        });

        Runnable formExitListener = () -> {
            if(!expensesListAdapter.items.isEmpty()) {
                rvExpensesList.setVisibility(View.VISIBLE);
                tvPromptEmptyList.setVisibility(View.GONE);
            } else {
                rvExpensesList.setVisibility(View.GONE);
                tvPromptEmptyList.setVisibility(View.VISIBLE);
            }
        };

        ExpensesEditActivity.exitListener = formExitListener;
        ExpensesNewActivity.exitListener = formExitListener;
    }

    private void initRecyclerViews() {
        expensesListAdapter = new ExpensesListAdapter(this);

        expensesListAdapter.items = SessionCache.expensesItems;
        expensesListAdapter.items.addAll(DataGenerator.getExpenseDataList());

        if(!expensesListAdapter.items.isEmpty()) {
            rvExpensesList.setVisibility(View.VISIBLE);
            tvPromptEmptyList.setVisibility(View.GONE);
        } else {
            rvExpensesList.setVisibility(View.GONE);
            tvPromptEmptyList.setVisibility(View.VISIBLE);
        }

        rvExpensesList.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        ));

        rvExpensesList.setAdapter(expensesListAdapter);

        ExpensesEditActivity.rvExpensesListRef = new WeakReference<>(rvExpensesList);
        ExpensesNewActivity.rvExpensesListRef = new WeakReference<>(rvExpensesList);
    }

}
