package com.example.androidcookbook.mydb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.example.androidcookbook.object.Ingredient;

import java.util.ArrayList;

public class IngredientsDB {

    private String[] allColumnsIngredient = {Constants.KEY_ID_ING, Constants.INGREDIENTS, Constants.INGREDIENTS_MU, Constants.KCAL};
    private MyDB db;
    private ArrayList<String> ings;
    private ArrayList<String> mu;
    private Ingredient ing;
    private ArrayList<String> kcal;

    public IngredientsDB(Context c) {
        db = new MyDB(c);
        db.open();
    }

    public MyDB getDb() {
        return db;
    }

    public long insertIngredient(String ing, String mu, String kcal) {

        try {
            ContentValues newTaskValue = new ContentValues();
            newTaskValue.put(Constants.INGREDIENTS, ing);
            newTaskValue.put(Constants.INGREDIENTS_MU, mu);
            newTaskValue.put(Constants.KCAL, kcal);
            return db.getDb().insertOrThrow(Constants.TABLE_INGREDIENTS, null, newTaskValue);
        } catch (SQLiteConstraintException ex) {
            //Log.v("Greska insert into database", ex.getMessage());
            return -1;
        }
    }

    public Cursor getTableIngredients() {
        Cursor c = db.getDb().query(Constants.TABLE_INGREDIENTS, new String[]{"ing"}, null, null, null, null, null);
        return c;
    }

    public Ingredient getIngredientById(String ing_id) {

        Ingredient ing = new Ingredient();
        Cursor cursor = db.getDb().query(Constants.TABLE_INGREDIENTS,
                allColumnsIngredient, Constants.KEY_ID_ING + " LIKE '" + ing_id + "%'",
                null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            ing = cursorToIngredient(cursor);
        }
        cursor.close();
        return ing;
    }

    private Ingredient cursorToIngredient(Cursor cursor) {
        Ingredient ingredient = new Ingredient();
        ingredient.setIng_id(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID_ING)));
        ingredient.setIngredient(cursor.getString(cursor.getColumnIndex(Constants.INGREDIENTS)));
        ingredient.setMu(cursor.getString(cursor.getColumnIndex(Constants.INGREDIENTS_MU)));
        ingredient.setKcal(cursor.getString(cursor.getColumnIndex(Constants.KCAL)));
        return ingredient;
    }


    public ArrayList<String> getIngByName() {
        Cursor cursor = db.getDb().rawQuery("SELECT ing_id, ing, ing_mu, kcal FROM ingredients", null);
        cursor.moveToFirst();
        ings = new ArrayList<String>();
        mu = new ArrayList<String>();
        kcal = new ArrayList<String>();

        while (!cursor.isAfterLast()) {
            ings.add(cursor.getString(cursor.getColumnIndex("ing")));
            mu.add(cursor.getString(cursor.getColumnIndex("ing_mu")));
            kcal.add(cursor.getString(cursor.getColumnIndex("kcal")));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return ings;
    }

    public ArrayList<Ingredient> getAllIngredients() {
        ArrayList<Ingredient> receptlist = new ArrayList<Ingredient>();
        Cursor cursor = db.getDb().query(Constants.TABLE_INGREDIENTS, allColumnsIngredient, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Ingredient recept = cursorToIngredient(cursor);
            receptlist.add(recept);
            cursor.moveToNext();
        }

        cursor.close();
        return receptlist;
    }

    //ne moze da se brise svojstvo ako postoji u receptu
    public void deleteIngredients(String ing_id) {


        db.getDb().delete(Constants.TABLE_INGREDIENTS, Constants.KEY_ID_ING + " = ?", new String[]{String.valueOf(ing_id)});

        db.getDb().delete(Constants.TABLE_RECIPE, Constants.KEY_ID + " = ?", new String[]{String.valueOf(ing_id)});
    }

    public ArrayList<Ingredient> getIngsByName(String ing_name) {
        ArrayList<Ingredient> inglist = new ArrayList<Ingredient>();
        Cursor cursor = db.getDb().query(Constants.TABLE_INGREDIENTS,
                allColumnsIngredient, Constants.INGREDIENTS + " LIKE '" + ing_name + "%'",
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Ingredient ing = cursorToIngredient(cursor);
            inglist.add(ing);
            cursor.moveToNext();
        }

        cursor.close();
        return inglist;
    }

    public void updateIngredient(String id, String update_ing_name,
                                 String mu, String kcal) {

        ContentValues updatevalue = new ContentValues();
        String where = Constants.KEY_ID_ING + "= '" + id + "'";
        updatevalue.put("ing", update_ing_name);

        if (mu != "") {
            updatevalue.put("ing_mu", mu);
        }
        if (kcal != "") {
            updatevalue.put("kcal", kcal);
        }

        db.getDb().update(Constants.TABLE_INGREDIENTS, updatevalue, where, null);

    }

    public Ingredient getIngs(String ing) {
        Ingredient ingredient = new Ingredient();
        Cursor cursor = db.getDb().query(Constants.TABLE_INGREDIENTS,
                allColumnsIngredient, Constants.INGREDIENTS + " = '" + ing + "'",
                null, null, null, null);

        cursor.moveToFirst();
        if (cursor != null && cursor.moveToFirst()) {
            ingredient = cursorToIngredient(cursor);
        }

        cursor.close();
        return ingredient;
    }

}
