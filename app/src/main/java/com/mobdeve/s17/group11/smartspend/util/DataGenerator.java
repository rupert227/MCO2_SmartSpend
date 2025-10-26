package com.mobdeve.s17.group11.smartspend.util;

import com.mobdeve.s17.group11.smartspend.expenses.ExpensesCategory;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesListItem;
import com.mobdeve.s17.group11.smartspend.util.date.Date;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {

    public static List<ExpensesListItem> getExpenseDataList() {
        List<ExpensesListItem> items = new ArrayList<>();

        Random rand = new Random();

        for(int i = 0; i < 50; i++) {
            items.add(new ExpensesListItem(rand.nextInt(2) == 0 ? ExpensesCategory.FOOD_AND_DRINKS : ExpensesCategory.TRANSPORTATION, rand.nextFloat() * 5000.0f, new Date(14, 10, 2025)));
        }

        return items;
    }

}
