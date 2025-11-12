package com.mobdeve.s17.group11.smartspend.budgets;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mobdeve.s17.group11.smartspend.util.Date;

import java.util.ArrayList;

public class BudgetsDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "budgets.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "budgets";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_AMOUNT_CURRENT = "amount_current";
    public static final String COLUMN_AMOUNT_MAX = "amount_max";
    public static final String COLUMN_DATE_START = "date_start";
    public static final String COLUMN_DATE_END = "date_end";
    public static final String COLUMN_NOTES = "notes";

    public BudgetsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String table =
                "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY_ID + " INTEGER, " +
                COLUMN_AMOUNT_CURRENT + " REAL, " +
                COLUMN_AMOUNT_MAX + " REAL, " +
                COLUMN_DATE_START + " INTEGER, " +
                COLUMN_DATE_END + " INTEGER, " +
                COLUMN_NOTES + " TEXT" + ")";

        sqLiteDatabase.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addBudget(BudgetsListItem budget) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        contentValues.put(COLUMN_CATEGORY_ID, budget.expensesCategoryID);
        contentValues.put(COLUMN_AMOUNT_CURRENT, budget.currentAmount);
        contentValues.put(COLUMN_AMOUNT_MAX, budget.maxAmount);
        contentValues.put(COLUMN_DATE_START, budget.startDate.getUniqueValue());
        contentValues.put(COLUMN_DATE_END, budget.endDate.getUniqueValue());
        contentValues.put(COLUMN_NOTES, budget.notes);

        long rowID = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        sqLiteDatabase.close();
        return rowID;
    }

    public void deleteBudget(long rowID) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        sqLiteDatabase.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(rowID)});
        sqLiteDatabase.close();
    }

    public int updateBudget(long rowID, BudgetsListItem budget) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        contentValues.put(COLUMN_CATEGORY_ID, budget.expensesCategoryID);
        contentValues.put(COLUMN_AMOUNT_CURRENT, budget.currentAmount);
        contentValues.put(COLUMN_AMOUNT_MAX, budget.maxAmount);
        contentValues.put(COLUMN_DATE_START, budget.startDate.getUniqueValue());
        contentValues.put(COLUMN_DATE_END, budget.endDate.getUniqueValue());
        contentValues.put(COLUMN_NOTES, budget.notes);

        int rows = sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_ID + "=?", new String[]{String.valueOf(rowID)});

        sqLiteDatabase.close();
        return rows;
    }

    public <T> boolean updateBudgetRow(long rowID, String column, T value) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        if(value instanceof String)
            contentValues.put(column, (String) value);
        else if(value instanceof Integer)
            contentValues.put(column, (Integer) value);
        else if(value instanceof Float)
            contentValues.put(column, (Float) value);
        else if(value instanceof Double)
            contentValues.put(column, (Double) value);
        else if(value instanceof Long)
            contentValues.put(column, (Long) value);
        else if(value instanceof Boolean)
            contentValues.put(column, (Boolean) value);
        else
            throw new IllegalArgumentException("Unsupported value type: " + value.getClass().getName());

        int rows = sqLiteDatabase.update(TABLE_NAME, contentValues, "id = ?", new String[]{String.valueOf(rowID)});

        sqLiteDatabase.close();
        return rows > 0;
    }

    public BudgetsListItem getBudgetByID(int rowID) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE id = ?",
                new String[]{String.valueOf(rowID)}
        );

        BudgetsListItem budget = null;

        if(cursor.moveToFirst()) {
            budget = new BudgetsListItem(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)),
                    cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT_CURRENT)),
                    cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT_MAX)),
                    new Date(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DATE_START))),
                    new Date(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DATE_END))),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTES))
            );

            budget.sqlRowID = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }

        cursor.close();
        sqLiteDatabase.close();
        return budget;
    }

    public ArrayList<BudgetsListItem> getAllBudgets() {
        ArrayList<BudgetsListItem> budgets = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC",
                null
        );

        if(cursor.moveToFirst()) {
            do {
                budgets.add(new BudgetsListItem(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT_CURRENT)),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT_MAX)),
                        new Date(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DATE_START))),
                        new Date(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DATE_END))),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTES))
                ));
            } while(cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();
        return budgets;
    }

}
