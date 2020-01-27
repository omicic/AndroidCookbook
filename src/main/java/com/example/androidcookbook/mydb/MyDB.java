package com.example.androidcookbook.mydb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class MyDB {

    private final Context context;
    private final MyDBHelper dbhelper;
    private SQLiteDatabase db;
    public static final String DATABASE_NAME = "cookeryDB";
    public static final int DATABASE_VERSION = 3;

    public MyDB(Context c) {
        context = c;
        dbhelper = new MyDBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public void open() throws SQLiteException {
        try {
            db = dbhelper.getWritableDatabase();

        } catch (SQLiteException ex) {
            db = dbhelper.getReadableDatabase();
        }
    }

    public void close() throws SQLiteException {
        if (db != null) {
            db.close();
        }
    }


    public SQLiteDatabase getDb() {
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }


}
