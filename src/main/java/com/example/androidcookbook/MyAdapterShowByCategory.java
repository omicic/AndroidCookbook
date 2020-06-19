package com.example.androidcookbook;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.androidcookbook.mydb.MenuDB;
import com.example.androidcookbook.object.Recipe;
import com.example.androidcookbook.object.RecipeMenu;
import com.example.androidcookbook.work.SetWeeklyMenu;

import java.util.ArrayList;
import java.util.HashMap;


public class MyAdapterShowByCategory extends BaseAdapter implements OnCheckedChangeListener {

    private LayoutInflater mInflater;
    private TextView set_menu_row_cell_name;
    private ArrayList<Recipe> searchRecipes;
    protected String selection;

    private HashMap<String, String> changedMenu;

    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private CheckBox checkBox5;
    private CheckBox checkBox6;
    private CheckBox checkBox7;

    private ImageButton set_menu_row_cell_row_show_recipe;
    private MenuDB menuDB;

    private ArrayList<String> rec_ids;
    private ArrayList<String> days;
    private ArrayList<String> chacked;
    private String d;
    private ArrayList<RecipeMenu> menuArray;
    private boolean mondaySet;
    private String mondaySetMenuId;
    private RecipeMenu menuToChange;

    public MyAdapterShowByCategory(SetWeeklyMenu weeklyMenu, ArrayList<Recipe> searchRecipes, ArrayList<RecipeMenu> searchMenu, String category) {
        mInflater = (LayoutInflater) weeklyMenu.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.searchRecipes = searchRecipes; //recepti koji su prikazani
        menuArray = new ArrayList<RecipeMenu>();
        menuToChange = new RecipeMenu();
        menuArray = searchMenu;        //menuDB.getAllMenu()
        menuDB = new MenuDB(weeklyMenu);

        //zapamti koji je cekrian
        for (int i = 0; i < menuArray.size(); i++) {
            for (int j = 0; j < searchRecipes.size(); j++) {
                if (menuArray.get(i).getRec_id().equals(searchRecipes.get(j).getId())) {

                    if (menuArray.get(i).getDay().equals("0")) {
                        if (menuArray.get(i).getCheck() == "true") {
                            Log.d("monday set", "set");
                            Log.d("monday recipe", menuArray.get(i).getMenu_id().toString());
                            mondaySet = true;
                            mondaySetMenuId = menuArray.get(i).getMenu_id().toString();
                        } else {
                            mondaySet = false;
                            mondaySetMenuId = "";
                        }
                    }

                }
            }
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            // row =  mInflater.inflate(R.layout.set_menu_row, parent, false);
        }

        // set_menu_row_cell_name = (TextView) row.findViewById(R.id.set_menu_row_cell_name);
        set_menu_row_cell_name.setText(searchRecipes.get(position).getRecipe().toString());

        checkBox1 = (CheckBox) row.findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) row.findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) row.findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox) row.findViewById(R.id.checkBox4);
        checkBox5 = (CheckBox) row.findViewById(R.id.checkBox5);
        checkBox6 = (CheckBox) row.findViewById(R.id.checkBox6);
        checkBox7 = (CheckBox) row.findViewById(R.id.checkBox7);

        checkBox1.setTag(searchRecipes.get(position).getId().toString());
        checkBox2.setTag(searchRecipes.get(position).getId().toString());
        checkBox3.setTag(searchRecipes.get(position).getId().toString());
        checkBox4.setTag(searchRecipes.get(position).getId().toString());
        checkBox5.setTag(searchRecipes.get(position).getId().toString());
        checkBox6.setTag(searchRecipes.get(position).getId().toString());
        checkBox7.setTag(searchRecipes.get(position).getId().toString());

        postaviCheckBoxove(searchRecipes.get(position).getId().toString());

        checkBox1.setOnCheckedChangeListener(this);
        checkBox2.setOnCheckedChangeListener(this);
        checkBox3.setOnCheckedChangeListener(this);
        checkBox4.setOnCheckedChangeListener(this);
        checkBox5.setOnCheckedChangeListener(this);
        checkBox6.setOnCheckedChangeListener(this);
        checkBox7.setOnCheckedChangeListener(this);

        //set_menu_row_cell_row_show_recipe = (ImageButton) row.findViewById(R.id.set_menu_row_cell_row_show_recipe);
        set_menu_row_cell_row_show_recipe.setTag(searchRecipes.get(position).getRecipe().toString());

        return (row);

    }

    private void postaviCheckBoxove(String rec_id) {

        if (menuArray.size() != 0) {

            for (int i = 0; i < menuArray.size(); i++) {
                if (menuArray.get(i).getRec_id().equals(rec_id)) {
                    switch (Integer.parseInt(menuArray.get(i).getDay().toString())) {
                        case 0:
                            checkBox1.setChecked(Boolean.parseBoolean(menuArray.get(i).getCheck()));
                            if (mondaySet && mondaySetMenuId != menuArray.get(i).getMenu_id().toString()) {
                                checkBox1.setEnabled(false);
                            } else {
                                checkBox1.setEnabled(true);
                            }

                            break;
                        case 1:
                            checkBox2.setChecked(Boolean.parseBoolean(menuArray.get(i).getCheck()));
                            break;
                        case 2:
                            checkBox3.setChecked(Boolean.parseBoolean(menuArray.get(i).getCheck()));
                            break;
                        case 3:
                            checkBox4.setChecked(Boolean.parseBoolean(menuArray.get(i).getCheck()));
                            break;
                        case 4:
                            checkBox5.setChecked(Boolean.parseBoolean(menuArray.get(i).getCheck()));
                            break;
                        case 5:
                            checkBox6.setChecked(Boolean.parseBoolean(menuArray.get(i).getCheck()));
                            break;
                        case 6:
                            checkBox7.setChecked(Boolean.parseBoolean(menuArray.get(i).getCheck()));
                            break;
                        default:

                            break;
                    }
                } else {
                    checkBox1.setChecked(false);
                    checkBox2.setChecked(false);
                    checkBox3.setChecked(false);
                    checkBox4.setChecked(false);
                    checkBox5.setChecked(false);
                    checkBox6.setChecked(false);
                    checkBox7.setChecked(false);
                }
            }
        }
//sta kada nemamo kombinaciju recid + day u menudb????
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.checkBox1:
                d = "0";
                save(buttonView.getTag().toString(), d, isChecked);
                break;

            case R.id.checkBox2:
                d = "1";
                save(buttonView.getTag().toString(), d, isChecked);
                this.notifyDataSetChanged();
                break;

            case R.id.checkBox3:
                d = "2";
                save(buttonView.getTag().toString(), d, isChecked);
                this.notifyDataSetChanged();
                break;

            case R.id.checkBox4:
                d = "3";
                save(buttonView.getTag().toString(), d, isChecked);
                this.notifyDataSetChanged();
                break;

            case R.id.checkBox5:
                d = "4";
                save(buttonView.getTag().toString(), d, isChecked);
                this.notifyDataSetChanged();
                break;

            case R.id.checkBox6:
                d = "5";
                save(buttonView.getTag().toString(), d, isChecked);
                this.notifyDataSetChanged();
                break;

            case R.id.checkBox7:
                d = "6";
                save(buttonView.getTag().toString(), d, isChecked);
                this.notifyDataSetChanged();
                break;

            default:
                break;
        }
    }


    private void save(String rec_id, String d, boolean isChecked) {

        menuToChange = menuDB.getMenuByRecId(rec_id, d);
        //Log.d("aa", menuToChange.getRec_id().toString());

        if (menuToChange.getMenu_id() == null) {
            menuDB.insertMenu(rec_id, d, Boolean.toString(isChecked), null);
        } else {
            menuDB.updateMenu(rec_id, d, Boolean.toString(isChecked), null);
        }

    }


    public ArrayList<String> getRec_ids() {
        return rec_ids;
    }

    public void setRec_ids(ArrayList<String> rec_ids) {
        this.rec_ids = rec_ids;
    }

    public ArrayList<String> getDays() {
        return days;
    }

    public void setDays(ArrayList<String> days) {
        this.days = days;
    }

    public ArrayList<String> getChacked() {
        return chacked;
    }

    public void setChacked(ArrayList<String> chacked) {
        this.chacked = chacked;
    }

    @Override
    public int getCount() {
        return searchRecipes.size();
    }

    @Override
    public Recipe getItem(int id) {
        return searchRecipes.get(id);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
