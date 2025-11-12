package com.mobdeve.s17.group11.smartspend;

import android.app.Application;
import android.content.res.ColorStateList;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.mobdeve.s17.group11.smartspend.budgets.BudgetsDatabase;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesDatabase;
import com.mobdeve.s17.group11.smartspend.util.SessionCache;

public class AppStart extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SessionCache.budgetsDatabase = new BudgetsDatabase(this);
        SessionCache.expensesDatabase = new ExpensesDatabase(this);

        SessionCache.budgetsItems.addAll(SessionCache.budgetsDatabase.getAllBudgets());
        SessionCache.expensesItems.addAll(SessionCache.expensesDatabase.getAllExpenses());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        SessionCache.Color.tvDanger = ContextCompat.getColor(this, R.color.tv_danger);
        SessionCache.Color.tvLabel1 = ContextCompat.getColor(this, R.color.tv_label1);
    }

}
