package com.mobdeve.s17.group11.smartspend.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import androidx.exifinterface.media.ExifInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Algorithm {

    private static final int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public static boolean isPositiveDecimal(String input) {
        if(input == null || input.isEmpty())
            return false;

        return input.matches("\\d*\\.?\\d+") && !input.startsWith("-");
    }

    public static boolean isSignedInteger(String input) {
        if(input == null || input.isEmpty())
            return false;

        return input.matches("[+-]?\\d+");
    }

    public static boolean isValidDate(int day, int month, int year) {
        if(month < 1 || month > 12)
            return false;

        if(day < 1)
            return false;

        daysInMonth[1] = isLeapYear(year) ? 29 : 28;

        return day <= daysInMonth[month - 1];
    }

    public static boolean isValidDate(String dayStr, String monthStr, String yearStr) {
        if(!isSignedInteger(dayStr) || !isSignedInteger(monthStr) || !isSignedInteger(yearStr))
            return false;

        try {
            int day = Integer.parseInt(dayStr);
            int month = Integer.parseInt(monthStr);
            int year = Integer.parseInt(yearStr);

            return isValidDate(day, month, year);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public static List<String> filterStringSearch(List<String> inputStrings, String query) {
        List<String> filteredStrings = new ArrayList<>();

        inputStrings.forEach(str -> {
            if(str.toLowerCase().contains(query.toLowerCase()))
                filteredStrings.add(str);
        });

        return filteredStrings;
    }

    public static List<String> filterStringSearch(String[] inputStrings, String query) {
        List<String> filteredStrings = new ArrayList<>();

        Arrays.stream(inputStrings).forEach(str -> {
            if(str.toLowerCase().contains(query.toLowerCase()))
                filteredStrings.add(str);
        });

        return filteredStrings;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();

        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.preScale(-1, 1);
                break;

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.preScale(1, -1);
                break;

            default:
                return bitmap;
        }

        return Bitmap.createBitmap(
                bitmap,
                0, 0,
                bitmap.getWidth(),
                bitmap.getHeight(),
                matrix,
                true
        );
    }

}
