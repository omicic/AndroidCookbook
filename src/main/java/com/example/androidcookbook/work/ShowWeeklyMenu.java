package com.example.androidcookbook.work;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidcookbook.R;
import com.example.androidcookbook.mydb.MenuDB;
import com.example.androidcookbook.mydb.RecipeDB;
import com.example.androidcookbook.object.RecipeMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


public class ShowWeeklyMenu extends AppCompatActivity implements OnCheckedChangeListener {

    private static final int GET_SALAD = 2;
    private static final int GET_DESSERT = 3;
    private static final int GET_MAIN_MEAL = 1;
    private static final int CHANGE_RESULT = 4;
    private MenuDB dbmenu;
    private RecipeDB dbrecipe;
    private HashMap<String, RecipeMenu> hmMonday;
    private HashMap<String, RecipeMenu> hmWednesday;
    private HashMap<String, RecipeMenu> hmTuersday;
    private HashMap<String, RecipeMenu> hmThursday;
    private HashMap<String, RecipeMenu> hmFriday;
    private HashMap<String, RecipeMenu> hmSaturday;
    private HashMap<String, RecipeMenu> hmSunday;
    private LinearLayout linLayout;

    private ArrayList<RecipeMenu> lunches;

    private TextView tvBreakfast;
    private TextView tvLunch;
    private TextView tvDinner;
    private LinearLayout dayLayout;
    private ArrayList<RecipeMenu> alrecipemenu;
    private ShapeDrawable rectShapeDrawable;
    private Typeface tf;
    private Button btBreakfast;
    private View btLunch;
    private Button btDinner;
    private LinearLayout lltvmealbreakfast;
    private LinearLayout lltvmeallunch;
    private LinearLayout lltvmealdinner;
    private AlertDialog dialog;
    protected String choose;
    private ArrayList<String> resultcheckedrecid;
    private ArrayList<Boolean> resultchecks;
    private Intent data;
    private String daymeal;
    private LinearLayout lbreakfast;
    private LinearLayout llunch;
    private LinearLayout ldinner;
    private LinearLayout ltv;
    private LinearLayout llday;
    private LinearLayout linRow;
    private LayoutParams params;
    private TextView tvBreakfastSalad;
    private TextView tvBreakfastDessert;

    private ArrayList<String> alSaladDessertCategoryMon;
    private ArrayList<String> alSaladDessertCategoryTue;
    private ArrayList<String> alSaladDessertCategoryThu;
    private ArrayList<String> alSaladDessertCategoryWen;
    private ArrayList<String> alSaladDessertCategorySat;
    private ArrayList<String> alSaladDessertCategoryFri;
    private ArrayList<String> alSaladDessertCategorySun;
    private TextView tvSalad;
    private ArrayList<String> menu_ids;
    private ArrayList<RecipeMenu> recipemenual;
    private TextView tvDessert;
    private ArrayList<String> fromSetWeeklyMenurec_ids;
    private ArrayList<String> fromSetWeeklyMenudays;
    private boolean[] fromSetWeeklyMenuchecked;
    private CheckBox cb;
    private String menu;
    private RecipeMenu recipeforadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        tf = Typeface.createFromAsset(getAssets(), "fonts/quikhand.ttf");
        tf.isItalic();

        hmMonday = new HashMap<String, RecipeMenu>();
        hmTuersday = new HashMap<String, RecipeMenu>();
        hmWednesday = new HashMap<String, RecipeMenu>();
        hmThursday = new HashMap<String, RecipeMenu>();
        hmFriday = new HashMap<String, RecipeMenu>();
        hmSaturday = new HashMap<String, RecipeMenu>();
        hmSunday = new HashMap<String, RecipeMenu>();

        menu_ids = new ArrayList<String>();
        recipemenual = new ArrayList<RecipeMenu>();
        resultcheckedrecid = new ArrayList<String>();
        fromSetWeeklyMenurec_ids = new ArrayList<String>();
        fromSetWeeklyMenudays = new ArrayList<String>();

        linLayout = new LinearLayout(this);
        linLayout.setBackgroundColor(Color.BLACK);
        LayoutParams linLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        ScrollView scLinLayout = new ScrollView(this);

        alrecipemenu = new ArrayList<RecipeMenu>();

        dbmenu = new MenuDB(this);
        dbrecipe = new RecipeDB(this);
        alrecipemenu = dbmenu.getAllMenu();

        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        CreateHashMap(); //? posebna nit

            createLayout(hmMonday, 0);
            createLayout(hmTuersday, 1);
            createLayout(hmWednesday, 2);
            createLayout(hmThursday, 3);
            createLayout(hmFriday, 4);
            createLayout(hmSaturday, 5);
            createLayout(hmSunday, 6);

        linLayout.setOrientation(LinearLayout.VERTICAL);
        scLinLayout.addView(linLayout);
        setContentView(scLinLayout,linLayoutParam);

    }

    private void createLayout(HashMap<String, RecipeMenu> hm, Integer dayOfweek) {

        linRow = new LinearLayout(this);
        linRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        linRow.setOrientation(LinearLayout.HORIZONTAL);

        llday = new LinearLayout(this);

        ltv = new LinearLayout(this);
        ltv.setLayoutParams(params);
        ltv.setOrientation(LinearLayout.VERTICAL);

        lbreakfast = new LinearLayout(this);
        lbreakfast.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        lbreakfast.setOrientation(LinearLayout.HORIZONTAL);
        lbreakfast.setBackgroundResource(R.drawable.breakfast1);

        llunch = new LinearLayout(this);
        llunch.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        llunch.setOrientation(LinearLayout.HORIZONTAL);
        llunch.setBackgroundResource(R.drawable.lunch1);

        ldinner = new LinearLayout(this);
        ldinner.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        ldinner.setOrientation(LinearLayout.HORIZONTAL);
        ldinner.setBackgroundResource(R.drawable.dinner1);

        ImageView ivday = new ImageView(this);
        switch (dayOfweek) {
            case 0:
                ivday.setBackgroundResource(R.drawable.monday1);
                llday.addView(ivday);
                break;
            case 1:
                ivday.setBackgroundResource(R.drawable.tuesday1);
                llday.addView(ivday);
                break;
            case 2:
                ivday.setBackgroundResource(R.drawable.wednesday1);
                llday.addView(ivday);
                break;
            case 3:
                ivday.setBackgroundResource(R.drawable.thursday1);
                llday.addView(ivday);
                break;
            case 4:
                ivday.setBackgroundResource(R.drawable.friday1);
                llday.addView(ivday);
                break;
            case 5:
                ivday.setBackgroundResource(R.drawable.saturday1);
                llday.addView(ivday);
                break;
            case 6:
                ivday.setBackgroundResource(R.drawable.sunday1);
                llday.addView(ivday);
                break;
        }

        lltvmealbreakfast = new LinearLayout(this);
        lltvmealbreakfast.setOrientation(LinearLayout.VERTICAL);

        lltvmeallunch = new LinearLayout(this);
        lltvmeallunch.setOrientation(LinearLayout.VERTICAL);

        lltvmealdinner = new LinearLayout(this);
        lltvmealdinner.setOrientation(LinearLayout.VERTICAL);

        menu_ids.clear();
        recipemenual.clear();

        citajMapu(hm); //? AsyncTask

        punitv();

        lbreakfast.addView(lltvmealbreakfast);
        lltvmealbreakfast.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f));
        if (menu == "edit") {
            lbreakfast.addView(btBreakfast);
        }

        llunch.addView(lltvmeallunch);
        lltvmeallunch.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f));
        if (menu == "edit") {
            llunch.addView(btLunch);
        }

        ldinner.addView(lltvmealdinner);
        lltvmealdinner.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f));
        if (menu == "edit") {
            ldinner.addView(btDinner);
        }

        ltv.addView(lbreakfast);
        ltv.addView(llunch);
        ltv.addView(ldinner);

        linRow.addView(llday);
        linRow.addView(ltv);

        linLayout.addView(linRow);

    }

    private void citajMapu(HashMap<String, RecipeMenu> hm) {

        Iterator<Entry<String, RecipeMenu>> myVOwnIterator = hm.entrySet().iterator();
        while (myVOwnIterator.hasNext()) {
            Entry<String, RecipeMenu> entry = myVOwnIterator.next();
            menu_ids.add(entry.getKey().toString());
            recipemenual.add(entry.getValue());
            Log.d("recipemenual ------->", entry.getKey());
        }
    }

    private void punitv() {

        lltvmealbreakfast.setPadding(10, 0, 0, 0);
        lltvmeallunch.setPadding(10, 0, 0, 0);
        lltvmealdinner.setPadding(10, 0, 0, 0);


        for (int j = 0; j < recipemenual.size(); j++) {

            TextView tv = new TextView(this);
            tv.setTextColor(Color.parseColor("#2E2EB8"));
            tv.setTypeface(tf);
            tv.setTextSize(16);
            tv.setPadding(20, 20, 0, 0);
            tv.setText(dbrecipe.getRecepteById(recipemenual.get(j).getRec_id()).getRecipe().toString());

            if ((dbrecipe.getRecepteById(recipemenual.get(j).getRec_id()).getCategory().equals("Salad")) ||
                    (dbrecipe.getRecepteById(recipemenual.get(j).getRec_id()).getCategory().equals("Dessert")) ||
                    (dbrecipe.getRecepteById(recipemenual.get(j).getRec_id()).getCategory().equals("Starter")) ) {

                if (recipemenual.get(j).getMainmeal().equals("Breakfast")) {
                    lltvmealbreakfast.addView(tv);
                }
                if (recipemenual.get(j).getMainmeal().equals("Lunch")) {
                    lltvmeallunch.addView(tv);
                }
                if (recipemenual.get(j).getMainmeal().equals("Dinner")) {
                    lltvmealdinner.addView(tv);
                }
            }

            if (dbrecipe.getRecepteById(recipemenual.get(j).getRec_id()).getCategory().equals("Breakfast")) {
                lltvmealbreakfast.addView(tv);
            }
            if (dbrecipe.getRecepteById(recipemenual.get(j).getRec_id()).getCategory().equals("Lunch")) {
                lltvmeallunch.addView(tv);
            }
            if (dbrecipe.getRecepteById(recipemenual.get(j).getRec_id()).getCategory().equals("Dinner")) {
                lltvmealdinner.addView(tv);
            }
        }

        menu_ids.clear();
        recipemenual.clear();
    }

    private void CreateHashMap() { //poziva se pri oncreate

        hmMonday.clear();
        hmTuersday.clear();
        hmWednesday.clear();
        hmThursday.clear();
        hmFriday.clear();
        hmSaturday.clear();
        hmSunday.clear();

        for (int i = 0; i < alrecipemenu.size(); i++) {
            //Log.d("alrecipemenu.get(i).getMenu_id()",alrecipemenu.get(i).getCheck());
            if (Boolean.parseBoolean(alrecipemenu.get(i).getCheck())) {
                if (alrecipemenu.get(i).getDay().equals("0")) { //ako je ponedeljak
                    hmMonday.put(alrecipemenu.get(i).getMenu_id(), alrecipemenu.get(i));
                }
                if (alrecipemenu.get(i).getDay().equals("1")) {
                    hmTuersday.put(alrecipemenu.get(i).getMenu_id(), alrecipemenu.get(i));
                }
                if (alrecipemenu.get(i).getDay().equals("2")) {
                    hmWednesday.put(alrecipemenu.get(i).getMenu_id(), alrecipemenu.get(i));
                }
                if (alrecipemenu.get(i).getDay().equals("3")) {
                    hmThursday.put(alrecipemenu.get(i).getMenu_id(), alrecipemenu.get(i));
                }
                if (alrecipemenu.get(i).getDay().equals("4")) {
                    hmFriday.put(alrecipemenu.get(i).getMenu_id(), alrecipemenu.get(i));
                }
                if (alrecipemenu.get(i).getDay().equals("5")) {
                    hmSaturday.put(alrecipemenu.get(i).getMenu_id(), alrecipemenu.get(i));
                }
                if (alrecipemenu.get(i).getDay().equals("6")) {
                    hmSunday.put(alrecipemenu.get(i).getMenu_id(), alrecipemenu.get(i));
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        RecipeMenu rm = dbmenu.getMenuByMenuId(buttonView.getTag().toString());
        if (isChecked) {
            rm.setCheck("true");
        } else {
            rm.setCheck("false");
        }


        if (rm.getDay().equals("0")) {
            getMainMealFromAlreadySet(hmMonday, rm);            //put mainmeal from recipeforadd into rm
            hmMonday.put(rm.getMenu_id(), rm);
        }
        if (rm.getDay().equals("1")) {
            getMainMealFromAlreadySet(hmTuersday, rm);            //put mainmeal from recipeforadd into rm
            hmTuersday.put(rm.getMenu_id(), rm);
        }
        if (rm.getDay().equals("2")) {
            getMainMealFromAlreadySet(hmWednesday, rm);
            hmWednesday.put(rm.getMenu_id(), rm);
        }
        if (rm.getDay().equals("3")) {
            getMainMealFromAlreadySet(hmThursday, rm);
            hmThursday.put(rm.getMenu_id(), rm);
        }
        if (rm.getDay().equals("4")) {
            getMainMealFromAlreadySet(hmFriday, rm);
            hmFriday.put(rm.getMenu_id(), rm);
        }
        if (rm.getDay().equals("5")) {
            getMainMealFromAlreadySet(hmSaturday, rm);
            hmSaturday.put(rm.getMenu_id(), rm);
        }
        if (rm.getDay().equals("6")) {
            getMainMealFromAlreadySet(hmSunday, rm);
            hmSunday.put(rm.getMenu_id(), rm);
        }
    }

    private void getMainMealFromAlreadySet(HashMap<String, RecipeMenu> hm, RecipeMenu rm) {

        menu_ids.clear();
        recipemenual.clear();
        citajMapu(hm);
        for (int j = 0; j < menu_ids.size(); j++) {
            if (menu_ids.get(j).equals(rm.getMenu_id()) && recipemenual.get(j).getMainmeal() != null) {
                rm.setMainmeal(recipemenual.get(j).getMainmeal());
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.weekly_menu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                Intent changeWeeklymenuIntent = new Intent(this, ChangeWeeklyMenu.class);
                startActivity(changeWeeklymenuIntent);
                finish();
                return true;
            case R.id.menu_create_shopping_list:
                Intent makeNoteWeeklyIntent = new Intent(this, MakeNoteForAll.class);
                startActivity(makeNoteWeeklyIntent);
                finish();
                return true;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}

