package com.example.androidcookbook.work;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidcookbook.R;
import com.example.androidcookbook.mydb.IngredientsDB;
import com.example.androidcookbook.object.Ingredient;

public class AddIngredient extends Activity {

    private Button buttonAddIng;
    private EditText nameIngredientsET;
    private EditText muET;
    private Button buttonCancelIng;
    private IngredientsDB ingredientsDB;
    private Ingredient ing;
    private EditText kcalET;
    private Intent Intent;
    private Button buttonClose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_ingredients);

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/quikhand.ttf");

        ingredientsDB = new IngredientsDB(this);

        muET = (EditText) findViewById(R.id.muE);
        nameIngredientsET = (EditText) findViewById(R.id.name_ingredienteE);
        kcalET = (EditText) findViewById(R.id.etKcal);
        //kcalET.setVisibility(0);

        buttonAddIng = (Button) findViewById(R.id.add_ingredients);
        buttonClose = (Button) findViewById(R.id.close_ingredients);
        ing = new Ingredient();

        if (getIntent().getExtras().getString("newIng") == null) { //ako se radi o izmeni namirnice
            ing = (Ingredient) getIntent().getExtras().getParcelable("ing");
            nameIngredientsET.setText(ing.getIngreident().toString());
            muET.setText(ing.getmMu().toString());
            buttonAddIng.setText("Change");
        }

        buttonAddIng.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    saveIngredientsToDB();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buttonClose.setOnClickListener(new OnClickListener() {


            private android.content.Intent ii;

            public void onClick(View v) {

                if (getIntent().getExtras().getParcelable("ing") != null) {
                    startIntent();
                } else {
                    finish();
                }
            }

        });

    }

    private void startIntent() {

        if (getIntent().getExtras().getParcelable("ing") != null) {
            Intent i = new Intent(this, ShowIngredients.class);
            startActivity(i);
            finish();
        } else {
            finish();
        }

    }

    private void saveIngredientsToDB() {

        if (getIntent().getExtras().getParcelable("ing") == null) {
            try {
                ingredientsDB.insertIngredient(nameIngredientsET.getText().toString(), muET.getText().toString(), kcalET.getText().toString());
            } catch (SQLiteConstraintException e) {
                if (e.getMessage() == "-1") {
                    Toast.makeText(this, getResources().getString(R.string.ingexist), Toast.LENGTH_LONG).show();
                }
            }

            nameIngredientsET.setText("");
            muET.setText("");
            kcalET.setText("0");
            nameIngredientsET.requestFocus();

        } else {
            ingredientsDB.updateIngredient(ing.getIng_id().toString(), nameIngredientsET.getText().toString(), muET.getText().toString(), kcalET.getText().toString());
            Intent i = new Intent(this, ShowIngredients.class);
            startActivity(i);
            finish();
        }
    }

    public void onCancel(View v) {

        if (ingredientsDB.getDb() != null) {
            ingredientsDB.getDb().close();
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        if (ingredientsDB.getDb() != null) {
            ingredientsDB.getDb().close();
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().getExtras().getParcelable("ing") != null) {
            Intent i = new Intent(this, ShowIngredients.class);
            startActivity(i);
            finish();
        } else {
            finish();
        }
    }

}
