package com.mobdeve.s17.group11.smartspend.util;

import com.mobdeve.s17.group11.smartspend.budgets.BudgetsDatabase;
import com.mobdeve.s17.group11.smartspend.budgets.BudgetsListItem;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesDatabase;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesListItem;

import java.util.ArrayList;
import java.util.List;

public class SessionCache {

    public static BudgetsDatabase budgetsDatabase;
    public static ExpensesDatabase expensesDatabase;

    public static final List<BudgetsListItem> budgetsItems = new ArrayList<>();
    public static final List<ExpensesListItem> expensesItems = new ArrayList<>();

    public static final SortSettingsVariables budgetsSortSettings = new SortSettingsVariables();
    public static final SortSettingsVariables expensesSortSettings = new SortSettingsVariables();

}
