package com.example.androidcookbook.work;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import java.util.List;


public class AddRecipe extends AppCompatActivity implements OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 4;
    private static final int REQUEST_CODE = 5;
    private static final int SELECT_PICTURE = 3;
    private static final int GET_DIRECTION = 2;
    private static final int GET_INGREDIENTS = 1;

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
    private MenuDB dbMenu;
    private Typeface tf;
    private IngredientsDB ingredientsDB;
    private ArrayList<Ingredient> ingredientfromdb;
    private ArrayList<String> quantityfromdb;
    private ArrayList<RecipePrepare> svi;
    private int delete;
    private String new_recipe;
    private ImageView addPhotoBT;
    private byte[] resultPicture;
    private String selectedImagePath;
    private byte[] resultbyteimage;
    private Utilities utility;
    private Bitmap bitmap;
    private LinearLayout llimage;
    private LayoutParams params;
    private Intent data;
    private String lastid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipes);
        addItemsOnSpinnerKategorija();
        addListenerOnSpinnerItemSelection();

        data = new Intent();
        utility = new Utilities();

        resultIngredient = new ArrayList<Ingredient>();
        resultIngredientQu = new ArrayList<String>();
        selectedImagePath = "";
        tf = Typeface.createFromAsset(getAssets(), "fonts/quikhand.ttf");

        dbRecept = new RecipeDB(this);
        dbMenu = new MenuDB(this);
        RecipeIngredientsDB = new RecipeIngredientsDB(this);
        ingredientsDB = new IngredientsDB(this);
        svi = new ArrayList<RecipePrepare>();
        recipe = new Recipe();

        ingredientfromdb = new ArrayList<Ingredient>();
        quantityfromdb = new ArrayList<String>();

        recipeET = (EditText) findViewById(R.id.recipeET);
        recipeET.setTypeface(tf);

        spinnerKategorija = (Spinner) findViewById(R.id.spinnerKategorija);

        addIgredientsBT = (Button) findViewById(R.id.add_ingrediens_bt);

        //addIgredientsBT.setBackgroundResource(R.drawable.selector);
        addIgredientsBT.setTypeface(tf);
        addDirectionsBT = (Button) findViewById(R.id.add_direction_of_recipe);
        //addDirectionsBT.setBackgroundResource(R.drawable.selector);
        addDirectionsBT.setTypeface(tf);

        addPhotoBT = (ImageView) findViewById(R.id.add_photo_bt);
        addPhotoBT.setFocusable(false);
        //addPhotoBT.setRotation(90);

        llimage = (LinearLayout) findViewById(R.id.llspinnerimage);
        llimage.setGravity(Gravity.CENTER);
        params = (LayoutParams) llimage.getLayoutParams();

        registerForContextMenu(addPhotoBT);

        addIgredientsBT.setOnClickListener(this);
        addDirectionsBT.setOnClickListener(this);
        addPhotoBT.setOnClickListener(this);
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
                try {
                    //naziv recepta mora da bude unet inace nece biti dodat recept
                    if (!recipeET.getText().toString().equals("")) {
                        saveItToDB();
                    } else {
                        Toast.makeText(this.getBaseContext(), getResources().getString(R.string.toastemptyname), Toast.LENGTH_LONG).show();
                        this.finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;

            case R.id.menu_cancel:
                try {
                    this.finish();
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
                    Intent addIngIntent = new Intent(this,
                            AddIngredientToRecipe.class);

                    if (resultIngredient.size() != 0) {
                        //Log.d("resultIngredient", "nije 0");
                        addIngIntent.putParcelableArrayListExtra("corectentereding", resultIngredient);
                        addIngIntent.putStringArrayListExtra("corectenteredqu", resultIngredientQu);
                    }

                    startActivityForResult(addIngIntent, GET_INGREDIENTS);

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

                    if (resultDirections != null) {
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

                    Toast.makeText(this.getBaseContext(), getResources().getString(R.string.toastlongpress),
                            Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    // Results from AddIngredientToRecipe: ingredient and quantity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        this.data = data;
        if (requestCode == GET_INGREDIENTS) { //ingredients
            if (resultCode == RESULT_OK) {
                resultIngredient = data.getExtras().getParcelableArrayList("i");//from AddIngredientToRecipe
                resultIngredientQu = data.getExtras().getStringArrayList("q");//from AddIngredientToRecipe
            }
        }

        if (requestCode == GET_DIRECTION) { //direction
            if (resultCode == RESULT_OK) {
                resultDirections = data.getStringExtra("directions");
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE) { //CAPTURE IMAGE
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                selectedImagePath = "";
                Bitmap bitmap = (Bitmap) extras.get("data");

                Bitmap scaledBitmap = null;
                try {
                    scaledBitmap = utility.ShrinkBitmap(this, bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                addPhotoBT.setImageBitmap(scaledBitmap);
            }
        }

        if (requestCode == SELECT_PICTURE) { //from Gallery
            if (resultCode == RESULT_OK) {

                selectedImagePath = utility.getPath(this, data.getData());
                ContentResolver cr = this.getContentResolver();
                Bitmap bitmap=null;
                try {
                    bitmap = MediaStore.Images.Media
                            .getBitmap(cr, Uri.fromFile(new File(selectedImagePath)));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Bitmap scaledBitmap = utility.ShrinkBitmap(this, bitmap);
                    addPhotoBT.setImageBitmap(scaledBitmap);

                } catch (Exception e) {
                    Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }

    private void ShowImage() {

       /* Uri selectedImageUri = data.getData();
        selectedImagePath = utility.getPath(this, selectedImageUri);
        try {
            Bitmap scaledBitmap = utility.ShrinkBitmap(this, selectedImagePath);
            addPhotoBT.setImageBitmap(scaledBitmap);

        } catch (Exception e) {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                    .show();
        }*/
    }

    //ja bih ovo u Asynctask
    public void saveItToDB() {

        dbRecept.getDb().open();
        TakePicture(); //getting path of file /recipeimages/*recipeid.jpeg
        AddNewRecipe();

        startActivity(new Intent(this, ShowListOfRecipe.class));
        finish();
        dbRecept.getDb().close();
    }

    private void TakePicture() {

        int nextid;
        //if (selectedImagePath != "") {
            if (dbRecept.getLastId().toString() != "") {
                nextid = Integer.parseInt(dbRecept.getLastId()) + 1; //uzmi poslednji id recepta i dodaj 1
            } else {
                nextid = 1;
            }

            //ja bih ovo u AsyncTask

        //Log.d("heeeej", String.valueOf(this.data.getData().getPath()));
        //if(this.data.getData() == null){
        if(selectedImagePath == ""){
            selectedImagePath = utility.TakePicture(nextid);
        } else {
            selectedImagePath = utility.TakePicture(this, this.data, nextid);
        }



        //} else {
       //     selectedImagePath = getResources().getString(R.string.nosetimage);
       // }
    }

    private void saveRecipeIngredients() {

        for (int i = 0; i < resultIngredient.size(); i++) {
            //uradi update kolicine ako postoji kombinacija recipe_id i ing_id u tabeli prepare
            if (RecipeIngredientsDB.findPrepareByIds(rec_id.toString(), resultIngredient.get(i).getIng_id().toString())) {
                RecipeIngredientsDB.updateRecipeIngredients(rec_id.toString(),
                        resultIngredient.get(i).getIng_id().toString(), resultIngredientQu.get(i).toString());
                ;
            } else {
                //ili dodaj novi slog u tabelu prepare
                AddNewIngToPrepare(rec_id.toString(), resultIngredient.get(i).getIng_id().toString(), resultIngredientQu.get(i).toString());
            }
        }
    }

    private void AddNewRecipe() {

        Log.d("aaaaa", selectedImagePath); //ok
        if ("sr".equals(getString(R.string.lang))) {
            switch (kategorija) {
                case "Doru�ak":
                    kategorija = "Breakfast";
                    break;
                case "Ru�ak":
                    kategorija = "Lunch";
                    break;
                case "Ve�era":
                    kategorija = "Dinner";
                    break;
                case "U�ina":
                    kategorija = "Snack";
                    break;
                case "Salata":
                    kategorija = "Salad";
                    break;
                case "Dezert":
                    kategorija = "Dessert";
                    break;
            }
        }

        if (resultDirections == null) {
            dbRecept.insertRecept(recipeET.getText().toString(), kategorija, "", selectedImagePath.toString());
        } else {
            dbRecept.insertRecept(recipeET.getText().toString(), kategorija, resultDirections.toString(), selectedImagePath.toString());
        }

        //koristimo za punjenje tabele menija, postaljamo false za sve dane
        dbMenu.insertMenu(dbRecept.getLastId(), "0", "false", null);
        dbMenu.insertMenu(dbRecept.getLastId(), "1", "false", null);
        dbMenu.insertMenu(dbRecept.getLastId(), "2", "false", null);
        dbMenu.insertMenu(dbRecept.getLastId(), "3", "false", null);
        dbMenu.insertMenu(dbRecept.getLastId(), "4", "false", null);
        dbMenu.insertMenu(dbRecept.getLastId(), "5", "false", null);
        dbMenu.insertMenu(dbRecept.getLastId(), "6", "false", null);
        //dbMenu.getDb().close();

        autoInc = dbRecept.getLastId();//get last recipe_id
        rec_id = autoInc;

        for (int i = 0; i < resultIngredient.size(); i++) {
            RecipeIngredientsDB.insertRecipeIngredients(rec_id, resultIngredient.get(i).getIng_id().toString(), resultIngredientQu.get(i).toString());
        }
    }

    private void AddNewIngToPrepare(String rec_id, String ing_id, String qu) {
        RecipeIngredientsDB.insertRecipeIngredients(rec_id, ing_id, qu);
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

                        ((TextView) adapter.getChildAt(0)).setTextColor(Color.BLUE);

                        kategorija = (String) adapter
                                .getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapter) {
                        kategorija = "no category";
                    }

                });
    }


    /**
     * This will be invoked when an item is long pressed
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
                        startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
                    } else {
                        Toast.makeText(getBaseContext(), "Camera is not available!", Toast.LENGTH_LONG).show();
                    }
                }
                return true;

            case R.id.cnt_mnu_add_from_gallery:

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "Select..."), SELECT_PICTURE); //6
                return true;
        }

        return false;
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

        if (dbMenu.getDb() != null) {
            dbMenu.getDb().close();
        }

        if (ingredientsDB.getDb() != null) {
            ingredientsDB.getDb().close();
        }
    }
}
