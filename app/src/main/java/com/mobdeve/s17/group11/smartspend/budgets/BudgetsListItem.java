package com.mobdeve.s17.group11.smartspend.budgets;

import com.mobdeve.s17.group11.smartspend.util.Date;

public class BudgetsListItem {

    public Date endDate, startDate;
    public float currentAmount, maxAmount;
    public int expensesCategoryID;
    public int listIndex;
    public long sqlRowID;
    public String notes;

    public BudgetsListItem() {}

    public BudgetsListItem(int expensesCategoryID, float currentAmount, float maxAmount, Date startDate, Date endDate, String notes) {
        this.expensesCategoryID = expensesCategoryID;
        this.currentAmount = currentAmount;
        this.maxAmount = maxAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
    }

    public BudgetsListItem(long sqlRowID, int expensesCategoryID, float currentAmount, float maxAmount, Date startDate, Date endDate, String notes) {
        this.sqlRowID = sqlRowID;
        this.expensesCategoryID = expensesCategoryID;
        this.currentAmount = currentAmount;
        this.maxAmount = maxAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
    }

}
