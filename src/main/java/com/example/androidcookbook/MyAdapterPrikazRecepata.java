package com.example.androidcookbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidcookbook.mydb.RecipeDB;
import com.example.androidcookbook.mydb.RecipeIngredientsDB;
import com.example.androidcookbook.object.Recipe;
import com.example.androidcookbook.object.RecipePrepare;
import com.example.androidcookbook.work.ShowListOfRecipe;
import com.example.androidcookbook.work.ShowRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


public class MyAdapterPrikazRecepata extends BaseAdapter implements OnCheckedChangeListener, OnClickListener {

    private TextView naziv;
    ;
    private ArrayList<Recipe> receptAL;
    private LayoutInflater mInflater;
    private RecipeDB dbRecept;
    private CheckBox checkbox;
    public HashMap<String, Boolean> checked;
    private ImageButton bEdit;
    private String deleteRecipe;
    private String bClicked;
    private Context context;
    private Recipe recept;
    private RecipeIngredientsDB recIngDB;
    private RecipeDB recipeDB;
    private Intent i;

    private ArrayList<String> frRecids;
    private ArrayList<String> numberOfingr;

    private int j;
    private ImageButton bCompare;
    private ArrayList<String> recipeids;
    private ArrayList<String> ingredients;
    private ArrayList<String> diffques;
    private ArrayList<String> ingfornote;
    private ArrayList<String> qufornote;
    private boolean checkedAll;
    private HashMap<String, String> recipeidforfindRecipes;

    public MyAdapterPrikazRecepata() {
        super();
    }

    //konstruktor koji se poziva za prikaz recepata iz FindRecipes, po kritrerijumu svojstava
    public MyAdapterPrikazRecepata(ShowListOfRecipe showListOfRecipe,
                                   String stringExtra,
                                   boolean emtylistforsearch,
                                   HashMap<String, String> recipeidforfindRecipes,
                                   ArrayList<String> recipeids,
                                   ArrayList<String> ingredients,
                                   ArrayList<String> diffques,
                                   RecipeDB receptDB,
                                   boolean checkedAll) {

        mInflater = (LayoutInflater) showListOfRecipe.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        checked = new HashMap<String, Boolean>();
        ingfornote = new ArrayList<String>();
        qufornote = new ArrayList<String>();
        checked.clear();

        this.context = showListOfRecipe;
        this.recipeidforfindRecipes = recipeidforfindRecipes;
        this.recipeids = recipeids;
        this.ingredients = ingredients;
        this.diffques = diffques;
        this.dbRecept = receptDB;
        this.checkedAll = checkedAll;

        //receptAL niz koji sadrzi recepte koji su rezultat pretrage
        if (receptAL == null) {
            receptAL = new ArrayList<Recipe>();
        } else {
            receptAL.clear();
        }

        if (!emtylistforsearch) {
            if (stringExtra == null && recipeidforfindRecipes != null) {
                getDataForFindRecipes(); //pretraga recepata po zadatim namirnicama, iz FindRecipes.class
            } else {
                getDataCategory(null); //pretraga po zadatom nazivu recepta, ili zadatoj kategoriji:salata ili dezert za ShowWeeklyMenu.class
            }
        } else {
            getDataCategory(null);
        }
    }

    //konstruktor koji se poziva iz klase ShowListOfRecipe, prikazivanje odabranog recepta kroz pretragu
    public MyAdapterPrikazRecepata(ShowListOfRecipe showListOfRecipes,
                                   String stringExtra,
                                   String category,
                                   RecipeDB recDb,
                                   boolean checkedAll) {

        mInflater = (LayoutInflater) showListOfRecipes.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checked = new HashMap<String, Boolean>();

        this.context = showListOfRecipes;
        this.dbRecept = recDb;
        this.checkedAll = checkedAll;

        checked.clear();

        if (receptAL == null) {
            receptAL = new ArrayList<Recipe>();
        } else {
            receptAL.clear();
        }

        getDataCategory(category);

    }

    public MyAdapterPrikazRecepata(ShowListOfRecipe showListOfRecipe,
                                   String cat, RecipeDB receptDB) {

        mInflater = (LayoutInflater) showListOfRecipe.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = showListOfRecipe;
        this.dbRecept = receptDB;
        checked = new HashMap<String, Boolean>();
        checked.clear();
        ingfornote = new ArrayList<String>();
        qufornote = new ArrayList<String>();

        //receptAL niz koji sadrzi recepte koji su rezultat pretrage
        if (receptAL == null) {
            receptAL = new ArrayList<Recipe>();
        } else {
            receptAL.clear();
        }

        getData(cat);

    }

    private void getData(String name) {

        if (name!="") {
            receptAL = (ArrayList<Recipe>) dbRecept.getAllRecepteByNaziv(name);    //Ako pretra�uje
        } else {
            receptAL = (ArrayList<Recipe>) dbRecept.getAllRecepte();    //Ako prikazuje sve recepte
        }
    }

    //for FindRecipes.class
    public void getDataForFindRecipes() {

        //use for sort
        recipeidforfindRecipes = (HashMap<String, String>) ValueComparator.sortByValue(recipeidforfindRecipes);
        if (recipeidforfindRecipes == null) {
            //Log.d("prazna","prikazi sve recepte");
        } else {

            frRecids = new ArrayList<String>();
            numberOfingr = new ArrayList<String>();

            Iterator<Entry<String, String>> myVOwnIterator = recipeidforfindRecipes.entrySet().iterator();
            while (myVOwnIterator.hasNext()) {
                Entry<String, String> entry = myVOwnIterator.next();
                frRecids.add(entry.getKey().toString());
                numberOfingr.add(entry.getValue().toString());
            }

            //sortirano je od manjeg broja podudaranja ka vecem i zbog toga prikazujemo u obrnutom redosledu
            j = frRecids.size(); //od 1 se racuna
            while (j > 0) {
                //zato sto se j racuna od 1 a frRecids array od 0;
                receptAL.add(dbRecept.getRecepteById(frRecids.get(j - 1).toString()));
                j--;
            }
        }
    }

    private void getDataCategory(String category) {

        if(category==null || category.equals("All") || category.equals("")){
            receptAL = (ArrayList<Recipe>) dbRecept.getAllRecepte();    //Ako prikazuje sve recepte
        } else {
            receptAL = (ArrayList<Recipe>) dbRecept.getAllRecipeByCategory(category);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            row = mInflater.inflate(R.layout.row_for_show, parent, false);
        }

        checkbox = (CheckBox) row.findViewById(R.id.cb_row_show);
        checkbox.setTag(receptAL.get(position).getId().toString());

        if (checkedAll) { //cekirano sve za brisanje
            checkbox.setChecked(true);
            checked.put(checkbox.getTag().toString(), true);
        } else {
            checkbox.setChecked(false);
        }

        checkbox.setOnCheckedChangeListener(this);

        naziv = (TextView) row.findViewById(R.id.cell_name);
        naziv.setText(receptAL.get(position).getRecipe().toString());

        bCompare = (ImageButton) row.findViewById(R.id.cell_row_compare);
        bCompare.setTag(receptAL.get(position).getId().toString());
        bCompare.setOnClickListener(this);


        bEdit = (ImageButton) row.findViewById(R.id.cell_row_edit);
        bEdit.setTag(receptAL.get(position).getId().toString());
        bEdit.setOnClickListener(this);

        return (row);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.cb_row_show) {
            checked.put(buttonView.getTag().toString(), isChecked);
        }
    }

    @Override
    public void onClick(View v) { //edit

        bClicked = v.getTag().toString();
        recipeDB = new RecipeDB(this.context);
        recIngDB = new RecipeIngredientsDB(this.context);
        recipeDB.getDb().open();
        recept = new Recipe();
        recept = recipeDB.getRecepteById(bClicked.toString());
        recipeDB.getDb().close();

        switch (v.getId()) {
            case R.id.cell_row_edit:

                // getting RecipePrepare _id,recipe_id,ing_id,ing_qu
                ArrayList<RecipePrepare> recept_ingredients = new ArrayList<RecipePrepare>();
                recept_ingredients = (ArrayList<RecipePrepare>) recIngDB.getAllIngredientsByRecipeId(bClicked.toString());

                i = new Intent(this.context, ShowRecipe.class);
                i.putExtra("recipe", (Recipe) recept); //Odabrani recept, za prikazivanje u ShowRecipe
                i.putParcelableArrayListExtra("receptIngredients", recept_ingredients);

                if (recipeidforfindRecipes != null) {
                    i.putExtra("editFromFindRecipe", "true");
                }

                this.context.startActivity(i);
                ((Activity) this.context).finish();
                return;

            case R.id.cell_row_compare: //za kreiranje stickija, poziva MakeNote.class

                if (recipeids != null) { //from FindRecipe
                    ingfornote.clear();
                    qufornote.clear();
                    for (int j = 0; j < recipeids.size(); j++) {
                        if (recipeids.get(j).toString().equals(bClicked.toString())) {

                            ingfornote.add(ingredients.get(j).toString());
                            qufornote.add(diffques.get(j).toString());
                        }
                    }

                    Intent noteIntent = new Intent(this.context, MakeNote.class);
                    noteIntent.putExtra("rec_id", bClicked.toString());
                    noteIntent.putStringArrayListExtra("ingredientstobuy", ingfornote);
                    noteIntent.putExtra("quantitiestobuy", qufornote);
                    this.context.startActivity(noteIntent);
                    ((Activity) this.context).finish();

                } else {
                    //Log.d("bClicked.toString()",bClicked.toString());
                    ingfornote.clear();
                    qufornote.clear();
                    recIngDB.getDb().open();
                    ArrayList<RecipePrepare> rpal = new ArrayList<RecipePrepare>();
                    rpal = recIngDB.getAllIngredientsByRecipeId(bClicked.toString());
                    //Log.d("rpal size",Integer.toString(rpal.size()));
                    recIngDB.getDb().close();
                    if (rpal.size() != 0) {
                        for (int i = 0; i < rpal.size(); i++) {
                            ingfornote.add(rpal.get(i).getIng_id());
                            qufornote.add(rpal.get(i).getIng_qu());
                        }

                        Intent noteIntent = new Intent(this.context, MakeNote.class);
                        noteIntent.putExtra("rec_id", bClicked.toString());
                        noteIntent.putStringArrayListExtra("ingredientstobuy", ingfornote);
                        noteIntent.putExtra("quantitiestobuy", qufornote);
                        this.context.startActivity(noteIntent);
                        //((Activity) this.context).finish();
                    } else { //Recept nema svojstva
                        Toast.makeText(this.context, context.getString(R.string.toastnorecipes), Toast.LENGTH_LONG).show();
                    }
                }

                return;
            default:
                break;
        }
    }

    public String getbEditClicked() {
        return bClicked;
    }

    public void setbEditClicked(String bClicked) {
        this.bClicked = bClicked;
    }


    public String getDeleteRecipe() {
        return deleteRecipe;
    }

    public void setDeleteRecipe(String deleteRecipe) {
        this.deleteRecipe = deleteRecipe;
    }

    public HashMap<String, Boolean> getChecked() {
        return checked;
    }

    public void setChecked(HashMap<String, Boolean> checked) {
        this.checked = checked;
    }

    @Override
    public int getCount() {
        return receptAL.size();
    }

    @Override
    public Recipe getItem(int id) {
        return receptAL.get(id);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
