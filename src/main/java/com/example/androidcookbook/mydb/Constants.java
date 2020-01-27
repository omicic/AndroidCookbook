package com.example.androidcookbook.mydb;

//import com.example.androidcook.R;

public class Constants {


    public static final String TABLE_RECIPE = "recipe";
    public static final String TABLE_PREPARE = "prepare";
    public static final String TABLE_INGREDIENTS = "ingredients";
    public static final String TABLE_MENU = "menu";

    //table recipe
    public static final String NAME = "name";
    public static final String CATEGORY = "category";
    public static final String KEY_ID = "_id";
    public static final String DESCRIPTION = "descriptions";
    public static final String PICTURE = "picture";

    //table recipeprepare
    public static final String KEY_ID_PREPARE = "_id";
    public static final String RECIPE_ID = "recipe_id";
    public static final String INGREDIENTS_ID = "ing_id";
    public static final String INGREDIENTS_QUANTITY = "quantity";

    //table ingredients
    public static final String KEY_ID_ING = "_id";
    public static final String INGREDIENTS = "ing";
    public static final String INGREDIENTS_MU = "ing_mu";
    public static final String KCAL = "kcal";

    //table menu
    public static final String MENU_ID = "_id";
    public static final String REC_ID = "recid";
    public static final String DAY = "day";
    public static final String MENU_CHECK = "m_check";
    public static final String MAIN_MEAL = "main_meal";


}
