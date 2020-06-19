package com.example.androidcookbook.mydb;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.androidcookbook.object.RecipeMenu;

import java.util.ArrayList;

public class MenuDB {
    private String[] allColumnsMenu = {Constants.MENU_ID, Constants.REC_ID, Constants.DAY, Constants.MENU_CHECK, Constants.MAIN_MEAL};
    private MyDB db;


    public MenuDB(Context c) {
        db = new MyDB(c);
        db.open();
    }

    public MyDB getDb() {
        return db;
    }

    @SuppressLint("LongLogTag")
    public long insertMenu(String rec_id, String d, String mcheck, String mainmenu) {

        try {
            ContentValues newTaskValue = new ContentValues();
            newTaskValue.put(Constants.REC_ID, rec_id);
            newTaskValue.put(Constants.DAY, d);
            newTaskValue.put(Constants.MENU_CHECK, mcheck);
            newTaskValue.put(Constants.MAIN_MEAL, mainmenu);

            return db.getDb().insert(Constants.TABLE_MENU, null, newTaskValue);
        } catch (SQLiteException ex) {
            Log.v("Insert into database error", ex.getMessage());
            return -1;
        }
    }

    public Cursor getTableMenu() {
        Cursor c = db.getDb().query(Constants.TABLE_MENU, new String[]{"rec_id"}, null, null, null, null, null);
        c.close();
        return c;
    }


    private RecipeMenu cursorToMenu(Cursor cursor) {
        RecipeMenu recipeMenu = new RecipeMenu();
        recipeMenu.setMenu_id(cursor.getString(cursor.getColumnIndex(Constants.MENU_ID)));
        recipeMenu.setRec_id(cursor.getString(cursor.getColumnIndex(Constants.REC_ID)));
        recipeMenu.setDay(cursor.getString(cursor.getColumnIndex(Constants.DAY)));
        recipeMenu.setCheck(cursor.getString(cursor.getColumnIndex(Constants.MENU_CHECK)));
        recipeMenu.setMainmeal(cursor.getString(cursor.getColumnIndex(Constants.MAIN_MEAL)));
        return recipeMenu;
    }

    public ArrayList<RecipeMenu> getAllMenu() {
        ArrayList<RecipeMenu> receptmenu = new ArrayList<RecipeMenu>();
        Cursor cursor = db.getDb().query(Constants.TABLE_MENU, allColumnsMenu, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RecipeMenu menu = cursorToMenu(cursor);
            receptmenu.add(menu);
            cursor.moveToNext();
        }

        cursor.close();
        return receptmenu;
    }

    public void updateMenu(String update_rec_id, String d, String mcheck, String mainmeal) {

        ContentValues updatevalue = new ContentValues();
        String where = Constants.REC_ID + "= '" + update_rec_id + "'" + " AND " + Constants.DAY + "= '" + d + "'";

        if (mcheck != "") {
            updatevalue.put("m_check", mcheck);
        }

        if (mainmeal != null) {
            updatevalue.put("main_meal", mainmeal);
        }

        db.getDb().update(Constants.TABLE_MENU, updatevalue, where, null);

    }

    public void deleteMenu(String rec_id, String day) {
        db.getDb().delete(Constants.TABLE_MENU, Constants.REC_ID + " = ?" + " AND " +
                Constants.DAY + " = ?", new String[]{String.valueOf(rec_id), String.valueOf(day)});

    }

    public boolean findMenuById(String rec_id, String d) {

        Cursor cursor = db.getDb().query(Constants.TABLE_MENU, allColumnsMenu,
                Constants.REC_ID + " LIKE '" + rec_id + "'" + " AND " + Constants.DAY + " LIKE '" + d + "'", null, null, null, null);

        cursor.moveToFirst();

        if (cursor != null && cursor.moveToFirst()) {
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public RecipeMenu getMenuByRecId(String rec_id, String d) {

        RecipeMenu rec_menu = new RecipeMenu();
        Cursor cursor = db.getDb().query(Constants.TABLE_MENU,
                allColumnsMenu, Constants.REC_ID + " LIKE '" + rec_id + "'" + " AND " + Constants.DAY + " LIKE '" + d + "'",
                null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            rec_menu = cursorToMenu(cursor);
        }
        cursor.close();
        return rec_menu;
    }

    public void deleteForRecipe(String rec_id) {
        db.getDb().delete(Constants.TABLE_MENU, Constants.REC_ID + " = ?", new String[]{String.valueOf(rec_id)});

    }

    public void updateMenu(String recid, boolean status) {

        ContentValues updatevalue = new ContentValues();
        String where = Constants.REC_ID + "= '" + recid + "'";
        updatevalue.put("m_check", status);
        db.getDb().update(Constants.TABLE_MENU, updatevalue, where, null);

    }

    public RecipeMenu getMenuByMenuId(String menu_id) {

        RecipeMenu rec_menu = new RecipeMenu();
        Cursor cursor = db.getDb().query(Constants.TABLE_MENU,
                allColumnsMenu, Constants.MENU_ID + " LIKE '" + menu_id + "'",
                null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            rec_menu = cursorToMenu(cursor);
        }
        cursor.close();
        return rec_menu;

    }
}
