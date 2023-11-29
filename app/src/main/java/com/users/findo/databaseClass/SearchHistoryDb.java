package com.users.findo.databaseClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SearchHistoryDb extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FindoSearchHistory.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "SearchHistory";
    private static final String SEARCH_KEYWORD = "searchKeyword";

    public SQLiteDatabase getDatabase() {
        return getWritableDatabase();
    }

    public SearchHistoryDb(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + SEARCH_KEYWORD +  " TEXT PRIMARY KEY )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertData(String search){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SEARCH_KEYWORD, search);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }



    public boolean isTableEmpty(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        boolean isEmpty = cursor.getCount() == 0;
        cursor.close();
        return isEmpty;
    }

    public ArrayList<String> getData() {
        ArrayList<String> data = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                String search_history = cursor.getString(cursor.getColumnIndex(SEARCH_KEYWORD));
                data.add(search_history);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return data;
    }

    public boolean updateData(String search) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SEARCH_KEYWORD, search);
        int result = db.update(TABLE_NAME, values, SEARCH_KEYWORD + " = ?",new String[] {search});
        db.close();
        return result > 0;
    }

    public void deleteItem(String search){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, SEARCH_KEYWORD + " = ?",new String[] {search});
        db.close();
    }

    public boolean itemExist(String search) {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + SEARCH_KEYWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{search});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return exists;
    }
}

