package com.mobdeve.s17.group11.smartspend.analytics;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesCategory;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesListItem;
import com.mobdeve.s17.group11.smartspend.util.NavigationBar;
import com.mobdeve.s17.group11.smartspend.util.SessionCache;
import com.mobdeve.s17.group11.smartspend.util.Date;
import com.mobdeve.s17.group11.smartspend.util.DateHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AnalyticsActivity extends AppCompatActivity {

    private Spinner spAnalyticsRange;
    private PieChart chartExpenseCategories;
    private TextView tvEmptyState;
    private RecyclerView rvCategoryBreakdown;

    private AnalyticsCategoryAdapter categoryAdapter;
    private AnalyticsRange currentRange = AnalyticsRange.DAY;
    private final Locale locale = Locale.getDefault();

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
        initRangeSpinner();
        initChart();
        updateAnalytics();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAnalytics();
    }

    private void initViews() {
        spAnalyticsRange = findViewById(R.id.sp_analytics_range);
        chartExpenseCategories = findViewById(R.id.chart_expense_categories);
        tvEmptyState = findViewById(R.id.tv_empty_state);
        rvCategoryBreakdown = findViewById(R.id.rv_category_breakdown);
    }

    private void initRangeSpinner() {
        List<String> rangeLabels = new ArrayList<>();
        for(AnalyticsRange range : AnalyticsRange.values())
            rangeLabels.add(range.getLabel());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                rangeLabels
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spAnalyticsRange.setAdapter(adapter);
        spAnalyticsRange.setSelection(currentRange.ordinal());
        spAnalyticsRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentRange = AnalyticsRange.fromIndex(position);
                updateAnalytics();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void initChart() {
        chartExpenseCategories.getDescription().setEnabled(false);
        chartExpenseCategories.getLegend().setEnabled(false);

        // --- DONUT / RING CONFIGURATION ---
        chartExpenseCategories.setDrawHoleEnabled(true);
        chartExpenseCategories.setHoleColor(Color.TRANSPARENT);
        chartExpenseCategories.setHoleRadius(65f);
        chartExpenseCategories.setTransparentCircleRadius(70f);
        chartExpenseCategories.setTransparentCircleColor(Color.WHITE);
        chartExpenseCategories.setTransparentCircleAlpha(50);

        // --- LABEL CONFIGURATION ---
        chartExpenseCategories.setDrawEntryLabels(false); // Hides clutter on the ring
        chartExpenseCategories.setCenterTextColor(Color.BLACK);
        chartExpenseCategories.setCenterTextSize(16f);
        // ---------------------------

        chartExpenseCategories.setRotationEnabled(false);
        chartExpenseCategories.setHighlightPerTapEnabled(true);

        // Add padding so "Popped" slices don't get clipped
        chartExpenseCategories.setExtraOffsets(10f, 10f, 10f, 10f);

        // --- INTERACTION LISTENER ---
        // Shows Category Name & Value in the CENTER when clicked
        chartExpenseCategories.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e instanceof PieEntry) {
                    PieEntry pieEntry = (PieEntry) e;
                    String text = pieEntry.getLabel() + "\n" + String.format(locale, "%.2f", e.getY());
                    chartExpenseCategories.setCenterText(text);
                }
            }

            @Override
            public void onNothingSelected() {
                chartExpenseCategories.setCenterText("");
            }
        });

        rvCategoryBreakdown.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new AnalyticsCategoryAdapter();
        rvCategoryBreakdown.setAdapter(categoryAdapter);
    }

    private void updateAnalytics() {
        Map<Integer, Float> totals = aggregateByCategory(currentRange);

        if(totals.isEmpty()) {
            chartExpenseCategories.clear();
            chartExpenseCategories.setCenterText(""); // Clear any previous center text
            chartExpenseCategories.setVisibility(View.GONE);
            rvCategoryBreakdown.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
            categoryAdapter.submitItems(Collections.emptyList());
            return;
        }

        tvEmptyState.setVisibility(View.GONE);
        chartExpenseCategories.setVisibility(View.VISIBLE);
        rvCategoryBreakdown.setVisibility(View.VISIBLE);

        List<Map.Entry<Integer, Float>> sortedEntries = new ArrayList<>(totals.entrySet());
        Collections.sort(sortedEntries, (entry0, entry1) -> Float.compare(entry1.getValue(), entry0.getValue()));

        List<PieEntry> pieEntries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        List<AnalyticsCategoryAdapter.CategoryBreakdownItem> categoryItems = new ArrayList<>();
        int[] palette = getChartColors(sortedEntries.size());
        AtomicInteger index = new AtomicInteger(0);

        sortedEntries.forEach(entry -> {
            int paletteColor = palette[index.get() % palette.length];
            String categoryName = ExpensesCategory.getExpensesCategoryName(entry.getKey());

            pieEntries.add(new PieEntry(entry.getValue(), categoryName));
            colors.add(paletteColor);

            categoryItems.add(new AnalyticsCategoryAdapter.CategoryBreakdownItem(
                    categoryName,
                    entry.getValue(),
                    paletteColor
            ));

            index.getAndIncrement();
        });

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(colors);
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(10f); // How far the slice "pops" out when clicked

        // Disable values on the chart lines to keep it clean (Indicator is now in Center)
        dataSet.setDrawValues(false);

        PieData pieData = new PieData(dataSet);
        chartExpenseCategories.setData(pieData);
        chartExpenseCategories.invalidate();

        categoryAdapter.submitItems(categoryItems);
    }

    private Map<Integer, Float> aggregateByCategory(AnalyticsRange range) {
        if(SessionCache.expensesItems.isEmpty())
            return new LinkedHashMap<>();

        // BUG FIX: Removed the "fallback" logic.
        // Now strictly filters based on Current Date.
        return aggregateWithinBounds(range, DateHelper.getCurrentDate());
    }

    private Map<Integer, Float> aggregateWithinBounds(AnalyticsRange range, Date referenceDate) {
        Map<Integer, Float> totals = new LinkedHashMap<>();
        int[] bounds = getRangeBounds(range, referenceDate);
        int startUnique = bounds[0];
        int endUnique = bounds[1];

        for(ExpensesListItem expense : SessionCache.expensesItems) {
            int expenseUnique = expense.date.getUniqueValue();

            if(expenseUnique < startUnique || expenseUnique > endUnique)
                continue;

            totals.merge(expense.expensesCategoryID, expense.amount, Float::sum);
        }

        return totals;
    }

    private int[] getRangeBounds(AnalyticsRange range, Date referenceDate) {
        Date startDate;

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(referenceDate.year, referenceDate.month - 1, referenceDate.day);

        switch(range) {
            case WEEK:
                calendar.add(java.util.Calendar.DAY_OF_YEAR, -6);
                break;
            case MONTH:
                calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
                break;
            case YEAR:
                calendar.set(java.util.Calendar.DAY_OF_YEAR, 1);
                break;
            case DAY:
            default:
                break;
        }

        startDate = new Date(
                calendar.get(java.util.Calendar.DAY_OF_MONTH),
                calendar.get(java.util.Calendar.MONTH) + 1,
                calendar.get(java.util.Calendar.YEAR)
        );

        return new int[]{startDate.getUniqueValue(), referenceDate.getUniqueValue()};
    }

    private int[] getChartColors(int required) {
        int[] palette = new int[]{
                Color.parseColor("#0D47A1"), // Dark Blue
                Color.parseColor("#1976D2"), // Blue
                Color.parseColor("#42A5F5"), // Light Blue
                Color.parseColor("#90CAF9"), // Very Light Blue
                Color.parseColor("#1A237E")  // Navy
        };

        if(required <= palette.length)
            return palette;

        int[] repeated = new int[required];
        for(int i = 0; i < required; i++)
            repeated[i] = palette[i % palette.length];

        return repeated;
    }

    private enum AnalyticsRange {
        DAY("Day"),
        WEEK("Week"),
        MONTH("Month"),
        YEAR("Year");

        private final String label;

        AnalyticsRange(String label) { this.label = label; }

        public String getLabel() { return label; }

        public static AnalyticsRange fromIndex(int index) {
            AnalyticsRange[] ranges = values();
            if(index < 0 || index >= ranges.length) return DAY;
            return ranges[index];
        }
    }
}