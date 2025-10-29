package com.mobdeve.s17.group11.smartspend.util;

import com.mobdeve.s17.group11.smartspend.expenses.ExpensesCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Algorithm {

    public static final Map<String, String> expenseCategoryDisplayNameMap = new HashMap<String, String>();

    public static void init() {

        Arrays.stream(ExpensesCategory.values()).forEach(expensesCategory -> {
            expenseCategoryDisplayNameMap.put(
                    expensesCategory.getDisplayName().toLowerCase(),
                    expensesCategory.getDisplayName()
            );
        });

    }

    public static List<String> filterStringSearch(List<String> inputStrings, String query) {
        List<String> filteredStrings = new ArrayList<>();

        inputStrings.forEach(str -> {
            if(str.toLowerCase().contains(query.toLowerCase()))
                filteredStrings.add(str);
        });

        return filteredStrings;
    }

    public static List<String> filterStringSearch(String[] inputStrings, String query) {
        List<String> filteredStrings = new ArrayList<>();

        Arrays.stream(inputStrings).forEach(str -> {
            if(str.toLowerCase().contains(query.toLowerCase()))
                filteredStrings.add(str);
        });

        return filteredStrings;
    }

}
