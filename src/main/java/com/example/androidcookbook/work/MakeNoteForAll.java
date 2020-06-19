package com.example.androidcookbook.work;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidcookbook.MainActivity;
import com.example.androidcookbook.R;
import com.example.androidcookbook.mydb.IngredientsDB;
import com.example.androidcookbook.mydb.MenuDB;
import com.example.androidcookbook.mydb.RecipeDB;
import com.example.androidcookbook.mydb.RecipeIngredientsDB;
import com.example.androidcookbook.object.Ingredient;
import com.example.androidcookbook.object.Recipe;
import com.example.androidcookbook.object.RecipeMenu;
import com.example.androidcookbook.object.RecipePrepare;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MakeNoteForAll extends AppCompatActivity implements OnClickListener {

    private IngredientsDB ingdb;
    private RecipeIngredientsDB recing;
    private RecipeDB recdb;
    private ArrayList<RecipePrepare> recings;
    private boolean postoji;
    private String ingtofind;
    private String qutofind;
    private int i;
    private ArrayList<String> arrayForSharedPrefIng;
    private ArrayList<String> arrayForSharedPrefQu;
    private ArrayList<String> arrayForSharedPrefMu;
    private ArrayList<String> recipesforbuy;
    private ArrayList<String> quantitiestobuy;
    private Recipe rec;
    private SharedPreferences preferences;
    private Editor editor;
    private int m;
    private Ingredient ing;

    private Typeface tf;
    private LinearLayout llmakenote;
    private Button ibok;
    private ArrayList<RecipeMenu> recipesmenu;
    private MenuDB recmenudb;
    private ArrayList<RecipePrepare> recclone;
    private AsyncTask<ArrayList<RecipeMenu>, Void, ArrayList<RecipePrepare>> async;
    private ArrayList<Recipe> recipes;
    private ArrayList<Ingredient> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.row_ingredients);

        tf = Typeface.createFromAsset(getAssets(), "fonts/quikhand.ttf");
        llmakenote = (LinearLayout) this.findViewById(R.id.llmakenote);
        TextView tvnameofrecipe = (TextView) findViewById(R.id.tvRecipeMakeNote);
        tvnameofrecipe.setVisibility(View.GONE);
        ScrollView scrollview = (ScrollView) findViewById(R.id.scviewmakenote);
        scrollview.setBackgroundResource(R.drawable.sticky);

        ingdb = new IngredientsDB(this);
        recdb = new RecipeDB(this);
        recing = new RecipeIngredientsDB(this);
        recmenudb = new MenuDB(this);

        ingredients = new ArrayList<Ingredient>();
        recipesmenu = new ArrayList<RecipeMenu>();
        recings = new ArrayList<RecipePrepare>();
        recipesforbuy = new ArrayList<String>();
        recclone = new ArrayList<RecipePrepare>();

        arrayForSharedPrefIng = new ArrayList<String>();
        arrayForSharedPrefQu = new ArrayList<String>();
        arrayForSharedPrefMu = new ArrayList<String>();
        recipesmenu = recmenudb.getAllMenu();

        async = new AsyncTaskMakeNoteForAll().execute(recipesmenu);
        try {
            recings = async.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if(recings.size()!=0) {
            recclone.clear();
            recclone.add(recings.get(0));

            for (int m = 1; m < recings.size(); m++) {
                boolean existinrecclone = false;
                for (int n = 0; n < recclone.size(); n++) { //u okviru recclone ispituje da li postoji ista namirnica, ako postoji dodaje kolicinu
                    if (recings.get(m).getIng_id().equals(recclone.get(n).getIng_id())) {
                        existinrecclone = true;
                        int suma = Integer.parseInt(recings.get(m).getIng_qu()) + Integer.parseInt(recclone.get(n).getIng_qu());
                        RecipePrepare rpforadd = new RecipePrepare();
                        rpforadd.setId(recings.get(m).getId());
                        rpforadd.setIng_id(recings.get(m).getIng_id());
                        rpforadd.setIng_qu(Integer.toString(suma));
                        rpforadd.setRec_id(recings.get(m).getRec_id());
                        recclone.set(n, rpforadd);
                    }
                }

                //ako ne postoji i nije prvi onda uzmi sledecu namirnicu
                if (!existinrecclone) {
                    recclone.add(recings.get(m));
                }
            }

            insertTextView();

            ibok = new Button(this);
            ibok.setBackgroundResource(R.drawable.selectormakenoteicon);
            ibok.setOnClickListener(this);
            LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            lpp.gravity = Gravity.RIGHT;
            ibok.setLayoutParams(lpp);
            llmakenote.addView(ibok);

        } else {
            Toast.makeText(this, "No ingredients for making note", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    private class AsyncTaskMakeNoteForAll extends AsyncTask<ArrayList<RecipeMenu>, Void, ArrayList<RecipePrepare>> {

        @Override
        protected ArrayList<RecipePrepare> doInBackground(
                ArrayList<RecipeMenu>... params) {

            recipesmenu = params[0];

            for (int i = 0; i < recipesmenu.size(); i++) {
                if (recipesmenu.get(i).getCheck().equals("true")) {
                    recipesforbuy.add(recipesmenu.get(i).getRec_id());
                }
            }

            //uzmi svojstva iz RecipePrepareDB
            for (int j = 0; j < recipesforbuy.size(); j++) {
                recclone = recing.getAllIngredientsByRecipeId(recipesforbuy.get(j));
                for (int k = 0; k < recclone.size(); k++) {
                    recings.add(recclone.get(k));
                }
            }

            return recings;
        }

    }

    private void insertTextView() {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        lp.setMargins(5, 10, 0, 0);
        m = 0;//brojac za sharedpref

        ingdb.getDb().open();
        ingredients = (ArrayList<Ingredient>) ingdb.getAllIngredients();

        for (int q = 0; q < recclone.size(); q++) {
            TextView tvIng = new TextView(this);
            tvIng.setTextColor(Color.BLUE);
            tvIng.setTextSize(20);
            tvIng.setTypeface(tf);
            tvIng.setLayoutParams(lp);
            for (int w = 0; w < ingredients.size(); w++) {
                if (ingredients.get(w).getIng_id().equals(recclone.get(q).getIng_id())) {
                    arrayForSharedPrefIng.add(ingredients.get(w).getIngreident());
                    arrayForSharedPrefQu.add(recclone.get(q).getIng_qu());
                    arrayForSharedPrefMu.add(ingredients.get(w).getmMu());
                    tvIng.setText(" -" + ingredients.get(w).getIngreident() + ",  " + recclone.get(q).getIng_qu() + "  " + ingredients.get(w).getmMu());
                    m++;
                }
            }
            llmakenote.addView(tvIng);

        }

    }

    private void SavePref(String ing, String qu, String mu) {

        if (m > 0) {
            editor.putString("Ingredient" + m, ing);
            editor.putString("Qu" + m, qu);
            editor.putString("Mu" + m, mu);
            editor.putBoolean("checked" + m, false);
            m--;
        }
    }

    @Override
    public void onClick(View v) {

        preferences = getApplicationContext().getSharedPreferences("WeeklyShoppingList", this.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putInt("Size", m);

        for (int sp = 0; sp < arrayForSharedPrefIng.size(); sp++) {
            SavePref(arrayForSharedPrefIng.get(sp), arrayForSharedPrefQu.get(sp), arrayForSharedPrefMu.get(sp));
        }

        editor.commit();

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("setallmenu", true);
        startActivity(i);
        this.finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}
