package com.mobdeve.s17.group11.smartspend.util;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.widget.ImageButton;

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

        if(activity instanceof AnalyticsActivity) {
            btnAnalytics.setImageTintList(ColorStateList.valueOf(SessionCache.Color.atvNavigationForegroundSelected));
            btnBudgets.setImageTintList(ColorStateList.valueOf(SessionCache.Color.atvNavigationForeground));
            btnExpenses.setImageTintList(ColorStateList.valueOf(SessionCache.Color.atvNavigationForeground));
            btnGallery.setImageTintList(ColorStateList.valueOf(SessionCache.Color.atvNavigationForeground));
        } else if(activity instanceof BudgetsActivity) {
            btnAnalytics.setImageTintList(ColorStateList.valueOf(SessionCache.Color.atvNavigationForeground));
            btnBudgets.setImageTintList(ColorStateList.valueOf(SessionCache.Color.atvNavigationForegroundSelected));
            btnExpenses.setImageTintList(ColorStateList.valueOf(SessionCache.Color.atvNavigationForeground));
            btnGallery.setImageTintList(ColorStateList.valueOf(SessionCache.Color.atvNavigationForeground));
        } else if(activity instanceof ExpensesActivity) {
            btnAnalytics.setImageTintList(ColorStateList.valueOf(SessionCache.Color.atvNavigationForeground));
            btnBudgets.setImageTintList(ColorStateList.valueOf(SessionCache.Color.atvNavigationForeground));
            btnExpenses.setImageTintList(ColorStateList.valueOf(SessionCache.Color.atvNavigationForegroundSelected));
            btnGallery.setImageTintList(ColorStateList.valueOf(SessionCache.Color.atvNavigationForeground));
        } else if(activity instanceof GalleryActivity) {
            btnAnalytics.setImageTintList(ColorStateList.valueOf(SessionCache.Color.atvNavigationForeground));
            btnBudgets.setImageTintList(ColorStateList.valueOf(SessionCache.Color.atvNavigationForeground));
            btnExpenses.setImageTintList(ColorStateList.valueOf(SessionCache.Color.atvNavigationForeground));
            btnGallery.setImageTintList(ColorStateList.valueOf(SessionCache.Color.atvNavigationForegroundSelected));
        }
    }

}
