package com.example.androidcookbook;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidcookbook.mydb.IngredientsDB;
import com.example.androidcookbook.mydb.RecipeDB;
import com.example.androidcookbook.mydb.RecipeIngredientsDB;
import com.example.androidcookbook.object.Ingredient;
import com.example.androidcookbook.object.Recipe;
import com.example.androidcookbook.object.RecipePrepare;

import java.io.File;
import java.util.ArrayList;

public class ShowNote extends Activity implements OnClickListener {

    private IngredientsDB ingdb;
    private RecipeIngredientsDB recing;
    private RecipeDB recdb;
    private ArrayList<RecipePrepare> recings;
    private boolean postoji;
    private String ingtofind;
    private String qutofind;
    private int i;
    private ArrayList<String> ingredientstobuy;
    private ArrayList<String> quantitiestobuy;
    private Recipe rec;
    private SharedPreferences preferences;
    private Editor editor;
    private int m;
    private Ingredient ing;
    private ArrayList<String> arrayForSharedPrefIng;
    private ArrayList<String> arrayForSharedPrefQu;
    private ArrayList<String> arrayForSharedPrefMu;
    private Typeface tf;
    private LinearLayout llnote;
    private Button ibok;
    private int padding;
    private int size;
    private String recid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.row_ingredients);

        tf = Typeface.createFromAsset(getAssets(), "fonts/quikhand.ttf");
        llnote = (LinearLayout) this.findViewById(R.id.llmakenote);
        ScrollView scrollview = (ScrollView) findViewById(R.id.scviewmakenote);

        RecipeDB recdb = new RecipeDB(this);
        recdb.getDb().open();
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        recipes = (ArrayList<Recipe>) recdb.getAllRecepte();

        for (int i = 0; i < recipes.size(); i++) {
            if (recipes.get(i).getId().equals(getIntent().getStringExtra("recidsticky"))) {
                recid = recipes.get(i).getId().toString();
                preferences = this.getSharedPreferences("Note" + recid, this.MODE_PRIVATE);
                size = preferences.getInt("size", -1); //ako postoji uzme size, a ako ne -1
            }
        }

        if (size != -1) {
            ShowNote();
        } else {
            Toast.makeText(this, "There's no ingredients to show.", Toast.LENGTH_LONG).show();
            finish();
        }
    }


    private void ShowNote() {

        llnote.removeAllViews();
        LayoutParams llparam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        LayoutParams lpib = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lpib.gravity = Gravity.RIGHT;

        llnote.setVisibility(View.VISIBLE);
        llnote.setBackgroundResource(R.drawable.sticky);

        llparam.setMargins(10, 5, 0, 5);

        TextView tvRecipe = new TextView(this);
        ImageButton ibCloseAndDeletePref = (ImageButton) findViewById(R.id.ibdelete);

        //ibCloseAndDeletePref.setId(-1);
        ibCloseAndDeletePref.setImageResource(R.drawable.deleteicon);
        ibCloseAndDeletePref.setTag(recid);

        //ibCloseAndDeletePref.setBackgroundColor(Color.TRANSPARENT);
        //llnote.addView(ibCloseAndDeletePref, lpib);
        ibCloseAndDeletePref.setOnClickListener(this);

        tvRecipe.setTypeface(tf);
        tvRecipe.setPadding(60, 10, 0, 0);

        tvRecipe.setTextSize(20);
        tvRecipe.setTextColor(Color.parseColor("#2E2EB8"));

        tvRecipe.setText(preferences.getString("Recipe", ""));
        llnote.addView(tvRecipe);

        padding = 50;

        for (int i = 1; i <= size; i++) {  //prikazuje samo one koji nisu prazni tj. ne nedostaju; TREBA NAPRAVITI U MAKENOTE.CLASS DA SE I NE UPISUJU U PREFERENCES!!!

            TextView tvingnote = new TextView(this);
            tvingnote.setTextSize(20);
            tvingnote.setId(i);
            //tvingnote.setTextColor(color.blue);
            tvingnote.setTypeface(tf);
            tvingnote.setTextColor(Color.parseColor("#2E2EB8"));
            tvingnote.setPadding(padding, 10, 0, 0);

            tvingnote.setText("- " + preferences.getString("Ingredient" + i, "") + ", " +
                    preferences.getString("Qu" + i, "") + " " +
                    preferences.getString("Mu" + i, ""));

            llnote.addView(tvingnote, llparam);

        }
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.ibdelete) {

            preferences = this.getSharedPreferences("Note" + v.getTag().toString(), this.MODE_PRIVATE);
            preferences.edit().clear().commit();

            Log.d("tag", v.getTag().toString());
            File file = new File("/data/data/com.cook.androidcookery/shared_prefs/Note" + v.getTag().toString() + ".xml");
            if (file.exists()) {
                Log.d("brise", "brise");
                file.delete();

            }


            finish();
        }


    }
}
