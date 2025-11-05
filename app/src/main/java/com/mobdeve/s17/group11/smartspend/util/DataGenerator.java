package com.mobdeve.s17.group11.smartspend.util;

import com.mobdeve.s17.group11.smartspend.budgets.BudgetsListItem;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesCategory;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {

    private static final Random rand = new Random();

    public static List<ExpensesListItem> getExpenseDataList() {
        List<ExpensesListItem> items = new ArrayList<>();

        for(int i = 0; i < 50; i++) {
            items.add(new ExpensesListItem(
                    rand.nextInt(ExpensesCategory.getListOrder().length),
                    rand.nextFloat() * 5000.0f,
                    new Date(rand.nextInt(28) + 1, rand.nextInt(12) + 1, rand.nextInt(2) + 2025),
                    "my house",
                    null
            ));
        }

        return items;
    }

    public static List<BudgetsListItem> getBudgetDataList() {
        List<BudgetsListItem> items = new ArrayList<>();

        for(int i = 0; i < 50; i++) {
            items.add(new BudgetsListItem(
                    rand.nextInt(ExpensesCategory.getListOrder().length),
                    rand.nextFloat() * 15000.0f,
                    new Date(rand.nextInt(28) + 1, rand.nextInt(12) + 1, rand.nextInt(2) + 2025),
                    new Date(rand.nextInt(28) + 1, rand.nextInt(12) + 1, rand.nextInt(2) + 2025),
                    "alalala"
            ));
        }

        return items;
    }

}
