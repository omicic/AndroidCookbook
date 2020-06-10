package com.example.androidcookbook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidcookbook.mydb.IngredientsDB;
import com.example.androidcookbook.mydb.MenuDB;
import com.example.androidcookbook.mydb.RecipeDB;
import com.example.androidcookbook.object.Ingredient;
import com.example.androidcookbook.object.Recipe;
import com.example.androidcookbook.object.RecipeMenu;
import com.example.androidcookbook.work.AddIngredient;
import com.example.androidcookbook.work.AddRecipe;
import com.example.androidcookbook.work.MySQLPullParserIngredient;
import com.example.androidcookbook.work.ShowIngredients;
import com.example.androidcookbook.work.ShowListOfRecipe;
import com.example.androidcookbook.work.ShowWeeklyMenu;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int TIME_ENTRY_REQUEST_CODE = 1;
    private SharedPreferences preferences;
    private LinearLayout llnote;
    private Typeface tf;
    private int padding;
    private ImageButton ibFindRecipes;
    private IngredientsDB ingredientDB;
    private ImageButton ibSticky;
    private RecipeDB recipeDB;
    private LinearLayout llmenu;

    private IngredientsDB ingdb;
    private InputStream is;
    private Intent starterIntent;
    private MenuDB menudb;
    private ArrayList<RecipeMenu> menu;
    private AlertDialog dialog;
    private LinearLayout llimagebutton;
    private LinearLayout llstickybuttons;
    private AsyncTask<ArrayList<Recipe>, Void, ArrayList<String>> fs;
    private ArrayList<Recipe> recipes;
    private ArrayList<String> alfs;

    private int size;
   //private int sizee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        starterIntent = getIntent();

        tf = Typeface.createFromAsset(getAssets(), "fonts/quikhand.ttf");

        llmenu = (LinearLayout) findViewById(R.id.llmenu);
        llimagebutton = (LinearLayout) findViewById(R.id.llimagebuttons);
        llstickybuttons = (LinearLayout) findViewById(R.id.llstickybuttons);
        ibFindRecipes = (ImageButton) findViewById(R.id.ibFindRecipes);
        ibFindRecipes.setBackgroundResource(R.drawable.selectorfindicon);

        recipeDB = new RecipeDB(this);
        recipeDB.getDb().open();
        recipes = new ArrayList<Recipe>();
        recipes = recipeDB.getAllRecepte();
        alfs = new ArrayList<String>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            } else {
                // continue with your code
                try {
                    checkSharedPrefState();
                } catch (InterruptedException | ExecutionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }  //ako je postavljen sticky na naslovnom ekranu, namirnice koje treba kupiti za odabrani recept

                takemanuForDay();

                ingdb = new IngredientsDB(this);
                ingdb.getDb().open();
                if (ingdb.getAllIngredients().size() == 0) {
                    getIngredeitnsFromAsset();
                }

                ibFindRecipes.setOnClickListener(this);
            }
        } else {
            // continue with your code
            try {
                checkSharedPrefState();
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }  //ako je postavljen sticky na naslovnom ekranu, namirnice koje treba kupiti za odabrani recept

            takemanuForDay();

            ingdb = new IngredientsDB(this);
            ingdb.getDb().open();
            if (ingdb.getAllIngredients().size() == 0) {
                getIngredeitnsFromAsset();
            }
            ibFindRecipes.setOnClickListener(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                } else {
                    Log.e("Permission", "Denied");
                }
                return;
            }
        }
    }

    private void takemanuForDay() {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        menudb = new MenuDB(this);

        llmenu.removeAllViews();

        menudb.getDb().open();
        menu = new ArrayList<RecipeMenu>();
        menu = menudb.getAllMenu();
        menudb.getDb().close();
        switch (day) {

            case Calendar.SUNDAY:
                makeTodayMenu("6");
                return;
            case Calendar.MONDAY:
                makeTodayMenu("0");
                return;
            case Calendar.TUESDAY:
                makeTodayMenu("1");
                return;
            case Calendar.WEDNESDAY:
                makeTodayMenu("2");
                return;
            case Calendar.THURSDAY:
                makeTodayMenu("3");
                return;
            case Calendar.FRIDAY:
                makeTodayMenu("4");
                return;
            case Calendar.SATURDAY:
                makeTodayMenu("5");
                return;
        }
    }

    private void makeTodayMenu(String string) {
        recipeDB = new RecipeDB(this);
        recipeDB.getDb().open();
        LinearLayout ll = new LinearLayout(this);
        for (int i = 0; i < menu.size(); i++) {

            if (menu.get(i).getDay().toString().equals(string) && menu.get(i).getCheck().equals("true")) {

                TextView tv = new TextView(this);
                tv.setTypeface(tf);
                tv.setTextColor(Color.parseColor("#611815"));
                tv.setTextSize(18);
                tv.setTag(menu.get(i).getMenu_id());

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                tv.setGravity(Gravity.CENTER);
                tv.setPadding(0, 10, 0, 0);
                Recipe recipe = new Recipe();
                recipe = recipeDB.getRecepteById(menu.get(i).getRec_id());

                tv.setText(recipe.getCategory() + ":   " + recipe.getRecipe());
                llmenu.addView(tv, lp);
            }
        }
        recipeDB.getDb().close();
    }

    private void getIngredeitnsFromAsset() {

        android.content.res.AssetManager assetManager = getBaseContext().getAssets();
        try {

            if ("en".equals(getString(R.string.lang))) {
                is = assetManager.open("Ingredients.xml");
            } else {
                is = assetManager.open("Namirnice.xml");
            }

            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();

            MySQLPullParserIngredient myXMLHandler = new MySQLPullParserIngredient();
            xr.setContentHandler(myXMLHandler);
            InputSource inStream = new InputSource(is);
            xr.parse(inStream);

            String ingname = myXMLHandler.getIngredient().getIngreident();
            String mu = myXMLHandler.getIngredient().getmMu();

            ArrayList<Ingredient> ingList = myXMLHandler.getIngList();
            for (int i = 0; i < ingList.size(); i++) {
                ingdb.insertIngredient(ingList.get(i).getIngreident(), ingList.get(i).getmMu(), null);
            }

            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ResourceType")
    private void checkSharedPrefState() throws InterruptedException, ExecutionException {

        fs = new FindSticky().execute(recipes);
        alfs = fs.get();

        if (!alfs.isEmpty()) {
            for (int i = 0; i < alfs.size(); i++) {
                ImageButton ibSticky = new ImageButton(getApplicationContext());
                ibSticky.setBackgroundResource(R.drawable.stickyexist);
                ibSticky.setId(3); // ovo treba resiti
                ibSticky.setTag(alfs.get(i).toString());
                ibSticky.setOnClickListener(this);
                llstickybuttons.addView(ibSticky);
            }
        }

        SharedPreferences preff = getSharedPreferences("WeeklyShoppingList", this.MODE_PRIVATE);
        String ing1 = preff.getString("Ingredient1", "noone"); //ako postoji uzme size, a ako ne -1
        //Log.d("ing1", Integer.toString(sizee));
        if (!ing1.equals("noone")) {
            //Log.d("doda","doad stickyall");
            ImageButton iballmenu = new ImageButton(this);
            iballmenu.setId(5);
            iballmenu.setOnClickListener(this);
            iballmenu.setBackgroundResource(R.drawable.stickyweeklylist);
            llstickybuttons.addView(iballmenu);
        }
    }

    private class FindSticky extends AsyncTask<ArrayList<Recipe>, Void, ArrayList<String>> {

        private ArrayList<Recipe> recarray;
        private ArrayList<String> recidforshowingsticky;
        private SharedPreferences pref;

        @Override
        protected ArrayList<String> doInBackground(ArrayList<Recipe>... recipes) {

            recarray = recipes[0];
            recidforshowingsticky = new ArrayList<String>();
            recidforshowingsticky.clear();

            int i = 0;
            for (int j = 0; j < recarray.size(); j++) {
                //Log.d("a ovo","");
                pref = getSharedPreferences("Note" + recarray.get(j).getId().toString(), getApplicationContext().MODE_PRIVATE);
                size = pref.getInt("size", -1); //ako postoji uzme size, a ako ne -1

                if (size != -1) {
                    i++;
                    recidforshowingsticky.add(recarray.get(j).getId().toString());
                }
            }
            return recidforshowingsticky;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);


        //DOESN'T WORK**********************************************************************

        //for hide or show item in menu list
      /*  ingredientDB = new IngredientsDB(this);


       /* if (ingredientDB.getAllIngredients().size() == 0) {
            menu.findItem(R.id.menu_show_ingredients).setEnabled(false);
        } else {
            menu.findItem(R.id.menu_show_ingredients).setEnabled(true);
        }
        ingredientDB.getDb().close();

        recipeDB = new RecipeDB(this);
        if (recipeDB.getAllRecepte().size() == 0) {
            menu.findItem(R.id.menu_show_recipe).setEnabled(false);
        } else {
            menu.findItem(R.id.menu_show_recipe).setEnabled(true);
        }
        recipeDB.getDb().close();*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_create_new:

                Intent namera = new Intent(this, AddIngredient.class);
                namera.putExtra("newIng", "newIng");
                startActivity(namera);
                return true;

            case R.id.menu_show_ingredients:

                Intent ingIntent = new Intent(this, ShowIngredients.class);
                startActivity(ingIntent);
                return true;

            case R.id.create_new_recipe:
                Intent addintent = new Intent(this, AddRecipe.class);
                addintent.putExtra("add_new_recipe", "addRecipe"); //use in AddRecipe, if create new recipe
                startActivity(addintent);
                return true;

            case R.id.menu_show_recipe:
                Intent i = new Intent(this, ShowListOfRecipe.class);
                startActivity(i);
                return true;

            case R.id.menu_show_menu:
                Intent showMenuIntent = new Intent(this, ShowWeeklyMenu.class);
                startActivity(showMenuIntent);
                return true;

            case R.id.createbackup:
                try {
                    exportrestoreDB("/data/data/com.cook.androidcookery/databases/cookeryDB", Environment.getExternalStorageDirectory() + "/database_copy_cookeryDB");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return true;

            case R.id.restore:
                try {
                    exportrestoreDB(Environment.getExternalStorageDirectory() + "/database_copy_cookeryDB", "/data/data/com.cook.androidcookery/databases/cookeryDB");
                    finish();
                    startActivity(starterIntent);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return true;

        }
        return true;
    }

    private void exportrestoreDB(String x, String y) throws IOException {

        final String inFileName = x;
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);

        String outFileName = y;

        // Open the empty db as the output stream
        OutputStream output = new FileOutputStream(outFileName);

        // Transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        // Close the streams
        output.flush();
        output.close();
        fis.close();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSearchRequested() {
        return super.onSearchRequested();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.ibFindRecipes) {
            onCreateDialog();
        }

        if (v.getId() == 3) { //dugme stikija
            Intent intentShowNote = new Intent(this, ShowNote.class);
            intentShowNote.putExtra("recidsticky", v.getTag().toString());
            startActivity(intentShowNote);
        }

        if (v.getId() == 5) {
            Intent intentShowNoteForAll = new Intent(this, ShowNoteForAll.class);
            //intentShowNoteForAll.putExtra("recidsticky", v.getTag().toString());
            startActivity(intentShowNoteForAll);
        }
    }

    public Dialog onCreateDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("Search by...")
                .setItems(R.array.find_array, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent intentFindRecipes = new Intent(MainActivity.this, FindRecipes.class);
                            startActivity(intentFindRecipes);
                        }
                        if (which == 1) {
                            Intent intentFindRecipesbyIngs = new Intent(MainActivity.this, ShowListOfRecipe.class);
                            startActivity(intentFindRecipesbyIngs);
                        }
                    }
                });

        dialog = alertDialogBuilder.create();
        dialog.show();
        return dialog;

    }

    @Override
    protected void onResume() {
        super.onResume();
       // Log.d("onResume after showNote", "aaa");
        try {

            llstickybuttons.removeAllViews();

            if (!alfs.isEmpty()) {
                alfs.clear();
            }

            checkSharedPrefState();
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  //ako je postavljen sticky na naslovnom ekranu, namirnice koje treba kupiti za odabrani recept

        takemanuForDay();
        invalidateOptionsMenu();
    }

}


