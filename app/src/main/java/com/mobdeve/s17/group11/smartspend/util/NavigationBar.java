package com.mobdeve.s17.group11.smartspend.util;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;

import com.mobdeve.s17.group11.smartspend.R;
import com.mobdeve.s17.group11.smartspend.analytics.AnalyticsActivity;
import com.mobdeve.s17.group11.smartspend.budgets.BudgetsActivity;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesActivity;
import com.mobdeve.s17.group11.smartspend.gallery.GalleryActivity;

public class NavigationBar {

    public static void init(Activity activity) {
        ImageButton btnAnalytics = activity.findViewById(R.id.btn_navigation_analytics);
        ImageButton btnBudgets = activity.findViewById(R.id.btn_navigation_budgets);
        ImageButton btnExpenses = activity.findViewById(R.id.btn_navigation_expenses);
        ImageButton btnGallery = activity.findViewById(R.id.btn_navigation_gallery);

        if(!(activity instanceof AnalyticsActivity)) {
            btnAnalytics.setOnClickListener(view -> {
                Intent intent = new Intent(activity.getBaseContext(), AnalyticsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
            });
        }

        if(!(activity instanceof BudgetsActivity)) {
            btnBudgets.setOnClickListener(view -> {
                Intent intent = new Intent(activity.getBaseContext(), BudgetsActivity.class);
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

        if(!(activity instanceof GalleryActivity)) {
            btnGallery.setOnClickListener(view -> {
                Intent intent = new Intent(activity.getBaseContext(), GalleryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
            });
        }

        ColorStateList defaultColor = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.atv_navigation_foreground));
        ColorStateList selectedColor = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.atv_navigation_foreground_selected));

        if(activity instanceof AnalyticsActivity) {
            btnAnalytics.setImageTintList(selectedColor);
            btnBudgets.setImageTintList(defaultColor);
            btnExpenses.setImageTintList(defaultColor);
            btnGallery.setImageTintList(defaultColor);
        } else if(activity instanceof BudgetsActivity) {
            btnAnalytics.setImageTintList(defaultColor);
            btnBudgets.setImageTintList(selectedColor);
            btnExpenses.setImageTintList(defaultColor);
            btnGallery.setImageTintList(defaultColor);
        } else if(activity instanceof ExpensesActivity) {
            btnAnalytics.setImageTintList(defaultColor);
            btnBudgets.setImageTintList(defaultColor);
            btnExpenses.setImageTintList(selectedColor);
            btnGallery.setImageTintList(defaultColor);
        } else if(activity instanceof GalleryActivity) {
            btnAnalytics.setImageTintList(defaultColor);
            btnBudgets.setImageTintList(defaultColor);
            btnExpenses.setImageTintList(defaultColor);
            btnGallery.setImageTintList(selectedColor);
        }
    }

}
