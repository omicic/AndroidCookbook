package com.example.androidcookbook.work;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidcookbook.R;
import com.example.androidcookbook.mydb.IngredientsDB;
import com.example.androidcookbook.mydb.RecipeIngredientsDB;
import com.example.androidcookbook.object.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class AddIngredientToRecipe extends AppCompatActivity implements OnClickListener {

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

    private String ingU;// only ingredient name
    private String muU;// only ingredients mesurement unit
    private String quantityU;
    private ArrayList<String> mus;
    private int id;
    private LinearLayout linearLayout;
    private EditText eQuant;
    private TextView tvId;
    private ArrayList<String> qu;
    private ArrayList<String> ingU_id;
    private ArrayList<String> quInDBal;
    private ArrayList<Ingredient> ingInDBal;
    private String ing_idU;
    private ImageButton bDel;
    private RecipeIngredientsDB recipePrepareDB;
    private TableRow row;
    private Typeface tf;
    private Button addBT;
    protected String ing;
    protected String mu;
    private ArrayList<Ingredient> corecting;
    protected String ing_id;
    private TextView tv;
    private TextView tv1;
    private TextView tv2;
    private Button cancelBT;
    private InputMethodManager mgr;
    private int inType;
    private ImageButton addIng;
    private String[] ingsids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ingredients_to_recipe);

        tf = Typeface.createFromAsset(getAssets(), "fonts/quikhand.ttf");
        mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        linearLayout = (LinearLayout) findViewById(R.id.lladdIngToRec);
        ingAC = (AutoCompleteTextView) findViewById(R.id.autoCompleteIngrediente);

        inType = ingAC.getInputType(); //backup input type for ingAC, koristi se prilikom uklanjanja kursora i vracanja istog
        ingAC.setInputType(InputType.TYPE_NULL);
        ingAC.setOnClickListener(this);

        table = new TableLayout(this);
        table.removeAllViews();

        id = 0;
        separators = ",";

        mus = new ArrayList<String>();
        qu = new ArrayList<String>();
        ingU_id = new ArrayList<String>();
        quInDBal = new ArrayList<String>();
        ingInDBal = new ArrayList<Ingredient>();

        // need for autocomplite text view
        ingredientsDB = new IngredientsDB(this);
        recipePrepareDB = new RecipeIngredientsDB(this);

        // dodavanje svojstava preko autocompletTV
        ingsList = ingredientsDB.getAllIngredients();
        ings = new String[ingsList.size()];
        ingsids = new String[ingsList.size()];

        linearLayout.removeAllViews();
        AddIngrAutoCompleteTV();

        //ako se ponovo vratimo na dodavanje/izmenu namirnica-promenljivh koje jos NISU unete u bazu.
        if (getIntent().getParcelableArrayListExtra("corectentereding") != null) {    //iz ChangeRecipe
            corecting = getIntent().getParcelableArrayListExtra("corectentereding");
            table.removeAllViews();
            for (int m = 0; m < corecting.size(); m++) {
                ing = corecting.get(m).getIngreident().toString();
                ing_id = corecting.get(m).getIng_id().toString();
                mu = corecting.get(m).getmMu().toString();
                id = id + 1;
                addRow(linearLayout, ing, (getIntent().getStringArrayListExtra("corectenteredqu").get(m)).toString(), mu, id, ing_id);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_recipe_menu, menu);
        menu.getItem(0).setVisible(true);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_save:
                saveIt(); // into arrayList
                break;

            case R.id.menu_add_ing:

                ingredientsDB.getDb().close();
                ingsList.clear();
                Intent intentAddIng = new Intent(this, AddIngredient.class);
                intentAddIng.putExtra("newIng", "newIng");
                startActivity(intentAddIng);

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void AddIngrAutoCompleteTV() {
        // from AutoCompleteTextView

        for (int i = 0; i < ingsList.size(); i++) {

            ings[i] = ingsList.get(i).getIngreident().toString() + ", "
                    + ingsList.get(i).getmMu().toString();// add values from ingredientsDB into format "ingredient,mu"

        }


        // dodavanje novih svojstava
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, ings);

        ingAC.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long rowId) {

                selectionI = (String) parent.getItemAtPosition(pos);

                ing = selectionI.split(Pattern.quote(separators))[0];
                mu = selectionI.split(Pattern.quote(separators))[1];
                id = id + 1; // the count of ingredients rows
                Ingredient ingredinet = ingredientsDB.getIngs(ing);
                ing_id = ingredinet.getIng_id();

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

    private void saveIt() {

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

        returnIntent(ingInDBal, quInDBal);
        this.finish();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.autoCompleteIngrediente:
                ingAC.setTextIsSelectable(true);
                ingAC.setInputType(inType);
                mgr.showSoftInput(ingAC, InputMethodManager.SHOW_FORCED); //show keyboard
                return;

            default:
                //delete row
                View r = (View) v.getParent();
                ViewGroup container = ((ViewGroup) r.getParent());
                container.removeView(r);
                container.invalidate();
                return;
        }
    }

    private void returnIntent(ArrayList<Ingredient> ingInDBal,
                              ArrayList<String> quInDBal) {

        Intent returnIntent = new Intent();
        returnIntent.putParcelableArrayListExtra("i", ingInDBal); // sva uneta svojstva-ovde se nalazi sve
        returnIntent.putStringArrayListExtra("q", quInDBal);// sve unete kolicine
        setResult(RESULT_OK, returnIntent);

    }

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

        tv.setTextColor(Color.parseColor("#2E2EB8"));
        tv1.setTextColor(Color.parseColor("#2E2EB8"));
        tv2.setTextColor(Color.parseColor("#2E2EB8"));
        tvIngredient.setTextColor(Color.parseColor("#2E2EB8"));
        evQuantity.setTextColor(Color.parseColor("#2E2EB8"));
        tvMu.setTextColor(Color.parseColor("#2E2EB8"));

        //setInputType()
        evQuantity.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        evQuantity.setHint("0.00");
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
        if (qu != "0") {
            evQuantity.setText(qu); //4
        }
        tv2.setText(", "); //5
        tvMu.setText(mu); //6

        TableRow.LayoutParams lpp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);
        TableRow.LayoutParams lppp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 5, 0, 0);
        lpp.setMargins(0, 5, 0, 0);
        lppp.setMargins(10, 5, 0, 0);

        row.addView(tvId, lpp);
        row.addView(tv, lpp);
        tvIngredient.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
        row.addView(tvIngredient, lp);
        row.addView(tv1, lpp);
        row.addView(evQuantity, lpp);
        row.addView(tv2, lpp);
        row.addView(tvMu, lpp);
        row.addView(bDel, lppp);

        bDel.setOnClickListener(this);

        table.addView(row);

    }

    protected void onResume() {
        super.onResume();

        //add button for new ingredients, when return from AddIngredients.class
        ingredientsDB.getDb().open();
        ingsList.clear();
        linearLayout.removeAllViews(); //da se ne bi duplirali
        ingsList = ingredientsDB.getAllIngredients();
        ings = new String[ingsList.size()];
        ingsids = new String[ingsList.size()];
        AddIngrAutoCompleteTV();

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        this.finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ingredientsDB.getDb() != null) {
            ingredientsDB.getDb().close();
        }
        if (recipePrepareDB.getDb() != null) {
            recipePrepareDB.getDb().close();
        }
        finish();
    }
}
