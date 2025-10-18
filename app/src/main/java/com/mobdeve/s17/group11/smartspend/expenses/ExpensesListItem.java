package com.mobdeve.s17.group11.smartspend.expenses;

import com.mobdeve.s17.group11.smartspend.util.date.Date;

public class ExpensesListItem {

    public ExpensesCategory expensesCategory;
    public float price;
    public Date date;

    public ExpensesListItem(ExpensesCategory expensesCategory, float price, Date date) {
        this.expensesCategory = expensesCategory;
        this.price = price;
        this.date = date;
    }

}
