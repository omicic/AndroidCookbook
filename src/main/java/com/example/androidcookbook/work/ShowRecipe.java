package com.example.androidcookbook.work;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.androidcookbook.R;
import com.example.androidcookbook.mydb.IngredientsDB;
import com.example.androidcookbook.mydb.RecipeDB;
import com.example.androidcookbook.mydb.RecipeIngredientsDB;
import com.example.androidcookbook.object.Ingredient;
import com.example.androidcookbook.object.Recipe;
import com.example.androidcookbook.object.RecipePrepare;

import java.util.ArrayList;


public class ShowRecipe extends Activity implements OnClickListener {

    private Button bCancel;
    private Button bEdit;
    private TextView tvDirections;
    private String quantity;
    private TextView tvIngredient;
    private TextView tvQuantity;
    private IngredientsDB ingDB;
    private Ingredient ingredient;
    private ArrayList<Ingredient> ingredients;
    private TextView tvMu;
    private boolean isActivityRestarting;
    private ArrayList<String> quantities;
    private ArrayList<RecipePrepare> receptPreparelist;
    private TextView nameOfRecipe;
    private ScrollView sDirections;
    private Recipe recept;
    private Typeface tf;
    private ImageView ivRecipe;
    private String pathName;
    private TableLayout tl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_recipe);

        ingDB = new IngredientsDB(this);

        recept = new Recipe();
        ingredient = new Ingredient();
        recept = getIntent().getParcelableExtra("recipe"); //take from ShowListOfRecipe.class

        ingredients = new ArrayList<Ingredient>();
        quantities = new ArrayList<String>();
        receptPreparelist = new ArrayList<RecipePrepare>();

        nameOfRecipe = (TextView) findViewById(R.id.sr_nameOfrecipe);
        tl = (TableLayout) findViewById(R.id.sr_tableOfingredients);
        tvDirections = (TextView) findViewById(R.id.sr_directions);
        ivRecipe = (ImageView) findViewById(R.id.ivRecipe);
        ivRecipe.setRotation(90);

        nameOfRecipe.setText(recept.getRecipe().toString());
        tvDirections.setText(recept.getDescrtiption().toString());

        pathName = Environment.getExternalStorageDirectory() + "/recipeimage/" + recept.getId() + "recipe.jpg";

        if (pathName != null && pathName != "") {
            pathName = recept.getPicture();
        } else {
            //Log.d("pathnameShowrecipe", pathName);
        }

        if (!pathName.equals("No set image")) //<--CHECK FILENAME IS NOT NULL
        {
            Utilities utility = new Utilities();
            Bitmap bmp = utility.ShrinkBitmap(pathName, 120, 120);
            ivRecipe.setImageBitmap(bmp);
        }

        nameOfRecipe.setTypeface(tf);
        tvDirections.setTypeface(tf);
        tvDirections.setTextColor(Color.parseColor("#5C5C8A"));

        //use Parcelable object ArrayList<RecipePrepare>, from selected recipe in ShowListOfRecipe.class, getting RecipePrepare: _id, recipe_id, ing_id, ing_qu
        if (receptPreparelist.size() == 0) {
            receptPreparelist = getIntent().getExtras().getParcelableArrayList("receptIngredients");
        }

        ingDB.getDb().open();
        ivRecipe.setOnClickListener(this);
        ShowInTableIngredientForRecipe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.show_recipe_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_edit:

                try {
                    //za prosledjivanje ChangeRecipe za izmenu
                    Intent intentchange = new Intent(this, ChangeRecipe.class);
                    intentchange.putExtra("recipeforchange", recept);
                    intentchange.putStringArrayListExtra("quforchange", quantities); //uzeto iz tabele prepare za recipe_id
                    intentchange.putParcelableArrayListExtra("ingforchange", ingredients);    //uzeto iz baze recipe_id
                    startActivity(intentchange);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void ShowInTableIngredientForRecipe() {

        // da se ne bi duplirale namirnice
        tl.removeAllViews();
        ingredients.clear();
        quantities.clear();

        for (int i = 0; i < receptPreparelist.size(); i++) {        //for every ingredients get quantity, ingredient id
            quantity = receptPreparelist.get(i).getIng_qu().toString();
            quantities.add(quantity);

            ingredient = ingDB.getIngredientById(receptPreparelist.get(i).getIng_id().toString());    // getting <Ingredient> from database for every ing_id int recipeIngrList
            ingredients.add(ingredient);//create list of <Ingredient> for putExtras-it's used from Edit recipe throught starting AddRecipe activity

            addTextView(tl, ingredient.getIngreident().toString(), quantity, ingredient.getmMu().toString());    //show in table layout
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivRecipe:
                try {
                    //when click on ivRecipe open image in gallery
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);

                    Uri uri = Uri.parse("file://" + pathName);
                    intent.setDataAndType(uri, "image/*");
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void addTextView(TableLayout tl, String ingname,
                             String quantities, String mu) {

        TableRow row = new TableRow(this);

        tvIngredient = new TextView(this);
        tvQuantity = new TextView(this);
        tvMu = new TextView(this);

        tvIngredient.setText("- " + ingname + ", ");
        tvQuantity.setText(quantities + " ");
        tvMu.setText(mu);

        tvIngredient.setTextColor(Color.parseColor("#5C5C8A"));
        tvQuantity.setTextColor(Color.parseColor("#5C5C8A"));
        tvMu.setTextColor(Color.parseColor("#5C5C8A"));

        TableRow.LayoutParams lp = new TableRow.LayoutParams();

        row.addView(tvIngredient, lp);
        row.addView(tvQuantity, lp);
        row.addView(tvMu, lp);

        tl.addView(row);

    }


    @Override
    public boolean onSearchRequested() {
        //pauseSomeStuff();
        return super.onSearchRequested();
    }

    @Override
    protected void onResume() {
        super.onResume();

        pathName = "No set image";
        RecipeDB recdb = new RecipeDB(this);
        String recid = recept.getId().toString();

        recept = recdb.getRecepteById(recid);
        nameOfRecipe.setText(recept.getRecipe().toString());
        tvDirections.setText(recept.getDescrtiption().toString());

        if (!recept.getPicture().toString().equals("No set image")) {
            pathName = Environment.getExternalStorageDirectory() + "/recipeimage/" + recept.getId() + "recipe.jpg";
            Utilities utility = new Utilities();
            Bitmap bmp = utility.ShrinkBitmap(pathName, 120, 120);
            ivRecipe.setImageBitmap(bmp);
        }
        recdb.getDb().close();

        RecipeIngredientsDB recingdb = new RecipeIngredientsDB(this);

        recingdb.getDb().open();
        receptPreparelist.clear();
        receptPreparelist = recingdb.getAllIngredientsByRecipeId(recid);
        recingdb.getDb().close();
        ShowInTableIngredientForRecipe();

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (ingDB.getDb() != null) {
            ingDB.getDb().close();
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent showlistofrecipeintent = new Intent(this, ShowListOfRecipe.class);
        startActivity(showlistofrecipeintent);
        this.finish();
    }


}
