package com.mobdeve.s17.group11.smartspend.budgets;

import com.mobdeve.s17.group11.smartspend.util.Date;

public class BudgetsListItem {

    public int budgetCategoryID;
    public float amount;
    public Date startDate, endDate;
    public String notes;

    public BudgetsListItem(int budgetCategoryID, float amount, Date startDate, Date endDate, String notes) {
        this.budgetCategoryID = budgetCategoryID;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
    }

}
