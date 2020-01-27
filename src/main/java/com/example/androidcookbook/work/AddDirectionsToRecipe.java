package com.example.androidcookbook.work;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidcookbook.R;
import com.example.androidcookbook.mydb.RecipeDB;
import com.example.androidcookbook.object.Ingredient;

import java.util.ArrayList;


public class AddDirectionsToRecipe extends AppCompatActivity implements OnClickListener {

    private TextView tvIngredient;
    private TextView tvQuantity;
    private ArrayList<Ingredient> ingresults;
    private ArrayList<String> quantities;
    private TextView tvMu;
    private TextView tvRowNumber;
    private String directions;
    private EditText eDirections;
    private RecipeDB receptDB;
    private ScrollView sDirections;
    private Typeface tf;
    private TextView tv;
    private TextView tv2;
    private TextView tv1;
    private InputMethodManager mgr;
    private int inType;
    private TextView nameOfRecipe;
    private TableLayout tl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.directions);

        tf = Typeface.createFromAsset(getAssets(),
                "fonts/quikhand.ttf");

        mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        receptDB = new RecipeDB(this);
        ingresults = new ArrayList<Ingredient>();
        quantities = new ArrayList<String>();

        nameOfRecipe = (TextView) findViewById(R.id.nameOfrecipe);
        nameOfRecipe.setTypeface(tf);
        nameOfRecipe.setText(getIntent().getExtras().getString("nameOfrecipe"));

        tl = (TableLayout) findViewById(R.id.tableOfingredients);

        sDirections = (ScrollView) findViewById(R.id.directionsScroll);
        eDirections = (EditText) findViewById(R.id.directionsET);

        eDirections.setTypeface(tf);

        if (getIntent().getExtras().getString("coretctingdescr").toString() != "") {
            eDirections.setText(getIntent().getExtras().getString("coretctingdescr").toString());
        }

        ingresults = getIntent().getExtras().getParcelableArrayList("ingredients"); //from AddRecipe.class
        quantities = getIntent().getExtras().getStringArrayList("quantities");

        addRow(tl, ingresults, quantities);

        inType = eDirections.getInputType(); //backup input type for ingAC, koristi se prilikom uklanjanja kursora i vracanja istog
        eDirections.setInputType(InputType.TYPE_NULL);

        eDirections.setOnClickListener(this);

    }

    protected void addDirections() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("directions", directions);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.directionsET:
                eDirections.setTextIsSelectable(true);
                eDirections.setInputType(inType);
                mgr.showSoftInput(eDirections, InputMethodManager.SHOW_FORCED); //show keyboard
                nameOfRecipe.setVisibility(View.GONE);
                tl.setVisibility(View.GONE);
                return;

            default:
                break;
        }
    }

    private void addRow(TableLayout tl, ArrayList<Ingredient> ingresults, ArrayList<String> quantities) {

        for (int i = 0; i < ingresults.size(); i++) {
            TableRow row = new TableRow(this);
            row.setId(i + 1);
            tv = new TextView(this);
            tv1 = new TextView(this);
            tv2 = new TextView(this);
            tvRowNumber = new TextView(this);
            tvIngredient = new TextView(this);
            tvQuantity = new TextView(this);
            tvMu = new TextView(this);

            tv.setTypeface(tf);
            tv2.setTypeface(tf);
            tvIngredient.setTypeface(tf);
            tvQuantity.setTypeface(tf);
            tvMu.setTypeface(tf);

            tv.setTextColor(Color.parseColor("#5C5C8A"));
            tv1.setTextColor(Color.parseColor("#5C5C8A"));
            tv2.setTextColor(Color.parseColor("#5C5C8A"));
            tvIngredient.setTextColor(Color.parseColor("#5C5C8A"));
            tvQuantity.setTextColor(Color.parseColor("#5C5C8A"));
            tvMu.setTextColor(Color.parseColor("#5C5C8A"));


            tvRowNumber.setText("" + i + ". "); //0
            tv.setText("- "); //1
            tvIngredient.setText(ingresults.get(i).getIngreident().toString()); //2
            tv1.setText(", "); //3
            tvQuantity.setText(quantities.get(i).toString());//4
            tv2.setText(", "); //5
            tvMu.setText(ingresults.get(i).getmMu().toString()); //6

            TableRow.LayoutParams lp = new TableRow.LayoutParams();
            lp.setMargins(2, 2, 5, 0);

            row.addView(tv, lp);
            row.addView(tvIngredient, lp);
            row.addView(tv1, lp);
            row.addView(tvQuantity, lp);
            row.addView(tv2, lp);
            row.addView(tvMu, lp);
            tl.addView(row);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_recipe_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_save:
                directions = eDirections.getText().toString();
                addDirections();
                mgr.hideSoftInputFromWindow(eDirections.getWindowToken(), 0);
                return true;

            case R.id.menu_cancel:
                try {
                    if (getIntent().getExtras().getString("coretctingdescr") != null) {
                        directions = getIntent().getExtras().getString("coretctingdescr").toString();
                    }
                    mgr.hideSoftInputFromWindow(eDirections.getWindowToken(), 0);
                    addDirections();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (receptDB.getDb() != null) {
            receptDB.getDb().close();
        }
    }

}