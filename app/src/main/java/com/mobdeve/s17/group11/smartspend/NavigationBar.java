package com.mobdeve.s17.group11.smartspend;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;

import com.mobdeve.s17.group11.smartspend.analytics.AnalyticsActivity;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesActivity;
import com.mobdeve.s17.group11.smartspend.map.MapActivity;

public class NavigationBar {

    public static void init(Activity activity) {
        ImageButton btnAnalytics = activity.findViewById(R.id.btn_navigation_analytics);
        ImageButton btnExpenses = activity.findViewById(R.id.btn_navigation_expenses);
        ImageButton btnMap = activity.findViewById(R.id.btn_navigation_map);

        if(!(activity instanceof AnalyticsActivity)) {
            btnAnalytics.setOnClickListener(view -> {
                Intent intent = new Intent(activity.getBaseContext(), AnalyticsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
            });
        }

        if(!(activity instanceof ExpensesActivity)) {
            btnExpenses.setOnClickListener(view -> {
                Intent intent = new Intent(activity.getBaseContext(), ExpensesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
            });
        }

        if(!(activity instanceof MapActivity)) {
            btnMap.setOnClickListener(view -> {
                Intent intent = new Intent(activity.getBaseContext(), MapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
            });
        }

        ColorStateList defaultColor = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.atv_navigation_foreground));
        ColorStateList selectedColor = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.atv_navigation_foreground_selected));

        if(activity instanceof AnalyticsActivity) {
            btnAnalytics.setImageTintList(selectedColor);
            btnExpenses.setImageTintList(defaultColor);
            btnMap.setImageTintList(defaultColor);
        } else if(activity instanceof ExpensesActivity) {
            btnAnalytics.setImageTintList(defaultColor);
            btnExpenses.setImageTintList(selectedColor);
            btnMap.setImageTintList(defaultColor);
        } else if(activity instanceof MapActivity) {
            btnAnalytics.setImageTintList(defaultColor);
            btnExpenses.setImageTintList(defaultColor);
            btnMap.setImageTintList(selectedColor);
        }
    }

}
