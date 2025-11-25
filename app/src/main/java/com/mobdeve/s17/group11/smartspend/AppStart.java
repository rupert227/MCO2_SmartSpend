package com.mobdeve.s17.group11.smartspend;

import android.app.Application;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.mobdeve.s17.group11.smartspend.budgets.BudgetsDatabase;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesDatabase;
import com.mobdeve.s17.group11.smartspend.util.SessionCache;

import java.io.File;

public class AppStart extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SessionCache.galleryDirectory = new File(getExternalFilesDir(null), "gallery");

        if(!SessionCache.galleryDirectory.exists())
            SessionCache.galleryDirectory.mkdirs();

        Log.d("SMARTSPEND_LOG", "Gallery Directory: " + SessionCache.galleryDirectory.getAbsolutePath());

        SessionCache.budgetsDatabase = new BudgetsDatabase(this);
        SessionCache.expensesDatabase = new ExpensesDatabase(this);

        SessionCache.budgetsItems.addAll(SessionCache.budgetsDatabase.getAllBudgets());
        SessionCache.expensesItems.addAll(SessionCache.expensesDatabase.getAllExpenses());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        SessionCache.Color.atvNavigationForeground = ContextCompat.getColor(this, R.color.atv_navigation_foreground);
        SessionCache.Color.atvNavigationForegroundSelected = ContextCompat.getColor(this, R.color.atv_navigation_foreground_selected);
        SessionCache.Color.icoGrayed = ContextCompat.getColor(this, R.color.ico_grayed);
        SessionCache.Color.icoNeutral = ContextCompat.getColor(this, R.color.ico_neutral);
        SessionCache.Color.tvDanger = ContextCompat.getColor(this, R.color.tv_danger);
        SessionCache.Color.tvLabel1 = ContextCompat.getColor(this, R.color.tv_label1);
    }

}
