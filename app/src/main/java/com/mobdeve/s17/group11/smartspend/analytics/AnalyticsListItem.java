package com.mobdeve.s17.group11.smartspend.analytics;

public class AnalyticsListItem {

    public float amount;
    public int expensesCategoryID;

    public AnalyticsListItem(int expensesCategoryID, float amount) {
        this.expensesCategoryID = expensesCategoryID;
        this.amount = amount;
    }

}
