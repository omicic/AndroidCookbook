package com.example.androidcookbook.work;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.androidcookbook.R;
import com.example.androidcookbook.mydb.RecipeDB;
import com.example.androidcookbook.object.Ingredient;

import java.util.ArrayList;

import static android.R.color.holo_blue_light;


public class ChangeDirection extends Activity {

    private TextView tvIngredient;
    private TextView tvQuantity;
    private ArrayList<Ingredient> ingresults;
    private ArrayList<String> quantities;
    private TextView tvMu;
    private TextView tvRowNumber;
    private Button bAdd;
    private Button bCancel;
    private String directions;
    private EditText eDirections;
    private RecipeDB receptDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.directions);

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/DK Carte Blanche.otf");

        receptDB = new RecipeDB(this);
        ingresults = new ArrayList<Ingredient>();
        quantities = new ArrayList<String>();

        TextView nameOfRecipe = (TextView) findViewById(R.id.nameOfrecipe);
        nameOfRecipe.setTypeface(tf);
        nameOfRecipe.setText(getIntent().getExtras().getString("nameOfrecipe"));

        TableLayout tl = (TableLayout) findViewById(R.id.tableOfingredients);

        ScrollView sDirections = (ScrollView) findViewById(R.id.directionsScroll);
        eDirections = (EditText) findViewById(R.id.directionsET);

        receptDB.getDb().open();
        eDirections.setText(getIntent().getExtras().getString("directions").toString());//put direction from ShowRecipe activity
        ingresults = getIntent().getExtras().getParcelableArrayList("ingredients");//getting from ChangeRecipe, when change recipe
        quantities = getIntent().getExtras().getStringArrayList("quantities");//getting from ChangeRecipe, when change recipe

        addTextView(tl, ingresults, quantities);

    }

    protected void addDirections() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("directionsforchange", directions);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_recipe_menu, menu);
        menu.getItem(0).setVisible(false);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_save:
                try {
                    directions = eDirections.getText().toString();
                    addDirections();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @SuppressLint("ResourceAsColor")
    private void addTextView(TableLayout tl, ArrayList<Ingredient> ingresults, ArrayList<String> quantities) {

        for (int i = 0; i < ingresults.size(); i++) {

            TableRow row = new TableRow(this);
            row.setId(i + 1);
            tvRowNumber = new TextView(this);
            tvIngredient = new TextView(this);
            tvQuantity = new TextView(this);
            tvMu = new TextView(this);

            tvIngredient.setTextColor(holo_blue_light);
            tvQuantity.setTextColor(holo_blue_light);
            tvMu.setTextColor(holo_blue_light);

            tvRowNumber.setText("" + i + ". ");
            tvIngredient.setText(" - " + ingresults.get(i).getIngreident().toString());
            tvQuantity.setText(", " + quantities.get(i).toString());
            tvMu.setText(" " + ingresults.get(i).getmMu().toString());

            TableRow.LayoutParams lp = new TableRow.LayoutParams();
            lp.setMargins(2, 2, 2, 2);
            row.addView(tvIngredient, lp);
            row.addView(tvQuantity, lp);
            row.addView(tvMu, lp);
            tl.addView(row);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
