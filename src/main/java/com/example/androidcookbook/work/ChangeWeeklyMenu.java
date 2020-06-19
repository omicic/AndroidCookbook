package com.example.androidcookbook.work;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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

import com.example.androidcookbook.MainActivity;
import com.example.androidcookbook.R;
import com.example.androidcookbook.mydb.MenuDB;
import com.example.androidcookbook.mydb.RecipeDB;
import com.example.androidcookbook.object.RecipeMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


public class ChangeWeeklyMenu extends AppCompatActivity implements OnClickListener, OnCheckedChangeListener {

    private static final int GET_STARTER = 1;
    private static final int GET_SALAD = 2;
    private static final int GET_DESSERT = 3;

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

    private ArrayList<RecipeMenu> alrecipemenu;
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

    private ArrayList<String> menu_ids;
    private ArrayList<RecipeMenu> recipemenual;

    private CheckBox cb;
    private String menu;
    private RecipeMenu recipeforadd;
    private ArrayList<String> fromSetWeeklyMenurec_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
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
        //fromSetWeeklyMenudays = new ArrayList<String>();

        linLayout = new LinearLayout(this);
        linLayout.setBackgroundColor(Color.BLACK);
        LayoutParams linLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        ScrollView scLinLayout = new ScrollView(this);

        alrecipemenu = new ArrayList<RecipeMenu>();

        dbmenu = new MenuDB(this);
        dbrecipe = new RecipeDB(this);
        alrecipemenu = dbmenu.getAllMenu();

        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        CreateHashMap();

        createLayout(hmMonday, 0);
        createLayout(hmTuersday, 1);
        createLayout(hmWednesday, 2);
        createLayout(hmThursday, 3);
        createLayout(hmFriday, 4);
        createLayout(hmSaturday, 5);
        createLayout(hmSunday, 6);

        linLayout.setOrientation(LinearLayout.VERTICAL);
        scLinLayout.addView(linLayout);
        setContentView(scLinLayout, linLayoutParam);
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

        btBreakfast = new Button(this);
        btBreakfast.setBackgroundResource(R.drawable.plusicon);
        btBreakfast.setOnClickListener(this);

        btLunch = new Button(this);
        btLunch.setBackgroundResource(R.drawable.plusicon);
        btLunch.setOnClickListener(this);

        btDinner = new Button(this);
        btDinner.setBackgroundResource(R.drawable.plusicon);
        btDinner.setOnClickListener(this);

        switch (dayOfweek) {
            case 0:
                btBreakfast.setTag("MON,B");
                btLunch.setTag("MON,L");
                btDinner.setTag("MON,D");
                break;
            case 1:
                btBreakfast.setTag("TUE,B");
                btLunch.setTag("TUE,L");
                btDinner.setTag("TUE,D");
                break;
            case 2:
                btBreakfast.setTag("WED,B");
                btLunch.setTag("WED,L");
                btDinner.setTag("WED,D");
                break;
            case 3:
                btBreakfast.setTag("THU,B");
                btLunch.setTag("THU,L");
                btDinner.setTag("THU,D");
                break;
            case 4:
                btBreakfast.setTag("FRI,B");
                btLunch.setTag("FRI,L");
                btDinner.setTag("FRI,D");
                break;
            case 5:
                btBreakfast.setTag("SAT,B");
                btLunch.setTag("SAT,L");
                btDinner.setTag("SAT,D");
                break;
            case 6:
                btBreakfast.setTag("SUN,B");
                btLunch.setTag("SUN,L");
                btDinner.setTag("SUN,D");
                break;
            default:
                break;
        }

        menu_ids.clear();
        recipemenual.clear();
        citajMapu(hm);
        punicb();

        LinearLayout.LayoutParams paramss = new LinearLayout.LayoutParams(50, 50);
        paramss.leftMargin = 8;
        paramss.gravity = Gravity.RIGHT;

        lbreakfast.addView(lltvmealbreakfast);
        lltvmealbreakfast.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f));
        btBreakfast.setLayoutParams(paramss);
        lbreakfast.addView(btBreakfast);

        llunch.addView(lltvmeallunch);
        lltvmeallunch.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f));
        btLunch.setLayoutParams(paramss);
        llunch.addView(btLunch);

        ldinner.addView(lltvmealdinner);
        lltvmealdinner.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f));
        btDinner.setLayoutParams(paramss);
        ldinner.addView(btDinner);


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
        }
    }

    private void punicb() {

        lltvmealbreakfast.setPadding(10, 0, 0, 0);
        lltvmeallunch.setPadding(10, 0, 0, 0);
        lltvmealdinner.setPadding(10, 0, 0, 0);

        for (int j = 0; j < recipemenual.size(); j++) {
            cb = new CheckBox(this);
            cb.setTextSize(14);
            cb.setTextColor(Color.rgb(50,120,160));
            cb.setTag(recipemenual.get(j).getMenu_id().toString());
            cb.setOnCheckedChangeListener(this);
            cb.setChecked(true);
            cb.setText(dbrecipe.getRecepteById(recipemenual.get(j).getRec_id()).getRecipe());

            addViewTolltvmeal(dbrecipe.getRecepteById(recipemenual.get(j).getRec_id()).getRecipe(),
                    dbrecipe.getRecepteById(recipemenual.get(j).getRec_id()).getCategory(),
                    recipemenual.get(j).getMainmeal(), recipemenual.get(j).getMenu_id());
        }

        menu_ids.clear();
        recipemenual.clear();

    }

    private void addViewTolltvmeal(String recipe, String category, String mainmeal, String menu_id) {

        if (category.equals("Salad") || category.equals("Dessert") || category.equals("Starter")) {

            if (mainmeal.equals("Breakfast")) {
                lltvmealbreakfast.addView(cb);
            }

            if (mainmeal.equals("Lunch")) {
                lltvmeallunch.addView(cb);
            }

            if (mainmeal.equals("Dinner")) {
                lltvmealdinner.addView(cb);
            }
        } else {

            if (category.equals("Breakfast")) {
                lltvmealbreakfast.addView(cb);
            }
            if (category.equals("Lunch")) {
                lltvmeallunch.addView(cb);
            }
            if (category.equals("Dinner")) {
                lltvmealdinner.addView(cb);
            }
        }
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
    public void onClick(View v) {
        //kada klikne dugme treba da se pojavi meni u kome ce korisnik odabrati koju vrstu jela dodaje: Starter, Salatu, Dezert
        //treba dodati u hashmapu odredjenog dana jelo u kombinaciji recids,meal
        daymeal = v.getTag().toString();
        onCreateDialog(daymeal);
    }

    public Dialog onCreateDialog(String daymeal) {

        final String category = daymeal;//where put in linearlayout

        Builder alertDialogBuilder = new Builder(ChangeWeeklyMenu.this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("Add...")
                .setItems(R.array.meal_array, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent addStarter = new Intent( getBaseContext(), ShowListOfRecipe.class);
                            addStarter.putExtra("addingmeal", "Starter");
                            addStarter.putExtra("category", "Starter");
                            startActivityForResult(addStarter, GET_STARTER);
                        }
                        if (which == 1) {
                            Intent addSalad = new Intent(getBaseContext(), ShowListOfRecipe.class);
                            addSalad.putExtra("addingmeal", "Salad");
                            addSalad.putExtra("category", "Salad");
                            startActivityForResult(addSalad, GET_SALAD);
                        }
                        if (which == 2) {
                            Intent addDessert = new Intent(getBaseContext(), ShowListOfRecipe.class);
                            addDessert.putExtra("addingmeal", "Dessert");
                            startActivityForResult(addDessert, GET_DESSERT);
                        }
                    }
                });

        dialog = alertDialogBuilder.create();
        dialog.show();
        return dialog;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        this.data = data;
        if (requestCode == GET_STARTER) {//1
            if (resultCode == RESULT_OK) {
                resultcheckedrecid = data.getExtras().getStringArrayList("add_to_menu_cbtag");//from ShowListOfRecipe.class
                resultchecks = (ArrayList<Boolean>) data.getSerializableExtra("add_to_menu_checks");//from ShowListOfRecipe.class
                //prosledjivanje ShoeWeeklyMenu.class istih
                AddToLayout(resultcheckedrecid, resultchecks);

                if (resultCode == RESULT_CANCELED) {
                    finish();
                }
            }
        }

        if (requestCode == GET_SALAD) {//2
            if (resultCode == RESULT_OK) {
                resultcheckedrecid = data.getExtras().getStringArrayList("add_to_menu_cbtag");//from ShowListOfRecipe.class
                resultchecks = (ArrayList<Boolean>) data.getSerializableExtra("add_to_menu_checks");//from ShowListOfRecipe.class
                //prosledjivanje ShoeWeeklyMenu.class istih
                AddToLayout(resultcheckedrecid, resultchecks);

                if (resultCode == RESULT_CANCELED) {
                    finish();
                }
            }
        }

        if (requestCode == GET_DESSERT) {//3
            if (resultCode == RESULT_OK) {
                resultcheckedrecid = data.getExtras().getStringArrayList("add_to_menu_cbtag");//from ShowListOfRecipe.class
                resultchecks = (ArrayList<Boolean>) data.getSerializableExtra("add_to_menu_checks");//from ShowListOfRecipe.class
                //prosledjivanje ShoeWeeklyMenu.class istih
                AddToLayout(resultcheckedrecid, resultchecks);

                if (resultCode == RESULT_CANCELED) {
                    finish();
                }
            }
        }
    }

    private void AddToLayout(ArrayList<String> resultcheckedrecid,
                             ArrayList<Boolean> resultchecks) {

        for (int i = 0; i < resultcheckedrecid.size(); i++) {

            if (resultchecks.get(i) == true) {

                switch (daymeal) {
                    case "MON,B":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "0", "Breakfast");
                        return;
                    case "MON,L":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "0", "Lunch");
                        return;
                    case "MON,D":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "0", "Dinner");
                        return;
                    case "TUE,B":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "1", "Breakfast");
                        return;
                    case "TUE,L":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "1", "Lunch");
                        return;
                    case "TUE,D":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "1", "Dinner");
                        return;

                    case "WED,B":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "2", "Breakfast");
                        return;
                    case "WED,L":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "2", "Lunch");
                        return;
                    case "WED,D":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "2", "Dinner");
                        return;

                    case "THU,B":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "3", "Breakfast");
                        return;
                    case "THU,L":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "3", "Lunch");
                        return;
                    case "THU,D":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "3", "Dinner");
                        return;

                    case "FRI,B":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "4", "Breakfast");
                        return;
                    case "FRI,L":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "4", "Lunch");
                        return;
                    case "FRI,D":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "4", "Dinner");
                        return;

                    case "SAT,B":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "5", "Breakfast");
                        return;
                    case "SAT,L":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "5", "Lunch");
                        return;
                    case "SAT,D":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "5", "Dinner");
                        return;

                    case "SUN,B":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "6", "Breakfast");
                        return;
                    case "SUN,L":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "6", "Lunch");
                        return;
                    case "SUN,D":
                        AddHashMap(resultcheckedrecid.get(i).toString(), "6", "Dinner");
                        return;

                }
            }
        }
    }

    private void AddHashMap(String rec_id, String day, String meal) { //meal B,L,D
        //int index = alrecipemenu.size();
        dbmenu.getDb().open();
        recipeforadd = new RecipeMenu();
        recipeforadd = dbmenu.getMenuByRecId(rec_id, day); //jos uvek nema uneto u bazi meal jer nije izvrseno cuvanje
        recipeforadd.setMainmeal(meal);

        //treba dodati hm-i
        if (day == "0") {
            hmMonday.put(dbmenu.getMenuByRecId(rec_id, day).getMenu_id(), recipeforadd); //ne moze jedan recept da bude dva puta u hashmap-i
        }
        if (day == "1") {
            hmTuersday.put(dbmenu.getMenuByRecId(rec_id, day).getMenu_id(), recipeforadd);
        }
        if (day == "2") {
            hmWednesday.put(dbmenu.getMenuByRecId(rec_id, day).getMenu_id(), recipeforadd);
        }
        if (day == "3") {
            hmThursday.put(dbmenu.getMenuByRecId(rec_id, day).getMenu_id(), recipeforadd);
        }
        if (day == "4") {
            hmFriday.put(dbmenu.getMenuByRecId(rec_id, day).getMenu_id(), recipeforadd);
        }
        if (day == "5") {
            hmSaturday.put(dbmenu.getMenuByRecId(rec_id, day).getMenu_id(), recipeforadd);
        }
        if (day == "6") {
            hmSunday.put(dbmenu.getMenuByRecId(rec_id, day).getMenu_id(), recipeforadd);
        }

        linLayout.removeAllViews();
        createLayout(hmMonday, 0);
        createLayout(hmTuersday, 1);
        createLayout(hmWednesday, 2);
        createLayout(hmThursday, 3);
        createLayout(hmFriday, 4);
        createLayout(hmSaturday, 5);
        createLayout(hmSunday, 6);
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
        getMenuInflater().inflate(R.menu.edit_save_weekly_menu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_save:

                updatedbMenu(hmMonday);
                updatedbMenu(hmTuersday);
                updatedbMenu(hmWednesday);
                updatedbMenu(hmThursday);
                updatedbMenu(hmFriday);
                updatedbMenu(hmSaturday);
                updatedbMenu(hmSunday);

                Intent i = new Intent(this, ShowWeeklyMenu.class);
                startActivity(i);
                finish();
                return true;

            case R.id.menu_set_main_meal:

                updatedbMenu(hmMonday);
                updatedbMenu(hmTuersday);
                updatedbMenu(hmWednesday);
                updatedbMenu(hmThursday);
                updatedbMenu(hmFriday);
                updatedbMenu(hmSaturday);
                updatedbMenu(hmSunday);

                Intent setMainMealIntent = new Intent(this, SetWeeklyMenu.class);
                startActivity(setMainMealIntent);
                finish();
                return true;

        }
        return true;
    }

    private void updatedbMenu(HashMap<String, RecipeMenu> hm) {

        recipemenual.clear();
        menu_ids.clear();
        citajMapu(hm);

        for (int i = 0; i < menu_ids.size(); i++) {
            dbmenu.updateMenu(recipemenual.get(i).getRec_id(), recipemenual.get(i).getDay(), recipemenual.get(i).getCheck(), recipemenual.get(i).getMainmeal());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

