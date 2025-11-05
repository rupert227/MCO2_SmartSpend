package com.mobdeve.s17.group11.smartspend.expenses;

import com.mobdeve.s17.group11.smartspend.util.Date;

public class ExpensesListItem {

    public Date date;
    public float amount;
    public int expensesCategoryID;
    public int listIndex;
    public long sqlRowID;
    public String location;
    public String notes;

    public ExpensesListItem() {}

    public ExpensesListItem(int expensesCategoryID, float amount, Date date, String location, String notes) {
        this.expensesCategoryID = expensesCategoryID;
        this.amount = amount;
        this.date = date;
        this.location = location;
        this.notes = notes;
    }

    public ExpensesListItem(long sqlRowID, int expensesCategoryID, float amount, Date date, String location, String notes) {
        this.sqlRowID = sqlRowID;
        this.expensesCategoryID = expensesCategoryID;
        this.amount = amount;
        this.date = date;
        this.location = location;
        this.notes = notes;
    }

}
