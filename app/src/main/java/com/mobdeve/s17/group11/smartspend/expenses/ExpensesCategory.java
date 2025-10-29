package com.mobdeve.s17.group11.smartspend.expenses;

import java.util.Map;
import java.util.TreeMap;

public class ExpensesCategory {

    public static final int OTHERS = 0;
    public static final int ACCOMMODATION = 1;
    public static final int ENTERTAINMENT = 2;
    public static final int FOOD_AND_DRINKS = 3;
    public static final int HEALTH_AND_FITNESS = 4;
    public static final int HOME_AND_ESSENTIALS = 5;
    public static final int PERSONAL_ITEMS = 6;
    public static final int SUBSCRIPTIONS = 7;
    public static final int TRANSPORTATION = 8;

    private static final int[] listOrder = {1, 2, 3, 4, 5, 6, 7, 8, 0};

    private static final Map<Integer, String> expenseCategoryNamesMap = new TreeMap<>();
    private static final Map<String, Integer> expenseCategoryIDMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    static {
        registerExpenseCategory(OTHERS, "Others");
        registerExpenseCategory(ACCOMMODATION, "Accommodation");
        registerExpenseCategory(ENTERTAINMENT, "Entertainment");
        registerExpenseCategory(FOOD_AND_DRINKS, "Food & Drinks");
        registerExpenseCategory(HEALTH_AND_FITNESS, "Health & Fitness");
        registerExpenseCategory(HOME_AND_ESSENTIALS, "Home & Essentials");
        registerExpenseCategory(PERSONAL_ITEMS, "Personal Items");
        registerExpenseCategory(SUBSCRIPTIONS, "Subscriptions");
        registerExpenseCategory(TRANSPORTATION, "Transportation");
    }

    private static void registerExpenseCategory(int expenseCategoryID, String expenseCategoryName) {
        expenseCategoryIDMap.put(expenseCategoryName, expenseCategoryID);
        expenseCategoryNamesMap.put(expenseCategoryID, expenseCategoryName);
    }

    public static int getExpenseCategoryID(String expenseCategoryName) {
        return expenseCategoryIDMap.getOrDefault(expenseCategoryName, -1);
    }

    public static String getExpenseCategoryName(int expenseCategoryID) {
        return expenseCategoryNamesMap.getOrDefault(expenseCategoryID, "Unknown Category");
    }

    public static int[] getListOrder() {
        return listOrder;
    }

}
