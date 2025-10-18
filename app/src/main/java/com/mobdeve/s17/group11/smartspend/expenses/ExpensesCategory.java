package com.mobdeve.s17.group11.smartspend.expenses;

public enum ExpensesCategory {

    ACCOMMODATION("Accommodation"),
    ENTERTAINMENT("Entertainment"),
    FOOD_AND_DRINKS("Food & Drinks"),
    HEALTH_AND_FITNESS("Health & Fitness"),
    HOME_AND_ESSENTIALS("Home & Essentials"),
    PERSONAL_ITEMS("Personal Items"),
    SUBSCRIPTIONS("Subscriptions"),
    TRANSPORTATION("Transportation"),
    OTHERS("Others");
    private final String displayName;

    ExpensesCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
