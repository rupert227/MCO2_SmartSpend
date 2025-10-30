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

    private static final Map<Integer, String> expensesCategoryNamesMap = new TreeMap<>();
    private static final Map<String, Integer> expensesCategoryIDMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    static {
        registerExpensesCategory(OTHERS, "Others");
        registerExpensesCategory(ACCOMMODATION, "Accommodation");
        registerExpensesCategory(ENTERTAINMENT, "Entertainment");
        registerExpensesCategory(FOOD_AND_DRINKS, "Food & Drinks");
        registerExpensesCategory(HEALTH_AND_FITNESS, "Health & Fitness");
        registerExpensesCategory(HOME_AND_ESSENTIALS, "Home & Essentials");
        registerExpensesCategory(PERSONAL_ITEMS, "Personal Items");
        registerExpensesCategory(SUBSCRIPTIONS, "Subscriptions");
        registerExpensesCategory(TRANSPORTATION, "Transportation");
    }

    private static void registerExpensesCategory(int expensesCategoryID, String expensesCategoryName) {
        expensesCategoryIDMap.put(expensesCategoryName, expensesCategoryID);
        expensesCategoryNamesMap.put(expensesCategoryID, expensesCategoryName);
    }

    public static boolean containsExpensesCategoryName(String expensesCategoryName) {
        return expensesCategoryIDMap.containsKey(expensesCategoryName);
    }

    public static int getExpensesCategoryID(String expensesCategoryName) {
        return expensesCategoryIDMap.getOrDefault(expensesCategoryName, -1);
    }

    public static String getExpensesCategoryName(int expensesCategoryID) {
        return expensesCategoryNamesMap.getOrDefault(expensesCategoryID, "Unknown Category");
    }

    public static int[] getListOrder() {
        return listOrder;
    }

}
