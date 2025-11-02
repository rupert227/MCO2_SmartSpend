package com.mobdeve.s17.group11.smartspend.util;

public class Date {

    public int day, month, year;

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getUniqueValue() {
        return year << 9 | month << 5 | day;
    }

}
