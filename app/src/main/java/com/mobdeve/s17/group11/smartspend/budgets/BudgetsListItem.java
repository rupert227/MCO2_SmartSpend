package com.mobdeve.s17.group11.smartspend.budgets;

import com.mobdeve.s17.group11.smartspend.util.Date;

public class BudgetsListItem {

    public int budgetCategoryID;
    public float price;
    public Date startDate, endDate;

    public BudgetsListItem(int budgetCategoryID, float price, Date startDate, Date endDate) {
        this.budgetCategoryID = budgetCategoryID;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
