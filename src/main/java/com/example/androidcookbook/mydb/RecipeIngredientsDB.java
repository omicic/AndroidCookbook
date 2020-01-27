package com.example.androidcookbook.mydb;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.androidcookbook.object.RecipePrepare;

import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientsDB {

    //Constants.
    private String[] allColumnsPrepare = {Constants.KEY_ID_PREPARE, Constants.RECIPE_ID, Constants.INGREDIENTS_ID, Constants.INGREDIENTS_QUANTITY};
    //private String[] allColumnsPrepare = {Constants.KEY_ID_PREPARE,Constants.RECIPE_ID,Constants.,Constants.INGREDIENTS_QUANTITY, Constants.DESCRIPTION};
    private MyDB db;


    public RecipeIngredientsDB(Context c) {
        db = new MyDB(c);
        db.open();
    }

    public MyDB getDb() {
        return db;
    }

    public List<RecipePrepare> getAllRecepteIngredients() {
        List<RecipePrepare> receptlist = new ArrayList<RecipePrepare>();
        Cursor cursor = db.getDb().query(Constants.TABLE_PREPARE,
                allColumnsPrepare, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RecipePrepare receptprepare = cursorToReceptPrepare(cursor);
            receptlist.add(receptprepare);
            cursor.moveToNext();
        }

        cursor.close();
        return receptlist;
    }

    public ArrayList<RecipePrepare> getAllIngredientsByRecipeId(String recipeid) {
        ArrayList<RecipePrepare> receptlist = new ArrayList<RecipePrepare>();
        Cursor cursor = db.getDb().query(Constants.TABLE_PREPARE,
                allColumnsPrepare, Constants.RECIPE_ID + " LIKE '" + recipeid + "%'",
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RecipePrepare ing = cursorToReceptPrepare(cursor);
            receptlist.add(ing);
            cursor.moveToNext();
        }

        cursor.close();
        return receptlist;
    }

    private RecipePrepare cursorToReceptPrepare(Cursor cursor) {
        // TODO Auto-generated method stub
        RecipePrepare recipePrepare = new RecipePrepare();
        recipePrepare.setId(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID_PREPARE)));
        recipePrepare.setRec_id(cursor.getString(cursor.getColumnIndex(Constants.RECIPE_ID)));
        recipePrepare.setIng_id(cursor.getString(cursor.getColumnIndex(Constants.INGREDIENTS_ID)));
        recipePrepare.setIng_qu(cursor.getString(cursor.getColumnIndex(Constants.INGREDIENTS_QUANTITY)));
        return recipePrepare;
    }

    @SuppressLint("LongLogTag")
    public boolean insertRecipeIngredients(String rec_id, String ing_id, String qu) {

        try {
            ContentValues newTaskValue = new ContentValues();
            newTaskValue.put(Constants.RECIPE_ID, rec_id);
            newTaskValue.put(Constants.INGREDIENTS_ID, ing_id);
            newTaskValue.put(Constants.INGREDIENTS_QUANTITY, qu);
            db.getDb().insert(Constants.TABLE_PREPARE, null, newTaskValue);
            return true;
        } catch (SQLiteException ex) {

            Log.v("Greska insert into database", ex.getMessage());
            return false;
        }
    }

    public RecipePrepare getPrepareByRecipeId(int position) {
        Cursor c = db.getDb().query(Constants.TABLE_PREPARE, null,
                Constants.RECIPE_ID + " = ?", null, "" + position, null, null,
                null);
        return (RecipePrepare) c;
    }

    public boolean findPrepareByIds(String rec_id, String ing_id) {

        Cursor cursor = db.getDb().query(Constants.TABLE_PREPARE, allColumnsPrepare,
                Constants.RECIPE_ID + " LIKE '" + rec_id + "' AND " + Constants.INGREDIENTS_ID + " LIKE '" + ing_id + "'", null, null, null, null);

        cursor.moveToFirst();
        if (cursor != null && cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public void updateRecipeIngredients(String rec_id, String ing_id,
                                        String qu) {

        ContentValues updatevalue = new ContentValues();
        String where = Constants.RECIPE_ID + "= '" + rec_id + "' AND " + Constants.INGREDIENTS_ID + " LIKE '" + ing_id + "'";
        updatevalue.put("quantity", qu);
        if (qu != "") {
            updatevalue.put("quantity", qu);
        }
        db.getDb().update(Constants.TABLE_PREPARE, updatevalue, where, null);

    }

    public void remove(String rec_id, String ing_id) {
        db.getDb().delete(Constants.TABLE_PREPARE, Constants.RECIPE_ID + " LIKE '" + rec_id + "' AND " + Constants.INGREDIENTS_ID + " LIKE '" + ing_id + "'",
                null);
    }

    public boolean findPrepareByIds(String string) {
        Cursor cursor = db.getDb().query(Constants.TABLE_PREPARE, allColumnsPrepare, Constants.INGREDIENTS_ID + " LIKE '" + string + "'", null, null, null, null);

        cursor.moveToFirst();
        if (cursor != null && cursor.moveToFirst()) {
            return true;
        } else {

            return false;
        }
    }

    public void removeAllForRec_id(String rec_id) {

        db.getDb().delete(Constants.TABLE_PREPARE, Constants.RECIPE_ID + " LIKE '" + rec_id + "'",
                null);

    }
}
