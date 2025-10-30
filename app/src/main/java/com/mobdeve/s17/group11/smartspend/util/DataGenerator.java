package com.mobdeve.s17.group11.smartspend.util;

import com.mobdeve.s17.group11.smartspend.budgets.BudgetsListItem;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesCategory;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {

    public static List<ExpensesListItem> getExpenseDataList() {
        List<ExpensesListItem> items = new ArrayList<>();

        Random rand = new Random();

        for(int i = 0; i < 50; i++) {
            items.add(new ExpensesListItem(rand.nextInt(ExpensesCategory.getListOrder().length), rand.nextFloat() * 5000.0f, new Date(14, 10, 2025), "my house", "not sure what to put here."));
        }

        return items;
    }

    public static List<BudgetsListItem> getBudgetDataList() {
        List<BudgetsListItem> items = new ArrayList<>();

        Random rand = new Random();

        for(int i = 0; i < 50; i++) {
            items.add(new BudgetsListItem(rand.nextInt(ExpensesCategory.getListOrder().length), rand.nextFloat() * 5000.0f, new Date(14, 10, 2025), new Date(14, 11, 2025), null));
        }

        return items;
    }

}
