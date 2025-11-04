package com.mobdeve.s17.group11.smartspend.budgets;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mobdeve.s17.group11.smartspend.util.Date;

import java.util.ArrayList;

public class BudgetsDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "budgets.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "budgets";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DATE_START = "date_start";
    private static final String COLUMN_DATE_END = "date_end";
    private static final String COLUMN_NOTES = "notes";

    public BudgetsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String table =
                "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY_ID + " INTEGER, " +
                COLUMN_AMOUNT + " REAL, " +
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
        ContentValues values = new ContentValues();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        values.put(COLUMN_CATEGORY_ID, budget.budgetsCategoryID);
        values.put(COLUMN_AMOUNT, budget.amount);
        values.put(COLUMN_DATE_START, budget.startDate.getUniqueValue());
        values.put(COLUMN_DATE_END, budget.endDate.getUniqueValue());
        values.put(COLUMN_NOTES, budget.notes);

        long rowID = sqLiteDatabase.insert(TABLE_NAME, null, values);

        sqLiteDatabase.close();
        return rowID;
    }

    public void deleteBudget(long rowID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(rowID)});
        sqLiteDatabase.close();
    }

    public int updateBudget(long rowID, BudgetsListItem budget) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        contentValues.put(COLUMN_CATEGORY_ID, budget.budgetsCategoryID);
        contentValues.put(COLUMN_AMOUNT, budget.amount);
        contentValues.put(COLUMN_DATE_START, budget.startDate.getUniqueValue());
        contentValues.put(COLUMN_DATE_END, budget.endDate.getUniqueValue());
        contentValues.put(COLUMN_NOTES, budget.notes);

        int rows = sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_ID + "=?", new String[]{String.valueOf(rowID)});

        sqLiteDatabase.close();
        return rows;
    }

    public BudgetsListItem getBudgetByID(int rowID) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE id = ?",
                new String[]{String.valueOf(rowID)}
        );

        BudgetsListItem budget = null;

        if(cursor.moveToFirst()) {
            budget = new BudgetsListItem(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)),
                    cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)),
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
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC",
                null
        );

        if(cursor.moveToFirst()) {
            do {
                budgets.add(new BudgetsListItem(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)),
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
