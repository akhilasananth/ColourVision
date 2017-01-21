package com.example.home.camera;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by robertfernandes on 1/19/2017.
 */
public class DBHandler extends SQLiteOpenHelper {

    private int id;

    public static final String DATABASE_NAME = "database.db";
    public static final String ITEM_TABLE_NAME = "items";
    public static final String ITEM_COLUMN_ID = "id";
    public static final String ITEM_COLUMN_COLOR = "color";

    public DBHandler (Context context) {
        this(context, DATABASE_NAME, null, 1);
    }

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table " + ITEM_TABLE_NAME + "(" +
                ITEM_COLUMN_ID + " integer primary key," +
                ITEM_COLUMN_COLOR + " text" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertItem(String color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_COLUMN_COLOR, color);
        db.insert(ITEM_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + ITEM_TABLE_NAME + " where id = " + id + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, ITEM_TABLE_NAME);
    }

    public boolean updateItem(Integer id, String color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_COLUMN_COLOR, color);
        db.update(ITEM_TABLE_NAME, contentValues, "id = ?", new String[] {Integer.toString(id)});
        return true;
    }

    public Integer deleteItem(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ITEM_TABLE_NAME,
                "id = ?",
                new String[] {Integer.toString(id)});
    }

    public HashMap<Integer, String> getAllItems() {
        HashMap<Integer, String> map = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + ITEM_TABLE_NAME, null);
        res.moveToFirst();

        while(res.isAfterLast() == false) {
            map.put(res.getInt(res.getColumnIndex(ITEM_COLUMN_ID)), res.getString(res.getColumnIndex(ITEM_COLUMN_COLOR)));
            res.moveToNext();
        }
        return map;
    }
}
