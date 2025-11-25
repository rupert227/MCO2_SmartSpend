package com.mobdeve.s17.group11.smartspend.expenses;

import android.graphics.Color;

import java.util.HashMap;
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

    private static final Map<Integer, Integer> expensesCategoryColorMap = new HashMap<>();
    private static final Map<String, Integer> expensesCategoryIDMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static final Map<Integer, String> expensesCategoryNamesMap = new TreeMap<>();

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

        float[] tempHSV = new float[3];

        for(int i = 0; i < listOrder.length; i++) {
            tempHSV[0] = ((float) i / (float) listOrder.length) * 360.0f;
            tempHSV[1] = 0.75f;
            tempHSV[2] = 1.0f;

            expensesCategoryColorMap.put(listOrder[i], Color.HSVToColor(tempHSV));
        }
    }

    public static String getExpensesCategoryName(int expensesCategoryID) {
        return expensesCategoryNamesMap.getOrDefault(expensesCategoryID, "Unknown Category");
    }

    public static boolean containsExpensesCategoryName(String expensesCategoryName) {
        return expensesCategoryIDMap.containsKey(expensesCategoryName);
    }

    public static int getExpensesCategoryColor(int expensesCategoryID) {
        return expensesCategoryColorMap.getOrDefault(expensesCategoryID, 0);
    }

    public static int getExpensesCategoryID(String expensesCategoryName) {
        return expensesCategoryIDMap.getOrDefault(expensesCategoryName, -1);
    }

    public static int[] getListOrder() {
        return listOrder;
    }

    private static void registerExpensesCategory(int expensesCategoryID, String expensesCategoryName) {
        expensesCategoryIDMap.put(expensesCategoryName, expensesCategoryID);
        expensesCategoryNamesMap.put(expensesCategoryID, expensesCategoryName);
    }

}
