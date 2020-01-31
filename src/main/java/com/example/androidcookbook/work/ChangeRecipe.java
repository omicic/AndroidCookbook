package com.example.androidcookbook.work;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidcookbook.R;
import com.example.androidcookbook.mydb.IngredientsDB;
import com.example.androidcookbook.mydb.MenuDB;
import com.example.androidcookbook.mydb.RecipeDB;
import com.example.androidcookbook.mydb.RecipeIngredientsDB;
import com.example.androidcookbook.object.Ingredient;
import com.example.androidcookbook.object.Recipe;
import com.example.androidcookbook.object.RecipePrepare;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ChangeRecipe extends AppCompatActivity implements OnClickListener {

    private static final int SELECT_PICTURE = 0;
    private static final int CAMERA_CAPTURE = 3;
    private static final int GET_INGREDIENT = 1;
    private static final int GET_DIRECTION = 2;
    private Button submitBT;
    private Button cancelBT;
    private Spinner spinnerKategorija;
    private String kategorija;
    private RecipeDB dbRecept;
    private Button addIgredientsBT;
    private Button addDirectionsBT;
    private EditText recipeET;
    private String autoInc;
    private com.example.androidcookbook.mydb.RecipeIngredientsDB RecipeIngredientsDB;
    private ArrayList<String> resultQuantity;
    private ArrayList<String> resultMu;
    private String resultDirections;
    private ArrayList<String> resultIngredientQu;
    private String rec_id;
    private ArrayList<String> resultIngForUpdate;
    private ArrayList<Ingredient> resultIngredient;
    private Recipe recipe;
    private TextView nameOfRecipe;
    private RecipeIngredientsDB dbRecipePrepare;
    private Typeface tf;
    private IngredientsDB ingredientsDB;
    private ArrayList<Ingredient> ingredientfromdb;
    private ArrayList<String> quantityfromdb;
    private ArrayList<RecipePrepare> svi;
    private int delete;
    private String new_recipe;
    private ImageView addPhotoBT;
    private String resultPicture;
    private String recipeKategorija;
    private String selectedImagePath;
    private String selectedNewImagePath;
    private LayoutParams params;
    private LinearLayout llimage;
    private Utilities utility;
    private Uri selectedNewImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipes);
        addItemsOnSpinnerKategorija();
        addListenerOnSpinnerItemSelection();

        tf = Typeface.createFromAsset(getAssets(), "fonts/quikhand.ttf");

        dbRecept = new RecipeDB(this);
        dbRecipePrepare = new RecipeIngredientsDB(this);
        RecipeIngredientsDB = new RecipeIngredientsDB(this);
        ingredientsDB = new IngredientsDB(this);

        utility = new Utilities();

        llimage = (LinearLayout) findViewById(R.id.llspinnerimage);
        llimage.setGravity(Gravity.CENTER);
        params = (LayoutParams) llimage.getLayoutParams();

        recipe = new Recipe();
        resultIngredient = new ArrayList<Ingredient>();
        resultIngredientQu = new ArrayList<String>();
        svi = new ArrayList<RecipePrepare>();

        recipe = getIntent().getExtras().getParcelable("recipeforchange"); //recid, recipe, category, description
        resultIngredient = getIntent().getExtras().getParcelableArrayList("ingforchange"); // svojstva za promenu

        resultIngredientQu = getIntent().getExtras().getStringArrayList("quforchange"); //kolicina za ta svojstva za promenu
        resultDirections = recipe.getDescrtiption().toString();
        recipeKategorija = recipe.getCategory().toString();

        spinnerKategorija = (Spinner) findViewById(R.id.spinnerKategorija);
        recipeET = (EditText) findViewById(R.id.recipeET);
        addIgredientsBT = (Button) findViewById(R.id.add_ingrediens_bt);
        addDirectionsBT = (Button) findViewById(R.id.add_direction_of_recipe);
        addPhotoBT = (ImageView) findViewById(R.id.add_photo_bt);
        addPhotoBT.setFocusable(false);
        //addPhotoBT.setRotation(90);

        addIgredientsBT.setText(getResources().getString(R.string.changeing));
        addDirectionsBT.setText(getResources().getString(R.string.changedir));

        String[] some_array = getResources().getStringArray(R.array.category_arrays);

        spinnerKategorija.setSelection(Arrays.asList(some_array).indexOf(recipeKategorija));

        //addIgredientsBT.setBackgroundResource(R.drawable.selector);
        //addDirectionsBT.setBackgroundResource(R.drawable.selector);

        recipeET.setTypeface(tf);
        addIgredientsBT.setTypeface(tf);
        addDirectionsBT.setTypeface(tf);

        recipeET.setText(recipe.getRecipe().toString());

        registerForContextMenu(addPhotoBT);

        String picture = recipe.getPicture().toString();
        if (!picture.equals("No set image")) {
            selectedImagePath = Environment.getExternalStorageDirectory() + "/recipeimage/" + recipe.getId().toString() + "recipe.jpg";
        } else {
            selectedImagePath = "No set image";
        }

        if (selectedImagePath.toString() != "No set image") {
            try {
                ShowImage(selectedImagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        addIgredientsBT.setOnClickListener(this);
        addDirectionsBT.setOnClickListener(this);
        addPhotoBT.setOnClickListener(this);
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
                    saveItToDB();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.add_ingrediens_bt:
                try {
                    Intent addIngIntent = new Intent(this, AddIngredientToRecipe.class);

                    if (resultIngredient.size() != 0) { //koristi se u slucaju lokalnih izmena u okviru aktivnosti
                        Log.d("resultIngredient", "nije 0");
                        addIngIntent.putParcelableArrayListExtra("corectentereding", resultIngredient);

                        addIngIntent.putStringArrayListExtra("corectenteredqu", resultIngredientQu);
                    } else {
                        addIngIntent.putParcelableArrayListExtra("corectentereding", null);
                        addIngIntent.putStringArrayListExtra("corectenteredqu", null);
                    }

                    startActivityForResult(addIngIntent, GET_INGREDIENT);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.add_direction_of_recipe:
                try {
                    Intent addDirIntent = new Intent(this, AddDirectionsToRecipe.class);
                    addDirIntent.putParcelableArrayListExtra("ingredients", resultIngredient); //koristi se za AddDirectionsToRecipe
                    addDirIntent.putStringArrayListExtra("quantities", resultIngredientQu);
                    addDirIntent.putExtra("rec_id", rec_id);
                    addDirIntent.putExtra("nameOfrecipe", recipeET.getText().toString());

                    if (resultDirections != null) {//koristi se u slucaju lokalnih izmena u okviru aktivnosti
                        //Log.d("resultDirections",Integer.toString(resultDirections.length()));
                        addDirIntent.putExtra("coretctingdescr", resultDirections);
                    } else {
                        addDirIntent.putExtra("coretctingdescr", "");
                    }

                    startActivityForResult(addDirIntent, GET_DIRECTION);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.add_photo_bt:
                try {

                    Toast.makeText(this.getBaseContext(), getResources().getString(R.string.nosetimage),
                            Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    // Results from AddIngredientToRecipe: ingredient and quantity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_INGREDIENT) { //change ingredients
            if (resultCode == RESULT_OK) {
                resultIngredient.clear();
                resultIngredientQu.clear();
                resultIngredient = data.getExtras().getParcelableArrayList("i");//from ChangeIngredient tj. AddIngredientToRecipe.class
                resultIngredientQu = data.getExtras().getStringArrayList("q");//from ChangeIngredient
            }
        }

        if (requestCode == GET_DIRECTION) {
            if (resultCode == RESULT_OK) {
                resultDirections = data.getStringExtra("directions");
            }
        }

        if (requestCode == CAMERA_CAPTURE) {
            if (resultCode == RESULT_OK) {
                selectedNewImageUri = data.getData();
                selectedNewImagePath = utility.getPath(this, selectedNewImageUri);
                try {
                    ShowImage(selectedNewImagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == SELECT_PICTURE) {
            if (resultCode == RESULT_OK) {
                selectedNewImageUri = data.getData();
                selectedNewImagePath = utility.getPath(this, selectedNewImageUri);
                try {
                    ShowImage(selectedNewImagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    private void ShowImage(String selectedImagePath) throws IOException {

        if (selectedImagePath != "No set image") { //if not empty
           Bitmap bitmap = utility.ShrinkBitmap(this,selectedImagePath);

            //params.height = bitmap.getHeight();
            //params.width = bitmap.getWidth();
            addPhotoBT.setImageBitmap(bitmap);
        } else {

        }
    }

    private void TakeNewPicture() {

        if (selectedImagePath == "No set image") {//if create new image

            selectedImagePath = Environment.getExternalStorageDirectory() + "/recipeimage/" + recipe.getId() + "recipe.jpg";
            File from = new File(selectedNewImagePath);
            File newImage = new File(selectedImagePath);
            from.renameTo(newImage);

        } else {
            File from = new File(selectedNewImagePath);
            File to = new File(selectedImagePath);
            from.renameTo(to);
        }
    }

    /**
     * This will be invoked when an item in the listview is long pressed
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.photos_action, menu);
    }

    /**
     * This will be invoked when a menu item is selected
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {

            case R.id.cnt_mnu_capture:
                PackageManager pm = getPackageManager();
                if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    if (i.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(i, CAMERA_CAPTURE);
                    } else {
                        Toast.makeText(getBaseContext(), "Camera is not available", Toast.LENGTH_LONG).show();
                    }
                }
                return true;

            case R.id.cnt_mnu_add_from_gallery:

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "Select..."), SELECT_PICTURE); //0
                return true;
        }
        return true;
    }

    private void ChangeRecipe() {

        if (selectedNewImagePath != null) {
            TakeNewPicture();
        }

        if (!kategorija.equals(recipe.getCategory().toString())) { //postavljanje false u dbMeniju, jer je do�lo do promene kategorije

            MenuDB dbMenu = new MenuDB(this);
            dbMenu.updateMenu(recipe.getId().toString(), false);
            dbMenu.getDb().close();
        }

        dbRecept.getDb().open();

        dbRecept.updateRecept(recipe.getId().toString(), recipeET.getText().toString(), kategorija, resultDirections.toString(), selectedImagePath);

        //koristimo za punjenje tabele menija, postaljamo false za sve dane
        dbRecipePrepare.removeAllForRec_id(recipe.getId().toString());

        for (int i = 0; i < resultIngredient.size(); i++) {
            Log.d("ing" + i, resultIngredient.get(i).getIng_id().toString());
            dbRecipePrepare.insertRecipeIngredients(recipe.getId().toString(), resultIngredient.get(i).getIng_id().toString(), resultIngredientQu.get(i).toString());
        }
        //resultIngredient.clear();
        //resultIngredientQu.clear();

    }

    // dodavanje items u spinner dinamicki
    private void addItemsOnSpinnerKategorija() {
        spinnerKategorija = (Spinner) findViewById(R.id.spinnerKategorija);
        List<String> listKategorija = new ArrayList<String>();
        listKategorija.add("");
    }

    private void addListenerOnSpinnerItemSelection() {
        spinnerKategorija
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapter,
                                               View view, int position, long id) {
                        kategorija = (String) adapter
                                .getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapter) {
                        kategorija = "no category";
                    }

                });
    }


    public void saveItToDB() {
        ChangeRecipe();
        //Intent showrecipe = new Intent(this, )
        this.finish();
    }


    public void onCancel(View v) {
        this.finish();
        dbRecept.getDb().close();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (dbRecept.getDb() != null) {
            dbRecept.getDb().close();
        }
        if (RecipeIngredientsDB.getDb() != null) {
            RecipeIngredientsDB.getDb().close();
        }
        if (dbRecipePrepare.getDb() != null) {
            dbRecipePrepare.getDb().close();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //Log.d("Resume","Change resume");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
