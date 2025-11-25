package com.mobdeve.s17.group11.smartspend.util;

import com.mobdeve.s17.group11.smartspend.budgets.BudgetsDatabase;
import com.mobdeve.s17.group11.smartspend.budgets.BudgetsListItem;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesDatabase;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesListItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SessionCache {

    public static BudgetsDatabase budgetsDatabase;
    public static ExpensesDatabase expensesDatabase;

    public static File galleryDirectory;

    public static final List<BudgetsListItem> budgetsItems = new ArrayList<>();
    public static final List<ExpensesListItem> expensesItems = new ArrayList<>();

    public static final PopupSettingsVariables.Analytics analyticsSortSettings = new PopupSettingsVariables.Analytics();
    public static final PopupSettingsVariables.Sort budgetsSortSettings = new PopupSettingsVariables.Sort();
    public static final PopupSettingsVariables.Sort expensesSortSettings = new PopupSettingsVariables.Sort();

    public static final String TEMP_IMAGE_NAME = "temp_img.jpg";

    public static class Color {

        public static int atvNavigationForeground;
        public static int atvNavigationForegroundSelected;
        public static int icoGrayed;
        public static int icoNeutral;
        public static int tvDanger;
        public static int tvLabel1;

    }

}
