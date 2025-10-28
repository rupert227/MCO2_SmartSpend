package com.mobdeve.s17.group11.smartspend.budgets;

import com.mobdeve.s17.group11.smartspend.expenses.ExpensesCategory;
import com.mobdeve.s17.group11.smartspend.util.Date;

public class BudgetsListItem {

    public ExpensesCategory budgetCategory;
    public float price;
    public Date startDate, endDate;

    public BudgetsListItem(ExpensesCategory budgetCategory, float price, Date startDate, Date endDate) {
        this.budgetCategory = budgetCategory;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
