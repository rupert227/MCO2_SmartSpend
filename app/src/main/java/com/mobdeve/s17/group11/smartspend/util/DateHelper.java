package com.mobdeve.s17.group11.smartspend.util;

import java.util.Calendar;

public class DateHelper {

    private static final Calendar calendar = Calendar.getInstance();

    public static final String[] longMonthNames = {"Null", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public static final String[] shortMonthNames = {"Nul", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public static Date getCurrentDate() {
        return new Date(
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR)
        );
    }

    public static String numericalDateTransform0(Date date) {
        int month = date.month >= 0 && date.month < longMonthNames.length ? date.month : 0;

        return longMonthNames[month] + " " + date.day + ", " + date.year;
    }

    public static String numericalDateTransform1(Date date) {
        int month = date.month >= 0 && date.month < longMonthNames.length ? date.month : 0;

        return shortMonthNames[month] + ". " + date.day + ", " + date.year;
    }

}
