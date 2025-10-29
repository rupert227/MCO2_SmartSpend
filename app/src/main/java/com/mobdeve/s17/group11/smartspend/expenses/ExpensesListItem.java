package com.mobdeve.s17.group11.smartspend.expenses;

import com.mobdeve.s17.group11.smartspend.util.Date;

public class ExpensesListItem {

    public int expensesCategoryID;
    public float price;
    public Date date;

    public ExpensesListItem(int expensesCategoryID, float price, Date date) {
        this.expensesCategoryID = expensesCategoryID;
        this.price = price;
        this.date = date;
    }

}
