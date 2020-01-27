package com.example.androidcookbook.mydb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE_RECEPT = "create table " + Constants.TABLE_RECIPE + "(" + Constants.KEY_ID +
            " integer primary key autoincrement," + Constants.NAME + " text not null," + Constants.CATEGORY + " text not null, " + Constants.DESCRIPTION + " text," + Constants.PICTURE + " text);";
    private static final String CREATE_TABLE_PREPARE = "create table " + Constants.TABLE_PREPARE + "(" + Constants.KEY_ID_PREPARE + " integer primary key autoincrement," + Constants.RECIPE_ID + " text," + Constants.INGREDIENTS_ID +
            " text," + Constants.INGREDIENTS_QUANTITY + " text);";
    private static final String CREATE_TABLE_INGREDIENTS = "create table " + Constants.TABLE_INGREDIENTS + "(" + Constants.KEY_ID_ING + " integer primary key autoincrement," + Constants.INGREDIENTS + " text UNIQUE," + Constants.INGREDIENTS_MU + " text, " + Constants.KCAL + " text);";

    private static final String CREATE_TABLE_MENU = "create table " + Constants.TABLE_MENU + "(" + Constants.MENU_ID + " integer primary key autoincrement," + Constants.REC_ID + " text," + Constants.DAY + " text," + Constants.MENU_CHECK + " text," + Constants.MAIN_MEAL + " text);";


    public MyDBHelper(Context context, String name, CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(CREATE_TABLE_RECEPT);
            db.execSQL(CREATE_TABLE_PREPARE);
            db.execSQL(CREATE_TABLE_INGREDIENTS);
            db.execSQL(CREATE_TABLE_MENU);

        } catch (SQLiteException ex) {
            Log.v("Create table exception", ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("TaskDBAdapter", "Upgrading from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("drop table if exists " + Constants.TABLE_RECIPE);
        db.execSQL("drop table if exists " + Constants.TABLE_PREPARE);
        db.execSQL("drop table if exists " + Constants.TABLE_INGREDIENTS);
        db.execSQL("drop table if exists " + Constants.TABLE_MENU);
        onCreate(db);
    }
}
