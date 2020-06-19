package com.example.androidcookbook.work;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidcookbook.MyAdapterPrikazRecepata;
import com.example.androidcookbook.R;
import com.example.androidcookbook.mydb.MenuDB;
import com.example.androidcookbook.mydb.RecipeDB;
import com.example.androidcookbook.mydb.RecipeIngredientsDB;
import com.example.androidcookbook.object.Ingredient;
import com.example.androidcookbook.object.Recipe;
import com.example.androidcookbook.object.RecipePrepare;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;


public class ShowListOfRecipe extends AppCompatActivity implements OnCheckedChangeListener, OnClickListener {

    private ListView listView;
    private CheckBox cbAll;

    private RecipeDB receptDB;
    private RecipeIngredientsDB recIngDB;
    private MenuDB menuDb;

    private MyAdapterPrikazRecepata listOfShowedRecept;


    private ArrayList<Recipe> selectedRecipe;
    private ArrayList<String> cbtag;
    private ArrayList<Boolean> checks;
    private ArrayList<Ingredient> frIngredients;
    private ArrayList<String> recipeids;
    private ArrayList<String> quantities;
    private ArrayList<String> ingredients;
    private ArrayList<String> temprecipes;
    private ArrayList<String> frQu;
    private ArrayList<String> diffques;
    private HashMap<String, String> recipeidforfindRecipes;

    private int br;
    private int j;
    private float diffqu;
    private float x;
    private float y;

    private boolean checkedAll;
    private AutoCompleteTextView searchRecipe;
    private ImageButton bClearSelection;
    private ImageButton bSearch;
    private int inType;
    private InputMethodManager mgr;
    private List<Recipe> allRecipes;
    private String[] recipes;
    private String selection;
    private ArrayAdapter<String> adapterac;
    private MyAdapterPrikazRecepata listOfShowedRecipes;
    private File file;
    private Recipe deletedrecipe;
    private boolean emtylistforsearch;


    private Spinner searchSpinnerForCategory;
    private String cat;//from spinner
    private String categoryForSpinner;
    private boolean firstEventConsumed;
    private boolean isUserAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_show_list_layout);
        mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        listView = (ListView) this.findViewById(R.id.list);
        cbAll = (CheckBox) findViewById(R.id.cbAll);

        searchRecipe = (AutoCompleteTextView) findViewById(R.id.acIngredients);

        //searchRecipe.setHint("Recipe...");
        bSearch = (ImageButton) findViewById(R.id.bSearch);
        bClearSelection = (ImageButton) findViewById(R.id.bClearSelection);

        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
        receptDB = new RecipeDB(this);
        recIngDB = new RecipeIngredientsDB(this);
        menuDb = new MenuDB(this);

        deletedrecipe = new Recipe();

        cbtag = new ArrayList<String>();
        checks = new ArrayList<Boolean>();
        recipeidforfindRecipes = new HashMap<String, String>();
        recipeids = new ArrayList<String>();
        temprecipes = new ArrayList<String>();
        quantities = new ArrayList<String>();
        ingredients = new ArrayList<String>();
        frIngredients = new ArrayList<Ingredient>();
        frQu = new ArrayList<String>();
        diffques = new ArrayList<String>();
        selectedRecipe = new ArrayList<Recipe>();

        selectedRecipe.clear();
        temprecipes.clear();
        recipeids.clear();
        ingredients.clear();
        diffques.clear();
        recipeidforfindRecipes.clear();
        checkedAll = false;

        j = 0;
        br = 0;

        AddIngrAutoCompleteTV();
       // Log.d("addingmeal", getIntent().getStringExtra("addingmeal"));
        postaviAdapter(); //razlicit jer se poziva iz vise klasa

        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        inType = searchRecipe.getInputType(); //backup input type for searchIng
        searchRecipe.setInputType(InputType.TYPE_NULL);
        searchRecipe.setVisibility(View.VISIBLE);
        searchRecipe.setOnClickListener(this);
        bSearch.setOnClickListener(this);
        bClearSelection.setOnClickListener(this);
        cbAll.setOnCheckedChangeListener(this);

        categoryForSpinner = "";
        searchSpinnerForCategory = (Spinner) findViewById(R.id.categoryspinner);
        ArrayAdapter<CharSequence> adapterspinner = ArrayAdapter.createFromResource(
                this, R.array.category_arrays, android.R.layout.simple_spinner_item);

        adapterspinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinnerForCategory.setAdapter(adapterspinner);
        searchSpinnerForCategory.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isUserAction = true;
                return false;
            }
        });

        searchSpinnerForCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isUserAction) {
                   cat = parent.getItemAtPosition(position).toString();
                   setAdapterr(cat);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void AddIngrAutoCompleteTV() {

        allRecipes = receptDB.getAllRecepte();
        recipes = new String[allRecipes.size()];

        for (int i = 0; i < allRecipes.size(); i++) {
            recipes[i] = allRecipes.get(i).getRecipe().toString();
        }

        selection = "";
        adapterac = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, recipes);

        searchRecipe.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long rowId) {
                selection = (String) parent.getItemAtPosition(pos);
                searchRecipe.setText(selection);
                searchRecipe.setSelection(searchRecipe.getText().length()); //Put the cursor on the end
            }
        });

        searchRecipe.setThreshold(1);
        searchRecipe.setAdapter(adapterac);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.acIngredients) {        //for keyboard showing
            searchRecipe.setTextIsSelectable(true);
            searchRecipe.requestFocus();
            searchRecipe.setInputType(inType);
            mgr.showSoftInput(searchRecipe, InputMethodManager.SHOW_FORCED); //show keyboard
        }

        if (v.getId() == R.id.bClearSelection) {
            searchRecipe.setText("");
            searchSpinnerForCategory.setVisibility(View.VISIBLE);
            searchSpinnerForCategory.setSelection(0);
            listOfShowedRecept = new MyAdapterPrikazRecepata(this, searchRecipe.getText().toString(), receptDB);
            listView.setAdapter(listOfShowedRecept);
            mgr.hideSoftInputFromWindow(bClearSelection.getWindowToken(), 0); //hide keyboard
        }

        if (v.getId() == R.id.bSearch) {
            listOfShowedRecept = new MyAdapterPrikazRecepata(this, searchRecipe.getText().toString(), receptDB);
            listView.setAdapter(listOfShowedRecept);
            searchSpinnerForCategory.setVisibility(View.INVISIBLE);
            mgr.hideSoftInputFromWindow(bSearch.getWindowToken(), 0);  //hide keyboard
        }
    }

    private void postaviAdapter() {
        //kada startujemo iz FindRecipes.class, prosledjujemo sve namirnice koje imamo i kolicine
        if (getIntent().getParcelableArrayListExtra("findI") != null) {
            FindRecipes();
        } else {
            if (getIntent().getStringExtra("addingmeal") != null) { //from ShowWeeklyMenu.class
                cat = getIntent().getStringExtra("addingmeal");
                setAdapterr(cat);
            } else {
                setAdapterr("");//from AddRecipe.class
            }
        }
    }

    private void setAdapterForCategory(String string, String category) {//for ShowWeeklyMenu.class

        listOfShowedRecept = new MyAdapterPrikazRecepata(this,
                string, category, receptDB, checkedAll);
        listView.setAdapter(listOfShowedRecept);

    }

    private void setAdapterr(String cat) {//if cat (category from spinner) is not set or is set
        if (cat == "") {
            listOfShowedRecept = new MyAdapterPrikazRecepata(this, null,
                    true, null, null,
                    null, null, receptDB, checkedAll);
            listView.setAdapter(listOfShowedRecept);

        } else { //da li se igde koristi? ShowListOfRecipe showListOfRecipes,
            listOfShowedRecept = new MyAdapterPrikazRecepata(this, null, cat, receptDB,checkedAll);
            listView.setAdapter(listOfShowedRecept);
            categoryForSpinner = cat;
        }
    }



    //koristi se za pronalaženje recepata za zadate namirnice
    private void FindRecipes() {

        frIngredients = getIntent().getParcelableArrayListExtra("findI"); //from FindRecipes.class
        frQu = getIntent().getStringArrayListExtra("findQ"); //from FindRecipes.class
        emtylistforsearch = false; //ako ne zadamo nijedan sastojak kao kriterijum pretrage, treba da nam prikaze sve recepte i

        ArrayList<RecipePrepare> allRecipeIng = (ArrayList<RecipePrepare>) recIngDB.getAllRecepteIngredients();

        //za svaku odabranu namirnicu pronadji recept u kojem se nalazi
        for (int f = 0; f < frIngredients.size(); f++) {        //OK

            if (frQu.get(f).toString().equals("0")) {
                y = 0;
            } else {
                y = Float.parseFloat(frQu.get(f).toString());
            }

            for (int r = 0; r < allRecipeIng.size(); r++) {
                diffqu = 0;
                if (allRecipeIng.get(r).getIng_id().toString().equals(frIngredients.get(f).getIng_id().toString())) {

                    recipeids.add(allRecipeIng.get(r).getRec_id().toString());
                    ingredients.add(frIngredients.get(f).getIng_id().toString());
                    quantities.add(allRecipeIng.get(r).getIng_qu().toString());

                    x = Float.parseFloat(allRecipeIng.get(r).getIng_qu().toString());
                    diffqu = x - y;

                    diffques.add(Float.toString(diffqu));
                }
            }
        }

        //If no receipes with entered ingredients than show message. That's mean there's no one recipes with that ingredient entered into database.
        if (!recipeids.isEmpty()) {
            //saberi broj namirnica po receptu i smesti u hashmapu <rec_id,broj>
            temprecipes = (ArrayList<String>) recipeids.clone();

            for (int n = 0; n < temprecipes.size(); n++) {
                br = 1;
                j = n + 1;
                while (temprecipes.size() > j) {
                    if (temprecipes.get(n).toString().equals(temprecipes.get(j).toString())) {
                        br++;
                        temprecipes.remove(j);
                        j--;
                    }
                    j++;
                }

                if (!Integer.toString(br).equals("0")) {
                    recipeidforfindRecipes.put(temprecipes.get(n).toString(), Integer.toString(br));
                }
            }

            listOfShowedRecept = new MyAdapterPrikazRecepata(this, null, false, recipeidforfindRecipes, recipeids, ingredients, diffques, receptDB, checkedAll);
            listView.setAdapter(listOfShowedRecept);
        } else {
            Toast.makeText(this, getResources().getString(R.string.toastnorecipes), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.add_delete_change_menu, menu);

        MenuItem item_add_to_menu = menu.findItem(R.id.menu_add_to_menu);
        MenuItem item_add = menu.findItem(R.id.menu_add);
        MenuItem item_delete = menu.findItem(R.id.menu_delete);

        if (getIntent().getStringExtra("addingmeal") != null){
            item_add_to_menu.setVisible(true);
            item_add.setVisible(false);
            item_delete.setVisible(false);
            if(listOfShowedRecept == null || listOfShowedRecept.isEmpty() ){
                item_add_to_menu.setVisible(false);
                item_add.setVisible(false);
                item_delete.setVisible(false);
                Toast.makeText(getApplicationContext(),"No recipes for category",Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_delete:

                receptDB.getDb().open();

                Iterator<Entry<String, Boolean>> myVeryOwnIterator = listOfShowedRecept.getChecked().entrySet().iterator();
                while (myVeryOwnIterator.hasNext()) {
                    Entry<String, Boolean> entry = myVeryOwnIterator.next();
                    cbtag.add(entry.getKey().toString());
                    checks.add(entry.getValue());
                }

                //brisanje istih
                for (int i = 0; i < cbtag.size(); i++) {

                    if (checks.get(i) == true) {
                        //delete photo from recipeimage
                        deletedrecipe = receptDB.getRecepteById(cbtag.get(i).toString());
                        file = new File(deletedrecipe.getPicture().toString());
                        if (file.exists()) {
                            file.delete();
                        }
                        receptDB.deleteRecept(cbtag.get(i));
                        menuDb.deleteForRecipe(cbtag.get(i));
                        recIngDB.removeAllForRec_id(cbtag.get(i));
                    }
                }

                recreate();
                return true;

            case R.id.menu_add:
                Intent addintent = new Intent(this, AddRecipe.class);
                addintent.putExtra("add_new_recipe", "addRecipe"); //use in AddRecipe, if create new recipe
                startActivity(addintent);
                //finish();
                return true;

            case R.id.menu_add_to_menu: //from ShowWeeklyMenu.class

                Iterator<Entry<String, Boolean>> myVeryOwnIteratoraddmenu = listOfShowedRecept.getChecked().entrySet().iterator();
                while (myVeryOwnIteratoraddmenu.hasNext()) {
                    Entry<String, Boolean> entry = myVeryOwnIteratoraddmenu.next();
                    cbtag.add(entry.getKey().toString());
                    checks.add(entry.getValue());
                }

                Intent returnIntentToShowWeekly = new Intent();
                returnIntentToShowWeekly.putStringArrayListExtra("add_to_menu_cbtag", cbtag); // sva uneta svojstva-ovde se nalazi sve
                returnIntentToShowWeekly.putExtra("add_to_menu_checks", checks);// sve unete kolicine
                setResult(RESULT_OK, returnIntentToShowWeekly);
                finish();
                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            checkedAll = true;
        } else {
            checkedAll = false;
        }
        postaviAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receptDB.getDb() != null) {
            receptDB.getDb().close();
        }
        if (recIngDB.getDb() != null) {
            recIngDB.getDb().close();
        }
        if (menuDb.getDb() != null) {
            menuDb.getDb().close();
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }
}
