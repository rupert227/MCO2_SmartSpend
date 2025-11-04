package com.mobdeve.s17.group11.smartspend.budgets;

import com.mobdeve.s17.group11.smartspend.util.Date;

public class BudgetsListItem {

    public Date endDate, startDate;
    public float amount;
    public int budgetsCategoryID;
    public long sqlRowID;
    public String notes;

    public BudgetsListItem() {}

    public BudgetsListItem(int budgetsCategoryID, float amount, Date startDate, Date endDate, String notes) {
        this.budgetsCategoryID = budgetsCategoryID;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
    }

    public BudgetsListItem(long sqlRowID, int budgetsCategoryID, float amount, Date startDate, Date endDate, String notes) {
        this.sqlRowID = sqlRowID;
        this.budgetsCategoryID = budgetsCategoryID;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
    }

}
