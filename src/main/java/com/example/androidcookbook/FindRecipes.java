package com.example.androidcookbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidcookbook.mydb.IngredientsDB;
import com.example.androidcookbook.mydb.RecipeIngredientsDB;
import com.example.androidcookbook.object.Ingredient;
import com.example.androidcookbook.work.ShowListOfRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FindRecipes extends AppCompatActivity implements OnClickListener {

    private AutoCompleteTextView ingAC;
    private TableLayout table;
    private EditText evQuantity;
    private TextView tvIngredient;
    private TextView tvMu;

    private IngredientsDB ingredientsDB;
    protected String selectionI;// selection from autocomplitetextview -->
    // Milk,dl....

    private List<Ingredient> ingsList;// all ingredients from Ingredients table
    private String[] ings; // ingredients into array {(ing,mu),(ing1,mu1)...}
    private String separators;// separators is ","

    private int id;
    private LinearLayout linearLayout;
    private TextView tvId;
    private ArrayList<String> ingU_id;
    private ArrayList<String> quInDBal;
    private ArrayList<Ingredient> ingInDBal;
    private ImageButton bDel;
    private RecipeIngredientsDB recipePrepareDB;
    private TableRow row;
    private Typeface tf;
    protected String ing;
    protected String mu;
    protected String ing_id;
    private TextView tv;
    private TextView tv1;
    private TextView tv2;
    private LayoutParams p;
    private InputMethodManager mgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {  //------ISTO KAO KOD AddingredientToRecipe.class
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ingredients_to_recipe);

        mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        tf = Typeface.createFromAsset(getAssets(), "fonts/quikhand.ttf");

        linearLayout = (LinearLayout) findViewById(R.id.lladdIngToRec);
        p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        table = new TableLayout(this);
        table.removeAllViews();
        table.setLayoutParams(p);
        id = 0;
        separators = ",";

        ingAC = (AutoCompleteTextView) findViewById(R.id.autoCompleteIngrediente);

        new ArrayList<String>();
        new ArrayList<String>();
        ingU_id = new ArrayList<String>();
        quInDBal = new ArrayList<String>();
        ingInDBal = new ArrayList<Ingredient>();

        // need for autocomplite text view
        ingredientsDB = new IngredientsDB(this);
        recipePrepareDB = new RecipeIngredientsDB(this);

        ingsList = ingredientsDB.getAllIngredients();

        // dodavanje svojstava preko autocompletTV
        AddIngrAutoCompleteTV();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.find_recipe_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_find:
                saveIt();
                Intent returnIntent = new Intent(this, ShowListOfRecipe.class);
                returnIntent.putParcelableArrayListExtra("findI", ingInDBal); // sva uneta svojstva-ovde se nalazi sve
                returnIntent.putStringArrayListExtra("findQ", quInDBal);// sve unete kolicine
                startActivity(returnIntent);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    private void AddIngrAutoCompleteTV() {
        // from AutoCompleteTextView
        ings = new String[ingsList.size()];
        for (int i = 0; i < ingsList.size(); i++) {
            ings[i] = ingsList.get(i).getIngreident().toString() + ", "
                    + ingsList.get(i).getmMu().toString();
        }

        // dodavanje novih svojstava
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, ings);

        ingAC.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long rowId) {
                selectionI = (String) parent.getItemAtPosition(pos);

                //ing_id = selectionI.split(Pattern.quote(separators))[0];
                ing = selectionI.split(Pattern.quote(separators))[0];
                mu = selectionI.split(Pattern.quote(separators))[1];
                id = id + 1; // the count of ingredients rows

                Ingredient ingredinet = ingredientsDB.getIngs(ing);
                ing_id = ingredinet.getIng_id();
                //change orientation for ingrideints which name lenght is more than 20 <------TREBA I VRATITI KADA SE OBRISE!!!!!!!!!
                if (ing.length() > 20) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                }

                addRow(linearLayout, ing, "0", mu, id, ing_id);

                mgr.hideSoftInputFromWindow(ingAC.getWindowToken(), 0); //skloni tastaturu

                ingU_id.add(ing_id.toString());
                ingAC.setText("");
            }
        });

        ingAC.setThreshold(1);
        ingAC.setAdapter(adapter);
        linearLayout.addView(table);

    }

    //puni arraylists ingInDBal iquInDBal sa odabranim namirnicama i njihovom količinom-one koje imamo    ------ISTO KAO KOD AddingredientToRecipe.class
    private void saveIt() {

        ingInDBal.clear();
        quInDBal.clear();

        for (int i = 0; i <= table.getChildCount(); i++) {
            View child = table.getChildAt(i);
            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;
                TextView tIngId = (TextView) row.getChildAt(0);
                TextView tIng = (TextView) row.getChildAt(2);
                EditText eQuant = (EditText) row.getChildAt(4);
                TextView tMu = (TextView) row.getChildAt(6);
                if (eQuant.getText().toString().equals("0")) {
                    Toast.makeText(this, "Količina namirnice ne može da bude 0. Namirnica nece biti uneta.", Toast.LENGTH_LONG).show();
                } else {
                    ingInDBal.add(new Ingredient(tIngId.getText().toString(), tIng.getText().toString(), tMu.getText().toString(), "0"));
                    quInDBal.add(eQuant.getText().toString());
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            default://for delete ingredients button
                View r = (View) v.getParent();
                ViewGroup container = ((ViewGroup) r.getParent());
                container.removeView(r);
                container.invalidate();
                return;
        }
    }

    // insert table row into tableLayout, preview ingredients name and editable quantities -----ISTO KAO KOD aDDINGREDIENTTORECIPE.CLASS
    protected void addRow(LinearLayout linearLayout, String ing,
                          String qu, String mu, int id, String ing_id) {

        row = new TableRow(this);
        row.setId(id);

        tvId = new TextView(this);
        tv = new TextView(this);
        tv1 = new TextView(this);
        tv2 = new TextView(this);
        tvIngredient = new TextView(this);
        evQuantity = new EditText(this);
        tvMu = new TextView(this);
        bDel = new ImageButton(this);

        tv.setTypeface(tf);
        tv2.setTypeface(tf);
        tvIngredient.setTypeface(tf);
        evQuantity.setTypeface(tf);
        tvMu.setTypeface(tf);

        tv.setTextColor(Color.parseColor("#3d579f"));
        tv1.setTextColor(Color.parseColor("#3d579f"));
        tv2.setTextColor(Color.parseColor("#3d579f"));
        tvIngredient.setTextColor(Color.parseColor("#3d579f"));
        evQuantity.setTextColor(Color.parseColor("#3d579f"));
        tvMu.setTextColor(Color.parseColor("#3d579f"));

        evQuantity.setGravity(Gravity.RIGHT);
        tvIngredient.setTextSize(20);
        tvIngredient.setGravity(Gravity.LEFT);
        tvMu.setGravity(Gravity.LEFT);
        evQuantity.setTextSize(20);

        tvMu.setTextSize(20);

        bDel.setBackgroundResource(R.drawable.delete);
        evQuantity.setId(Integer.parseInt(ing_id));
        bDel.setId(Integer.parseInt(ing_id));

        tvId.setText(ing_id.toString()); //0
        tvId.setVisibility(View.INVISIBLE);
        tv.setText("- "); //1
        tvIngredient.setText(ing); //2
        tv1.setText(", "); //3
        evQuantity.setHint("0.00");
        evQuantity.setHintTextColor(Color.parseColor("#cccccc"));
        if (qu != "0") {
            evQuantity.setText(qu); //4
        }
        tv2.setText(", "); //5
        tvMu.setText(mu); //6

        TableRow.LayoutParams lpp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);
        TableRow.LayoutParams lpdel = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 5, 0, 0);
        lpp.setMargins(0, 5, 0, 0);
        lpdel.setMargins(15, 5, 0, 0);

        row.addView(tvId, lpp);
        row.addView(tv, lpp);
        tvIngredient.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
        row.addView(tvIngredient, lp);
        row.addView(tv1, lpp);
        evQuantity.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.7f));
        row.addView(evQuantity, lp);
        row.addView(tv2, lpp);
        row.addView(tvMu, lpp);
        row.addView(bDel, lpdel);

        bDel.setOnClickListener(this);

        table.addView(row);

    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (recipePrepareDB.getDb() != null) {
            recipePrepareDB.getDb().close();
        }
    }

    protected void onResume() {
        super.onResume();

        //add button for new ingredients, when return from AddIngredients.class
        ingredientsDB.getDb().open();
        ingsList.clear();
        linearLayout.removeAllViews(); //da se ne bi duplirali
        ingsList = ingredientsDB.getAllIngredients();
        ings = new String[ingsList.size()];
        AddIngrAutoCompleteTV();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
