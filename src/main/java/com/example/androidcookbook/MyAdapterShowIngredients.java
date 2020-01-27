package com.example.androidcookbook;

import android.content.Context;
import android.content.Intent;
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

import com.example.androidcookbook.mydb.IngredientsDB;
import com.example.androidcookbook.object.Ingredient;
import com.example.androidcookbook.work.AddIngredient;
import com.example.androidcookbook.work.ShowIngredients;

import java.util.ArrayList;
import java.util.HashMap;


public class MyAdapterShowIngredients extends BaseAdapter implements OnCheckedChangeListener, OnClickListener {

    private TextView naziv;
    ;
    private LayoutInflater mInflater;
    private com.example.androidcookbook.mydb.IngredientsDB dbIng;
    private CheckBox checkbox;
    public HashMap<String, Boolean> checked;
    private ImageButton bEdit;
    private String deleteRecipe;
    private String bEditClicked;
    private Context context;
    public Intent i;
    private ArrayList<Ingredient> ingAL;
    private IngredientsDB IngredientsDB;
    public Ingredient ing;
    private ImageButton bCompare;
    private boolean checkedAll;

    public MyAdapterShowIngredients(ShowIngredients showListOfIng,
                                    String stringExtra, IngredientsDB ingDb, Context context, boolean checkedAll) {

        mInflater = (LayoutInflater) showListOfIng.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checked = new HashMap<String, Boolean>();

        this.dbIng = ingDb;
        this.context = context;
        this.checkedAll = checkedAll;

        checked.clear();
        getData(stringExtra);

    }

    private void getData(String stringExtra) {
        if (ingAL == null) {
            ingAL = new ArrayList<Ingredient>();
        }

        ingAL.clear();

        if (stringExtra != null) {
            ingAL = (ArrayList<Ingredient>) dbIng.getIngsByName(stringExtra);
        } //Ako pretra�uje
        else {
            ingAL = (ArrayList<Ingredient>) dbIng.getAllIngredients();    //Ako prikazuje sva svojstva
        }
    }

    @Override
    public int getCount() {
        return ingAL.size();
    }

    @Override
    public Ingredient getItem(int id) {
        return ingAL.get(id);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //this.position = position;
        View row = convertView;
        if (row == null) {
            row = mInflater.inflate(R.layout.row_for_show, parent, false);
        }

        checkbox = (CheckBox) row.findViewById(R.id.cb_row_show);
        checkbox.setTag(ingAL.get(position).getIng_id().toString());
        if (checkedAll) { //cekirano sve za brisanje
            checkbox.setChecked(true);
            checked.put(checkbox.getTag().toString(), true);
        } else {
            checkbox.setChecked(false);
        }

        checkbox.setOnCheckedChangeListener(this);

        naziv = (TextView) row.findViewById(R.id.cell_name);
        naziv.setText(ingAL.get(position).getIngreident().toString());

        bCompare = (ImageButton) row.findViewById(R.id.cell_row_compare);
        bCompare.setVisibility(View.INVISIBLE);

        bEdit = (ImageButton) row.findViewById(R.id.cell_row_edit);
        bEdit.setTag(ingAL.get(position).getIng_id().toString());
        bEdit.setOnClickListener(this);

        return (row);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView.getId() == R.id.cb_row_show) {

            if (isChecked) {
                checked.put(buttonView.getTag().toString(), isChecked);
            } else {
                checked.put(buttonView.getTag().toString(), !isChecked);
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.cell_row_edit) {
            bEditClicked = v.getTag().toString();

            IngredientsDB = new IngredientsDB(this.context);

            IngredientsDB.getDb().open();

            // geting recipe_id
            ing = new Ingredient();
            ing = IngredientsDB.getIngredientById(bEditClicked.toString());

            i = new Intent(this.context, AddIngredient.class);
            i.putExtra("ing", ing); //Odabrani recept, za prikazivanje u ShowRecipe
            this.context.startActivity(i);

        }
    }


    public Ingredient getIng() {
        return ing;
    }

    public void setIng(Ingredient ing) {
        this.ing = ing;
    }

    public String getbEditClicked() {
        return bEditClicked;
    }

    public void setbEditClicked(String bEditClicked) {
        this.bEditClicked = bEditClicked;
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
}
