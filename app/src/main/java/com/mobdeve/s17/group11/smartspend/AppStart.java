package com.mobdeve.s17.group11.smartspend;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

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
    }

}
