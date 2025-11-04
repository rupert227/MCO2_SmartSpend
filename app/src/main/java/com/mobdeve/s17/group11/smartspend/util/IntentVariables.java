package com.mobdeve.s17.group11.smartspend.util;

import com.mobdeve.s17.group11.smartspend.budgets.BudgetsListItem;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesListItem;

public class IntentVariables {
    
    public static class BudgetEdit {

        public BudgetsListItem budget = new BudgetsListItem();
        public int listIndex;

    }

    public static class ExpenseEdit {

        public ExpensesListItem expense = new ExpensesListItem();
        public int listIndex;

    }

}
