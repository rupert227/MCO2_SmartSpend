package com.mobdeve.s17.group11.smartspend.expenses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mobdeve.s17.group11.smartspend.util.Date;

import java.util.ArrayList;

public class ExpensesDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "expenses.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "expenses";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_NOTES = "notes";

    public ExpensesDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String table =
                "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY_ID + " INTEGER, " +
                COLUMN_AMOUNT + " REAL, " +
                COLUMN_DATE + " INTEGER, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_NOTES + " TEXT" + ")";

        sqLiteDatabase.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addExpense(ExpensesListItem expense) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        contentValues.put(COLUMN_CATEGORY_ID, expense.expensesCategoryID);
        contentValues.put(COLUMN_AMOUNT, expense.amount);
        contentValues.put(COLUMN_DATE, expense.date.getUniqueValue());
        contentValues.put(COLUMN_LOCATION, expense.location);
        contentValues.put(COLUMN_NOTES, expense.notes);

        long rowID = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        sqLiteDatabase.close();
        return rowID;
    }

    public void deleteExpense(long rowID) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        sqLiteDatabase.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(rowID)});
        sqLiteDatabase.close();
    }

    public int updateExpense(long rowID, ExpensesListItem expense) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        contentValues.put(COLUMN_CATEGORY_ID, expense.expensesCategoryID);
        contentValues.put(COLUMN_AMOUNT, expense.amount);
        contentValues.put(COLUMN_DATE, expense.date.getUniqueValue());
        contentValues.put(COLUMN_LOCATION, expense.location);
        contentValues.put(COLUMN_NOTES, expense.notes);

        int rows = sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_ID + "=?", new String[]{String.valueOf(rowID)});

        sqLiteDatabase.close();
        return rows;
    }

    public <T> boolean updateExpenseRow(long rowID, String column, T value) {
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

    public ExpensesListItem getExpenseByID(int rowID) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE id = ?",
                new String[]{String.valueOf(rowID)}
        );

        ExpensesListItem expense = null;

        if(cursor.moveToFirst()) {
            expense = new ExpensesListItem(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)),
                    cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)),
                    new Date(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DATE))),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTES))
            );

            expense.sqlRowID = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }

        cursor.close();
        sqLiteDatabase.close();
        return expense;
    }

    public ArrayList<ExpensesListItem> getAllExpenses() {
        ArrayList<ExpensesListItem> expenses = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC",
                null
        );

        if(cursor.moveToFirst()) {
            do {
                expenses.add(new ExpensesListItem(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)),
                        new Date(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DATE))),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTES))
                ));
            } while(cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();
        return expenses;
    }

}
