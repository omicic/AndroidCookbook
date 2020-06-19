package com.example.androidcookbook;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidcookbook.mydb.IngredientsDB;
import com.example.androidcookbook.mydb.RecipeDB;
import com.example.androidcookbook.mydb.RecipeIngredientsDB;
import com.example.androidcookbook.object.Ingredient;
import com.example.androidcookbook.object.Recipe;
import com.example.androidcookbook.object.RecipePrepare;

import java.util.ArrayList;


public class MakeNote extends AppCompatActivity implements OnClickListener {

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
    private LinearLayout llmakenote;
    private Button ibok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.row_ingredients);

        tf = Typeface.createFromAsset(getAssets(), "fonts/quikhand.ttf");
        llmakenote = (LinearLayout) this.findViewById(R.id.llmakenote);
        ScrollView scrollview = (ScrollView) findViewById(R.id.scviewmakenote);
        //scrollview.setBackgroundResource(R.drawable.sticky);

        ingdb = new IngredientsDB(this);
        recdb = new RecipeDB(this);
        recing = new RecipeIngredientsDB(this);

        rec = recdb.getRecepteById(getIntent().getExtras().get("rec_id").toString()); //iz MyAdapterPrikazRecepta.class: case R.id.cell_row_compare

        recings = new ArrayList<RecipePrepare>();

        ingredientstobuy = new ArrayList<String>();
        quantitiestobuy = new ArrayList<String>();

        arrayForSharedPrefIng = new ArrayList<String>();
        arrayForSharedPrefQu = new ArrayList<String>();
        arrayForSharedPrefMu = new ArrayList<String>();

        TextView tvRecipe = (TextView) findViewById(R.id.tvRecipeMakeNote);
        tvRecipe.setId(Integer.parseInt(getIntent().getExtras().get("rec_id").toString()));

        tvRecipe.setTypeface(tf);
        tvRecipe.setTextColor(Color.BLUE);

        String recipe = recdb.getRecepteById(getIntent().getExtras().get("rec_id").toString()).getRecipe();
        tvRecipe.setText("For: " + recipe.toString());

        //prikazivanje preostalih sastojaka koji nisu zadati a potrebni su za pripremu jela
        postoji = false;
        i = 0;
        m = 0;//brojac za sharedpref

        recings = recing.getAllIngredientsByRecipeId(rec.getId().toString());
        recing.getDb().close();

        ingredientstobuy = getIntent().getStringArrayListExtra("ingredientstobuy");        //from MyAdapterPrikazRecepata.java
        quantitiestobuy = getIntent().getStringArrayListExtra("quantitiestobuy");        //from MyAdapterPrikazRecepata.java

        for (int k = 0; k < recings.size(); k++) { //sva svojstva za dati recept

            postoji = false;
            for (int ii = 0; ii < ingredientstobuy.size(); ii++) {
                if (recings.get(k).getIng_id().toString().equals(ingredientstobuy.get(ii).toString())) {
                    //-----treba da zapamtimo koji postoje i cija je vrednost <=0
                    //pisi to iz ingredient
                    postoji = true;
                    i = ii;
                }
            }

            if (postoji) {

                ingtofind = ingredientstobuy.get(i).toString();
                qutofind = quantitiestobuy.get(i).toString();
                ing = ingdb.getIngredientById(ingtofind);
                if (Float.parseFloat(qutofind) > 0) {

                    //-------TREBA PUNITI NIZ KOJI CE SE SNIMITI U SHAREDPREFER
                    arrayForSharedPrefIng.add(ing.getIngreident().toString());
                    arrayForSharedPrefQu.add(qutofind.toString());
                    arrayForSharedPrefMu.add(ing.getmMu().toString());

                    insertTextView(ing.getIngreident().toString(), qutofind.toString(), ing.getmMu().toString());
                    m++;
                }


            } else {

                ingtofind = recings.get(k).getIng_id().toString(); //OVO JE OK
                qutofind = recings.get(k).getIng_qu().toString(); //OVO JE OK

                //-------TREBA PUNITI NIZ KOJI CE SE SNIMITI U SHAREDPREFER
                arrayForSharedPrefIng.add(ingdb.getIngredientById(ingtofind).getIngreident().toString());
                arrayForSharedPrefQu.add(qutofind.toString());
                arrayForSharedPrefMu.add(ingdb.getIngredientById(ingtofind).getmMu().toString());

                insertTextView(ingdb.getIngredientById(ingtofind).getIngreident().toString(),
                        qutofind.toString(),
                        ingdb.getIngredientById(ingtofind).getmMu().toString());
                m++;

            }
        }

        ibok = new Button(this);
        ibok.setBackgroundResource(R.drawable.selectormakenoteicon);
        ibok.setOnClickListener(this);
        LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lpp.gravity = Gravity.RIGHT;
        ibok.setLayoutParams(lpp);
        llmakenote.addView(ibok);
    }

    private void insertTextView(String ing, String qu, String mu) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        lp.setMargins(40, 10, 0, 0);
        if (Float.parseFloat(qu) > 0) {

            TextView tvIng = new TextView(this);
            tvIng.setId(Integer.parseInt(getIntent().getExtras().get("rec_id").toString()));
            tvIng.setTextColor(Color.BLUE);
            tvIng.setTextSize(20);
            tvIng.setTypeface(tf);
            tvIng.setLayoutParams(lp);
            tvIng.setText(" - " + ing + ",  " + qu + " " + mu);

            llmakenote.addView(tvIng);

        }
    }

    private void SavePref(String recipe, String ing, String qu, String mu) {

        preferences = getApplicationContext().getSharedPreferences("Note" + rec.getId(), getApplicationContext().MODE_PRIVATE);

        if (m > 0) {
            editor = preferences.edit();
            editor.putString("Recipe", rec.getRecipe().toString());
            editor.putInt("size", arrayForSharedPrefIng.size());
            editor.putString("Ingredient" + m, ing);
            editor.putString("Qu" + m, qu);
            editor.putString("Mu" + m, mu);
            editor.commit();
            m--;
        }
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onClick(View arg0) {

        for (int sp = 0; sp < arrayForSharedPrefIng.size(); sp++) {
            SavePref(rec.getRecipe(), arrayForSharedPrefIng.get(sp).toString(), arrayForSharedPrefQu.get(sp).toString(), arrayForSharedPrefMu.get(sp).toString());
        }

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        this.finish();

    }

}
