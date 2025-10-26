package com.mobdeve.s17.group11.smartspend.util;

public class FormatHelper {

    public static String floatToPrice(float value) {
        return String.format("%.2f", value);
    }

    public static String doubleToPrice(double value) {
        return String.format("%.2d", value);
    }

}
