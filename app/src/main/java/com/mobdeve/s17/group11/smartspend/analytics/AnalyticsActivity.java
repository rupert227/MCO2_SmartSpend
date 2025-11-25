package com.mobdeve.s17.group11.smartspend.analytics;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.mobdeve.s17.group11.smartspend.R;
import com.mobdeve.s17.group11.smartspend.util.FormatHelper;
import com.mobdeve.s17.group11.smartspend.util.NavigationBar;
import com.mobdeve.s17.group11.smartspend.util.SessionCache;
import com.mobdeve.s17.group11.smartspend.util.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("SetTextI18n")
public class AnalyticsActivity extends AppCompatActivity {

    public AnalyticsListAdapter analyticsListAdapter;
    public List<Integer> pieColors = new ArrayList<>();
    public List<PieEntry> pieEntries = new ArrayList<>();
    public Map<Integer, Float> expenseCategoryAmountMap = new HashMap<>();
    public PieChart pieAnalytics;
    public PieData pieData;
    public PieDataSet pieDataSet;
    public TextView tvAmount;
    public TextView tvCaption;
    public TextView tvCategory;
    public TextView tvPercentage;
    public float totalExpensesAmount;
    
    private AnalyticsPopupSort analyticsPopupSort;
    private ConstraintLayout clDetails;
    private ImageButton btnSort;
    private RecyclerView rvAnalytics;
    private TextView tvPromptEmptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_analytics);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NavigationBar.init(this);

        initViews();
        initListeners();
        initRecyclerViews();
        initCharts();

        analyticsPopupSort = new AnalyticsPopupSort(this);

        analyticsPopupSort.applySettings(SessionCache.analyticsSortSettings);
        analyticsPopupSort.applySort();
    }

    private void initViews() {
        btnSort = findViewById(R.id.btn_header_sort);
        clDetails = findViewById(R.id.cl_details);
        pieAnalytics = findViewById(R.id.pie_analytics);
        rvAnalytics = findViewById(R.id.rv_analytics);
        tvAmount = findViewById(R.id.tv_amount);
        tvCaption = findViewById(R.id.tv_caption);
        tvCategory = findViewById(R.id.tv_category);
        tvPercentage = findViewById(R.id.tv_percentage);
        tvPromptEmptyList = findViewById(R.id.tv_empty_list);

        tvCaption.setText("Start Date - End Date");
    }

    private void initListeners() {
        btnSort.setOnClickListener(view -> {
            if(!analyticsPopupSort.popupWindow.isShowing()) {
                analyticsPopupSort.applySettings(SessionCache.analyticsSortSettings);
                analyticsPopupSort.popupWindow.showAsDropDown(view);
                UIUtils.Appearance.dimBehind(analyticsPopupSort.popupWindow.getContentView(), view, 0.1f);
            } else {
                analyticsPopupSort.popupWindow.dismiss();
            }
        });
    }

    private void initRecyclerViews() {
        analyticsListAdapter = new AnalyticsListAdapter(this);

        rvAnalytics.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        ));

        rvAnalytics.setAdapter(analyticsListAdapter);
    }

    private void initCharts() {
        pieDataSet = new PieDataSet(pieEntries, "");
        pieData = new PieData(pieDataSet);

        pieData.setDrawValues(false);
        pieDataSet.setColors(pieColors);

        pieAnalytics.setData(pieData);

        pieAnalytics.getDescription().setEnabled(false);
        pieAnalytics.getLegend().setEnabled(false);

        pieAnalytics.setDrawHoleEnabled(true);
        pieAnalytics.setHoleColor(Color.TRANSPARENT);
        pieAnalytics.setHoleRadius(65f);

        pieAnalytics.setTransparentCircleRadius(70f);
        pieAnalytics.setTransparentCircleColor(Color.WHITE);
        pieAnalytics.setTransparentCircleAlpha(50);

        pieAnalytics.setDrawEntryLabels(false);
        pieAnalytics.setRotationEnabled(false);
        pieAnalytics.setHighlightPerTapEnabled(true);

        pieAnalytics.invalidate();

        pieAnalytics.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry selected = (PieEntry) e;

                String label = selected.getLabel();
                float amount = selected.getValue();

                tvAmount.setText(FormatHelper.floatToPrice(amount));
                tvCategory.setText(label);
                tvPercentage.setText(
                        FormatHelper.floatToPrice((amount / totalExpensesAmount) * 100.0f) + "%"
                );
            }

            @Override
            public void onNothingSelected() {
                useUnselectedCategoryDetails();
            }

        });
    }

    public void validateUI() {
        if(!analyticsListAdapter.items.isEmpty()) {
            clDetails.setVisibility(View.VISIBLE);
            tvCaption.setVisibility(View.VISIBLE);
            rvAnalytics.setVisibility(View.VISIBLE);
            tvPromptEmptyList.setVisibility(View.GONE);
        } else {
            clDetails.setVisibility(View.GONE);
            tvCaption.setVisibility(View.GONE);
            rvAnalytics.setVisibility(View.GONE);
            tvPromptEmptyList.setVisibility(View.VISIBLE);
        }
    }

    public void useUnselectedCategoryDetails() {
        tvAmount.setText(FormatHelper.floatToPrice(totalExpensesAmount));
        tvCategory.setText("All Categories");
        tvPercentage.setText("100.00%");
    }

}
