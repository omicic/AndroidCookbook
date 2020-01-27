package com.example.androidcookbook.mydb;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.androidcookbook.object.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeDB {

    private String[] allColumnsRecept = {Constants.KEY_ID, Constants.NAME,
            Constants.CATEGORY, Constants.DESCRIPTION, Constants.PICTURE};
    private MyDB db;
    private String description;
    private String autoInc;

    public RecipeDB(Context c) {
        db = new MyDB(c);
        db.open();
    }


    public MyDB getDb() {
        return db;
    }

    //it's working
    @SuppressLint("LongLogTag")
    public long insertRecept(String naziv, String kategorija,
                             String descriptions, String selectedImagePath) {

        try {

            ContentValues newTaskValue = new ContentValues();
            newTaskValue.put(Constants.NAME, naziv);
            newTaskValue.put(Constants.CATEGORY, kategorija);
            newTaskValue.put(Constants.DESCRIPTION, descriptions);
            newTaskValue.put(Constants.PICTURE, selectedImagePath);

            return db.getDb().insert(Constants.TABLE_RECIPE, null, newTaskValue);
        } catch (SQLiteException ex) {
            Log.v("Error insert into database", ex.getMessage());
            return -1;
        }
    }

    //it's working
    public ArrayList<Recipe> getAllRecepte() {
        ArrayList<Recipe> receptlist = new ArrayList<Recipe>();

        Cursor cursor = db.getDb().query(Constants.TABLE_RECIPE,
                allColumnsRecept, null, null, null, null, null);

        //Log.d("dodje","dodje");
        cursor.moveToFirst();
        //Log.d("dodje","dodje");
        while (!cursor.isAfterLast()) {

            Recipe recept = cursorToRecept(cursor);
            receptlist.add(recept);
            cursor.moveToNext();
        }
        cursor.close();
        return receptlist;

    }

    public List<Recipe> getAllRecepteById(int id) {
        List<Recipe> receptlist = new ArrayList<Recipe>();
        Cursor cursor = db.getDb().query(Constants.TABLE_RECIPE,
                allColumnsRecept, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Recipe recept = cursorToRecept(cursor);
            receptlist.add(recept);
            cursor.moveToNext();
        }

        cursor.close();
        return receptlist;
    }

    public List<Recipe> getAllRecepteByNaziv(String naziv) {
        List<Recipe> receptlist = new ArrayList<Recipe>();
        Cursor cursor = db.getDb().query(Constants.TABLE_RECIPE,
                allColumnsRecept, Constants.NAME + " LIKE '" + naziv + "%'",
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Recipe recept = cursorToRecept(cursor);
            receptlist.add(recept);
            cursor.moveToNext();
        }

        cursor.close();
        return receptlist;
    }

    public Recipe getRecepteById(String position) {
        Recipe recept = new Recipe();

        Cursor cursor = db.getDb().query(Constants.TABLE_RECIPE, allColumnsRecept,
                Constants.KEY_ID + " LIKE '" + position + "'", null, null, null, null);

        cursor.moveToFirst();
        if (cursor != null && cursor.moveToFirst()) {
            recept.setId(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID)));
            recept.setRecipe(cursor.getString(cursor.getColumnIndex(Constants.NAME)));
            recept.setCategory(cursor.getString(cursor.getColumnIndex(Constants.CATEGORY)));
            recept.setDescrtiption(cursor.getString(cursor.getColumnIndex(Constants.DESCRIPTION)));
            recept.setPicture(cursor.getString(cursor.getColumnIndex(Constants.PICTURE)));
            cursor.close();
        }

        cursor.close();
        return recept;
    }

    //ok
    public void updateRecept(String rec_id, String update_rec_name, String kategorija, String direction, String picture) {

        ContentValues updatevalue = new ContentValues();
        String where = Constants.KEY_ID + "= '" + rec_id + "'";

        updatevalue.put("name", update_rec_name);

        if (direction != "") {
            updatevalue.put("descriptions", direction);
        }

        if (kategorija != "") {
            updatevalue.put("category", kategorija);
        }

        updatevalue.put("picture", picture);

        db.getDb().update(Constants.TABLE_RECIPE, updatevalue, where, null);

    }

    public Cursor getTableRecept() {
        Cursor c = db.getDb().query(Constants.TABLE_RECIPE, null, null, null,
                null, null, null);

        return c;
    }

    public Recipe cursorToRecept(Cursor cursor) {
        Recipe recept = new Recipe();
        recept.setId(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID)));
        recept.setRecipe(cursor.getString(cursor.getColumnIndex(Constants.NAME)));
        recept.setCategory(cursor.getString(cursor.getColumnIndex(Constants.CATEGORY)));
        recept.setDescrtiption(cursor.getString(cursor.getColumnIndex(Constants.DESCRIPTION)));
        recept.setPicture(cursor.getString(cursor.getColumnIndex(Constants.PICTURE)));
        return recept;
    }


    //it's working
    public void deleteRecept(String rec_id) {
        db.getDb().delete(Constants.TABLE_PREPARE, Constants.RECIPE_ID + " = ?", new String[]{String.valueOf(rec_id)});
        db.getDb().delete(Constants.TABLE_RECIPE, Constants.KEY_ID + " = ?",
                new String[]{String.valueOf(rec_id)});
    }


    public Recipe getReceptByNaziv(String naziv) {
        Cursor c = db.getDb().query(Constants.TABLE_RECIPE, null,
                Constants.NAME + " = ?", null, "" + naziv, null, null, null);
        return (Recipe) c;
    }

    public boolean findRecepteById(String position) {

        Cursor cursor = db.getDb().query(Constants.TABLE_RECIPE, allColumnsRecept,
                Constants.KEY_ID + " LIKE '" + position + "'", null, null, null, null);

        cursor.moveToFirst();
        if (cursor != null && cursor.moveToFirst()) {
            return true;
        } else {

            return false;
        }
    }

    public String getDirectionById(String rec_id) {
        Cursor cursor = db.getDb().query(Constants.TABLE_RECIPE, allColumnsRecept,
                Constants.KEY_ID + " LIKE '" + rec_id + "'", null, null, null, null);

        cursor.moveToFirst();
        if (cursor != null && cursor.moveToFirst()) {
            description = cursor.getString(cursor.getColumnIndex(Constants.DESCRIPTION));
            cursor.close();
        }

        return description;

    }

    public String getLastId() {
        String query = "SELECT seq FROM SQLITE_SEQUENCE where name = 'recipe';";
        Cursor cursor = db.getDb().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                autoInc = cursor.getString(cursor.getColumnIndex("seq"));
            } while (cursor.moveToNext());
        } else {
            autoInc = "";
        }
        cursor.close();
        //Log.d("getlastid from recipeDB", autoInc.toString());
        return autoInc;
    }

    public ArrayList<Recipe> getAllRecipeByCategory(String category) {

        List<Recipe> receptlist = new ArrayList<Recipe>();
        Cursor cursor = db.getDb().query(Constants.TABLE_RECIPE,
                allColumnsRecept, Constants.CATEGORY + " LIKE '" + category + "%'",
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Recipe recept = cursorToRecept(cursor);
            receptlist.add(recept);
            cursor.moveToNext();
        }

        cursor.close();
        return (ArrayList<Recipe>) receptlist;

    }
}
