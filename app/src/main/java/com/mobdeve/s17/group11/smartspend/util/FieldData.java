package com.mobdeve.s17.group11.smartspend.util;

public class FieldData {
    
    public static class Budget {

        public boolean use;
        public Date endDate, startDate;
        public float amount;
        public int categoryID;
        public int listIndex;
        public String notes;

    }

    public static class Expense {

        public boolean use;
        public Date date;
        public float amount;
        public int categoryID;
        public int listIndex;
        public String location;
        public String notes;

    }

}
