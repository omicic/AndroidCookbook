package com.example.androidcookbook.work;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import android.widget.Toast;

import com.example.androidcookbook.MyAdapterShowIngredients;
import com.example.androidcookbook.R;
import com.example.androidcookbook.mydb.IngredientsDB;
import com.example.androidcookbook.mydb.RecipeIngredientsDB;
import com.example.androidcookbook.object.Ingredient;

import java.util.ArrayList;
import java.util.Iterator;


public class ShowIngredients extends Activity implements OnCheckedChangeListener, OnClickListener {

    private IngredientsDB ingredientDB;
    private ArrayList<String> cbtag;
    private ArrayList<Ingredient> selectedIngredients;
    private ListView listView;
    private ImageButton closeBT;
    private MyAdapterShowIngredients listOfShowedIngredients;
    private Iterator<String> myVeryOwnIterator;
    private RecipeIngredientsDB recipePrepare;
    private CheckBox cbAll;
    private boolean checkedAll;
    private AutoCompleteTextView searchIng;
    private ImageButton bSearch;
    private ArrayList<Ingredient> allRIngs;
    private String[] ings;
    private ArrayAdapter<String> adapterac;
    protected String selection;
    private InputMethodManager mgr;
    private int inType;
    private ImageButton bClearSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_show_list_layout);

        mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        ingredientDB = new IngredientsDB(this);
        cbtag = new ArrayList<String>();

        selectedIngredients = new ArrayList<Ingredient>();
        selectedIngredients.clear();
        checkedAll = false;

        listView = (ListView) this.findViewById(R.id.list);
        cbAll = (CheckBox) findViewById(R.id.cbAll);
        searchIng = (AutoCompleteTextView) findViewById(R.id.acIngredients);
        bSearch = (ImageButton) findViewById(R.id.bSearch);
        bClearSelection = (ImageButton) findViewById(R.id.bClearSelection);

        inType = searchIng.getInputType(); //backup input type for searchIng
        searchIng.setInputType(InputType.TYPE_NULL);
        searchIng.setVisibility(View.VISIBLE);
        searchIng.setOnClickListener(this);
        bSearch.setOnClickListener(this);
        bClearSelection.setOnClickListener(this);

        cbAll.setOnCheckedChangeListener(this);

        AddIngrAutoCompleteTV();

        setAdapter();

        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    private void AddIngrAutoCompleteTV() {

        allRIngs = ingredientDB.getAllIngredients();
        ings = new String[allRIngs.size()];

        for (int i = 0; i < allRIngs.size(); i++) {
            ings[i] = allRIngs.get(i).getIngreident().toString();
        }

        selection = "";
        adapterac = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, ings);

        searchIng.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long rowId) {
                selection = (String) parent.getItemAtPosition(pos);
                searchIng.setText(selection);
                searchIng.setSelection(searchIng.getText().length()); //Put the cursor on the end
            }
        });

        searchIng.setThreshold(1);
        searchIng.setAdapter(adapterac);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.acIngredients) {        //for keyboard showing
            searchIng.setTextIsSelectable(true);
            searchIng.requestFocus();
            searchIng.setInputType(inType);
            mgr.showSoftInput(searchIng, InputMethodManager.SHOW_FORCED); //show keyboard
        }

        if (v.getId() == R.id.bClearSelection) {
            searchIng.setText("");
            listOfShowedIngredients = new MyAdapterShowIngredients(this, searchIng.getText().toString(), ingredientDB, this, checkedAll);
            listView.setAdapter(listOfShowedIngredients);
            mgr.hideSoftInputFromWindow(bClearSelection.getWindowToken(), 0); //hide keyboard
        }

        if (v.getId() == R.id.bSearch) {
            listOfShowedIngredients = new MyAdapterShowIngredients(this, searchIng.getText().toString(), ingredientDB, this, checkedAll);
            listView.setAdapter(listOfShowedIngredients);
            mgr.hideSoftInputFromWindow(bSearch.getWindowToken(), 0);  //hide keyboard
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.add_delete_change_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_delete:

                ingredientDB.getDb().open();
                //preuzimanje cekiranih svojstava za brisanje
                cbtag.clear();

                boolean cannotdelete = false;

                if (checkedAll) {
                    recipePrepare = new RecipeIngredientsDB(this);

                    Log.d("delete all", Integer.toString(cbtag.size()));
                    ArrayList<Ingredient> ingredients = ingredientDB.getAllIngredients();
                    for (int i = 0; i < ingredients.size(); i++) {

                        if (recipePrepare.findPrepareByIds(ingredients.get(i).getIng_id())) {
                            cannotdelete = true;
                        } else {
                            ingredientDB.deleteIngredients(ingredients.get(i).getIng_id());
                            cannotdelete = false;
                        }
                    }


                } else {

                    myVeryOwnIterator = listOfShowedIngredients.getChecked().keySet().iterator();

                    while (myVeryOwnIterator.hasNext()) {
                        cbtag.add((String) myVeryOwnIterator.next());
                    }
                    //brisanje istih
                    recipePrepare = new RecipeIngredientsDB(this);
                    for (int i = 0; i < cbtag.size(); i++) {
                        if (recipePrepare.findPrepareByIds(cbtag.get(i).toString())) {
                            cannotdelete = true;
                        } else {
                            ingredientDB.deleteIngredients(cbtag.get(i));
                            cannotdelete = false;
                        }
                    }
                }


                if (cannotdelete) {
                    Toast.makeText(this, getResources().getString(R.string.deling), Toast.LENGTH_LONG).show();
                }

                setAdapter();
                return true;

            case R.id.menu_add:
                Intent addintent = new Intent(this, AddIngredient.class);
                addintent.putExtra("newIng", "addIngredient"); //use in AddIngredient, if create new
                startActivityForResult(addintent, 1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                setAdapter();
            }
        }
    }

    private void setAdapter() {
        searchIng.setText("");
        searchIng.setFocusable(true);
        listOfShowedIngredients = new MyAdapterShowIngredients(this, null, ingredientDB, this, checkedAll);
        listView.setAdapter(listOfShowedIngredients);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (ingredientDB.getDb() != null) {
            ingredientDB.getDb().close();
        }
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            checkedAll = true;
        } else {
            checkedAll = false;
        }

        setAdapter();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
