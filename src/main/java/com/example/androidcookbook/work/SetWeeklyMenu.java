package com.example.androidcookbook.work;

/*Kategorija mo�e imati samo jedno jelo za odre�eni dan u nedelji.
 * Npr. jelo broj 1 mo�e biti doru�ak u ponedeljak. Nijedno drugo jelo ne mo�e biti ozna�eno koa
 * doru�ak za ponedeljak. �ekbox za ponedeljak jela broj 1 je ozna�eno, sva ostala jela imaju
 * disable �ekboksove. Ako nijedno nije ozna�eno svi �ekboxovi za ponedeljak su omogu�eni.
 * Interaktivne promene se ne snimaju u bazu. Snima se samo prilikom klika na dugme Save.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidcookbook.R;
import com.example.androidcookbook.mydb.MenuDB;
import com.example.androidcookbook.mydb.RecipeDB;
import com.example.androidcookbook.object.Recipe;
import com.example.androidcookbook.object.RecipeMenu;

import java.util.ArrayList;
import java.util.List;


public class SetWeeklyMenu extends AppCompatActivity implements OnItemSelectedListener, OnClickListener, OnCheckedChangeListener {

    private static String[] ColorForRow = null;
    private RecipeDB dbRecept;
    private MenuDB dbMenu;

    private ArrayList<Recipe> searchRecipes; //za prikazivanje po zadatom kriterijumu
    private ArrayList<String> rec_ids; //za pripremu za upis u bazu podataka, interaktivno pracenje
    private ArrayList<String> days;//za pripremu za upis u bazu podataka, interaktivno pracenje
    private ArrayList<Boolean> checked;    //za pripremu za upis u bazu podataka, interaktivno pracenje
    private ArrayList<RecipeMenu> allMenu; //za iscitavanje tabele dbMenu

    private Spinner searchSpinner;
    private TableLayout tableview;
    private CheckBox chMon;
    private CheckBox chTue;
    private CheckBox chWen;
    private CheckBox chTur;
    private CheckBox chFri;
    private CheckBox chSat;
    private CheckBox chSun;

    private String category;//trenutna zadata kategorija: dorucak, rucak....

    private int positionmon;//indikatori interaktivne izmene checkboxova.
    private int positiontue;
    private int positionwen;
    private int positiontur;
    private int positionfry;
    private int positionsat;
    private int positionsun;

    private Boolean checkmonday; //indikatori cekiranih po danima
    private Boolean checkTuesday;
    private Boolean checkWednesday;
    private Boolean checkThursday;
    private Boolean checkFriday;
    private Boolean checkSaturday;
    private Boolean checkSunday;

    private Typeface tf;
    protected String queryy; //za pretragu
    protected String selection;//from autocomplitetextview
    private AutoCompleteTextView acRecipe;
    private RecipeDB dbRecipes;
    private List<Recipe> allRecipes;
    private String[] recipes;
    private ImageButton bSearch;
    private TableRow tRow;
    private TableRow tRowCheckDay;
    private TableRow tRowTextDay;
    private String[] hiderowintable;
    private LayoutParams lp;
    private TableLayout tableviewrecipe;
    private TableRow tRowShowRecipe;
    private int height;
    private int width;
    private String addingmeal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.set_menu);
        tf = Typeface.createFromAsset(getAssets(), "fonts/quikhand.ttf");

        ColorForRow = new String[]{"#b1d2b0", "#cedbbc", "#eee5b3", "#e3d2c0", "#e7b59e", "#b1d2b0", "#cedbbc"};

        dbMenu = new MenuDB(this);
        dbRecipes = new RecipeDB(this);
        allRecipes = dbRecipes.getAllRecepte();
        allMenu = dbMenu.getAllMenu();

        rec_ids = new ArrayList<String>();
        days = new ArrayList<String>();
        checked = new ArrayList<Boolean>();
        searchRecipes = new ArrayList<Recipe>();
        selection = "";

        for (int i = 0; i < allMenu.size(); i++) {
            rec_ids.add(allMenu.get(i).getRec_id().toString());
            days.add(allMenu.get(i).getDay().toString());
            checked.add(Boolean.parseBoolean(allMenu.get(i).getCheck().toString()));
        }

        tableview = (TableLayout) findViewById(R.id.tlForShowingListOfRecipe);
        acRecipe = (AutoCompleteTextView) findViewById(R.id.acRecipe);

        AddIngrAutoCompleteTV();
        bSearch = (ImageButton) findViewById(R.id.bSearch);

        searchSpinner = (Spinner) findViewById(R.id.searchspinner);
        ArrayAdapter<CharSequence> adapterspinner = ArrayAdapter.createFromResource(
                this, R.array.category_arrays, android.R.layout.simple_spinner_item);


        adapterspinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(adapterspinner);
        searchSpinner.setOnItemSelectedListener(this);
        bSearch.setOnClickListener(this);
    }

    private void AddIngrAutoCompleteTV() {

        recipes = new String[allRecipes.size()];
        for (int i = 0; i < allRecipes.size(); i++) {
            recipes[i] = allRecipes.get(i).getRecipe().toString();
        }

        ArrayAdapter<String> adapterac = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, recipes);

        acRecipe.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long rowId) {
                selection = (String) parent.getItemAtPosition(pos);
                acRecipe.setText(selection);
            }
        });

        acRecipe.setThreshold(1);
        acRecipe.setAdapter(adapterac);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapter, View v, int pos, //Sppiner
                               long arg3) {
        hiderowintable = null; //prikazivanje svih recepata iz zadate kategorije
        category = adapter.getItemAtPosition(pos).toString();

        if ("sr".equals(getString(R.string.lang))) {
            switch (category) {
                case "Doru�ak":
                    category = "Breakfast";
                    break;
                case "Ru�ak":
                    category = "Lunch";
                    break;
                case "Ve�era":
                    category = "Dinner";
                    break;
                case "U�ina":
                    category = "Snack";
                    break;
            }
        }


        searchRecipes.clear();
        searchRecipes = dbRecipes.getAllRecipeByCategory(category);
        DrawTable();
    }


    private void DrawTable() {
        acRecipe.setText("");
        tableview.removeAllViews();

        //indikatori interaktivne izmene checkboxova. Nijedan nije cekiran za grupe boxova mon,tue,wen...
        positionmon = -1;
        positiontue = -1;
        positionwen = -1;
        positiontur = -1;
        positionfry = -1;
        positionsat = -1;
        positionsun = -1;

        setEnableDisable(); //ako je izmenjeno intuitivno, ne iz baze vec tokom rada
        int j = 0;
        for (int i = 0; i < searchRecipes.size(); i++) {

            TextView tvNameOfRecipe = new TextView(this);
            tRow = new TableRow(this);
            tRowCheckDay = new TableRow(this);
            tRowTextDay = new TableRow(this);

            chMon = new CheckBox(this);
            chTue = new CheckBox(this);
            chWen = new CheckBox(this);
            chTur = new CheckBox(this);
            chFri = new CheckBox(this);
            chSat = new CheckBox(this);
            chSun = new CheckBox(this);

            chMon.setTag(searchRecipes.get(i).getId().toString());
            chTue.setTag(searchRecipes.get(i).getId().toString());
            chWen.setTag(searchRecipes.get(i).getId().toString());
            chTur.setTag(searchRecipes.get(i).getId().toString());
            chFri.setTag(searchRecipes.get(i).getId().toString());
            chSat.setTag(searchRecipes.get(i).getId().toString());
            chSun.setTag(searchRecipes.get(i).getId().toString());

            chMon.setId(Integer.parseInt("0"));
            chTue.setId(Integer.parseInt("1"));
            chWen.setId(Integer.parseInt("2"));
            chTur.setId(Integer.parseInt("3"));
            chFri.setId(Integer.parseInt("4"));
            chSat.setId(Integer.parseInt("5"));
            chSun.setId(Integer.parseInt("6"));

            chMon.setGravity(Gravity.CENTER_HORIZONTAL);
            chTue.setGravity(Gravity.CENTER_HORIZONTAL);
            chWen.setGravity(Gravity.CENTER_HORIZONTAL);
            chTur.setGravity(Gravity.CENTER_HORIZONTAL);
            chFri.setGravity(Gravity.CENTER_HORIZONTAL);
            chSat.setGravity(Gravity.CENTER_HORIZONTAL);
            chSun.setGravity(Gravity.CENTER_HORIZONTAL);

            setChecked(searchRecipes.get(i).getId(), "0");//postavi iscitano iz baze
            setChecked(searchRecipes.get(i).getId(), "1");
            setChecked(searchRecipes.get(i).getId(), "2");
            setChecked(searchRecipes.get(i).getId(), "3");
            setChecked(searchRecipes.get(i).getId(), "4");
            setChecked(searchRecipes.get(i).getId(), "5");
            setChecked(searchRecipes.get(i).getId(), "6");

            setIfDinamicalyCheckedUnchecked(i, "0");
            setIfDinamicalyCheckedUnchecked(i, "1");
            setIfDinamicalyCheckedUnchecked(i, "2");
            setIfDinamicalyCheckedUnchecked(i, "3");
            setIfDinamicalyCheckedUnchecked(i, "4");
            setIfDinamicalyCheckedUnchecked(i, "5");
            setIfDinamicalyCheckedUnchecked(i, "6");

            chMon.setOnCheckedChangeListener(this);
            chTue.setOnCheckedChangeListener(this);
            chWen.setOnCheckedChangeListener(this);
            chTur.setOnCheckedChangeListener(this);
            chFri.setOnCheckedChangeListener(this);
            chSat.setOnCheckedChangeListener(this);
            chSun.setOnCheckedChangeListener(this);

            TextView tvMon = new TextView(this);
            TextView tvTue = new TextView(this);
            TextView tvWen = new TextView(this);
            TextView tvTur = new TextView(this);
            TextView tvFri = new TextView(this);
            TextView tvSat = new TextView(this);
            TextView tvSun = new TextView(this);

            tvMon.setGravity(Gravity.CENTER);
            tvTue.setGravity(Gravity.CENTER);
            tvWen.setGravity(Gravity.CENTER);
            tvTur.setGravity(Gravity.CENTER);
            tvFri.setGravity(Gravity.CENTER);
            tvSat.setGravity(Gravity.CENTER);
            tvSun.setGravity(Gravity.CENTER);

            tvMon.setTextSize(10);
            tvTue.setTextSize(10);
            tvWen.setTextSize(10);
            tvTur.setTextSize(10);
            tvFri.setTextSize(10);
            tvSat.setTextSize(10);
            tvSun.setTextSize(10);

            tvMon.setTextColor(Color.parseColor("#1e73be"));
            tvTue.setTextColor(Color.parseColor("#1e73be"));
            tvWen.setTextColor(Color.parseColor("#1e73be"));
            tvTur.setTextColor(Color.parseColor("#1e73be"));
            tvFri.setTextColor(Color.parseColor("#1e73be"));
            tvSat.setTextColor(Color.parseColor("#1e73be"));
            tvSun.setTextColor(Color.parseColor("#1e73be"));

            chMon.setPadding(0, 0, 10, 5);
            chTue.setPadding(0, 0, 10, 5);
            chWen.setPadding(0, 0, 10, 5);
            chTur.setPadding(0, 0, 10, 5);
            chFri.setPadding(0, 0, 10, 5);
            chSat.setPadding(0, 0, 10, 5);
            chSun.setPadding(0, 0, 10, 5);

            tvMon.setText("Mon");
            tvTue.setText("Tue");
            tvWen.setText("Wen");
            tvTur.setText("Thu");
            tvFri.setText("Fry");
            tvSat.setText("Sat");
            tvSun.setText("Sun");

            tvMon.setTypeface(tf);
            tvTue.setTypeface(tf);
            tvWen.setTypeface(tf);
            tvTur.setTypeface(tf);
            tvFri.setTypeface(tf);
            tvSat.setTypeface(tf);
            tvSun.setTypeface(tf);

            tRow.setPadding(0, 7, 0, 0);
            tRow.setTag(searchRecipes.get(i).getId().toString());

            TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.span = 7;
            params.gravity = Gravity.CLIP_VERTICAL;
            params.setMargins(10, 0, 0, 0);

            tvNameOfRecipe.setTag(searchRecipes.get(i).getId().toString());
            tvNameOfRecipe.setTextSize(16);
            tvNameOfRecipe.setTextColor(Color.parseColor("#1e73be"));
            tvNameOfRecipe.setText(searchRecipes.get(i).getRecipe().toString());

            tRow.addView(tvNameOfRecipe, params);

            tRowCheckDay.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            tRowCheckDay.setTag(searchRecipes.get(i).getId().toString());
            tRowCheckDay.addView(chMon, 0);
            tRowCheckDay.addView(chTue, 1);
            tRowCheckDay.addView(chWen, 2);
            tRowCheckDay.addView(chTur, 3);
            tRowCheckDay.addView(chFri, 4);
            tRowCheckDay.addView(chSat, 5);
            tRowCheckDay.addView(chSun, 6);

            tRowTextDay.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            tRowTextDay.setTag(searchRecipes.get(i).getId().toString());
            tRowTextDay.addView(tvMon, 0);
            tRowTextDay.addView(tvTue, 1);
            tRowTextDay.addView(tvWen, 2);
            tRowTextDay.addView(tvTur, 3);
            tRowTextDay.addView(tvFri, 4);
            tRowTextDay.addView(tvSat, 5);
            tRowTextDay.addView(tvSun, 6);

            //change colors
            TableLayout tablerow = new TableLayout(this);
            j++;
            if (j != 7) {
                tablerow.setBackgroundColor(Color.parseColor(ColorForRow[j]));
            } else {
                j = 0;
            }

            tablerow.addView(tRow);

            tRowCheckDay.setPadding(5, 0, 0, 0);
            //tRowTextDay.setPadding(10, 0, 0, 0);
            tablerow.addView(tRowCheckDay);
            tablerow.addView(tRowTextDay);
            height = tablerow.getHeight();

            //eight = tablerow.get;
            //Log.d("row height",Integer.toString(height));
            tableview.addView(tablerow);

            if (hiderowintable != null) { //ako je odabran kriterijum pretrage po nazivu
                if (hiderowintable.length != searchRecipes.size()) {
                    for (int h = 0; h < hiderowintable.length; h++) {
                        if (tRow.getTag().toString().equals(hiderowintable[h].toString())) {
                            //Log.d("hiderow" + h, tRow.getTag().toString());
                            tRow.setVisibility(View.GONE);
                            tRowCheckDay.setVisibility(View.GONE);
                            tRowTextDay.setVisibility(View.GONE);
                        }
                    }
                } else {
                    for (int h = 0; h < searchRecipes.size(); h++) {
                        if (tRow.getTag().toString().equals(searchRecipes.get(i).getId().toString())) {
                            //Log.d("hiderow" + h, tRow.getTag().toString());
                            tRow.setVisibility(View.GONE);
                            tRowCheckDay.setVisibility(View.GONE);
                            tRowTextDay.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.set_weekly_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_save:
                //rec_ids, days, checked su promene koje se bele�e
                for (int j = 0; j < rec_ids.size(); j++) {
                    dbMenu.updateMenu(rec_ids.get(j).toString(), days.get(j).toString(), checked.get(j).toString(), null);
                }

                Intent reopenShowWeeklyMenuIntent1 = new Intent(this, ShowWeeklyMenu.class);
                startActivity(reopenShowWeeklyMenuIntent1);
                this.finish();
                return true;

            case R.id.menu_cancel:
                Intent reopenShowWeeklyMenuIntent2 = new Intent(this, ShowWeeklyMenu.class);
                startActivity(reopenShowWeeklyMenuIntent2);
                this.finish();
                return true;

            case R.id.menu_uncheck_all:
                for (int j = 0; j < searchRecipes.size(); j++) {
                    String rec_id = searchRecipes.get(j).getId();
                    for (int i = 0; i < rec_ids.size(); i++) {
                        if (rec_ids.get(i).equals(rec_id)) {
                            checked.set(i, false);
                        }
                    }
                }

                DrawTable();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setIfDinamicalyCheckedUnchecked(int i, String day) {

        switch (Integer.parseInt(day.toString())) {
            case 0:
                if (positionmon == -1) { //ako nijedan nije cekiran
                    chMon.setEnabled(true); //omoguci sve checkboxove
                } else { //ako je jedan cekiran
                    if (i == positionmon) {
                        chMon.setEnabled(true);//taj omoguci, da bi moglo da se menja stanje tj. da se odcekira
                    } else {
                        chMon.setEnabled(false);//ostale onemoguci, jer postoji samo jedno jelo za dorucak, rucak... u ponedeljak
                    }
                }
                break;
            case 1:
                if (positiontue == -1) { //ako nijedan nije cekiran
                    chTue.setEnabled(true); //omoguci sve checkboxove
                } else { //ako je jedan cekiran
                    if (i == positiontue) {
                        chTue.setEnabled(true);//taj omoguci, da bi moglo da se menja stanje tj. da se odcekira
                    } else {
                        chTue.setEnabled(false);//ostale onemoguci, jer postoji samo jedno jelo za dorucak, rucak... u ponedeljak
                    }
                }
                break;
            case 2:
                if (positionwen == -1) { //ako nijedan nije cekiran
                    chWen.setEnabled(true); //omoguci sve checkboxove
                } else { //ako je jedan cekiran
                    if (i == positionwen) {
                        chWen.setEnabled(true);//taj omoguci, da bi moglo da se menja stanje tj. da se odcekira
                    } else {
                        chWen.setEnabled(false);//ostale onemoguci, jer postoji samo jedno jelo za dorucak, rucak... u ponedeljak
                    }
                }
                break;
            case 3:
                if (positiontur == -1) { //ako nijedan nije cekiran
                    chTur.setEnabled(true); //omoguci sve checkboxove
                } else { //ako je jedan cekiran
                    if (i == positiontur) {
                        chTur.setEnabled(true);//taj omoguci, da bi moglo da se menja stanje tj. da se odcekira
                    } else {
                        chTur.setEnabled(false);//ostale onemoguci, jer postoji samo jedno jelo za dorucak, rucak... u ponedeljak
                    }
                }
                break;
            case 4:
                if (positionfry == -1) { //ako nijedan nije cekiran
                    chFri.setEnabled(true); //omoguci sve checkboxove
                } else { //ako je jedan cekiran
                    if (i == positionfry) {
                        chFri.setEnabled(true);//taj omoguci, da bi moglo da se menja stanje tj. da se odcekira
                    } else {
                        chFri.setEnabled(false);//ostale onemoguci, jer postoji samo jedno jelo za dorucak, rucak... u ponedeljak
                    }
                }
                break;
            case 5:
                if (positionsat == -1) { //ako nijedan nije cekiran
                    chSat.setEnabled(true); //omoguci sve checkboxove
                } else { //ako je jedan cekiran
                    if (i == positionsat) {
                        chSat.setEnabled(true);//taj omoguci, da bi moglo da se menja stanje tj. da se odcekira
                    } else {
                        chSat.setEnabled(false);//ostale onemoguci, jer postoji samo jedno jelo za dorucak, rucak... u ponedeljak
                    }
                }
                break;
            case 6:
                if (positionsun == -1) { //ako nijedan nije cekiran
                    chSun.setEnabled(true); //omoguci sve checkboxove
                } else { //ako je jedan cekiran
                    if (i == positionsun) {
                        chSun.setEnabled(true);//taj omoguci, da bi moglo da se menja stanje tj. da se odcekira
                    } else {
                        chSun.setEnabled(false);//ostale onemoguci, jer postoji samo jedno jelo za dorucak, rucak... u ponedeljak
                    }
                }
                break;
            default:
                break;
        }

    }

    private void setEnableDisable() {

        for (int i = 0; i < searchRecipes.size(); i++) {
            for (int j = 0; j < rec_ids.size(); j++) {
                if (searchRecipes.get(i).getId().toString().equals(rec_ids.get(j).toString()) &&
                        days.get(j).equals("0")) {
                    if (checked.get(j)) {
                        positionmon = i;
                    }
                }
                if (searchRecipes.get(i).getId().toString().equals(rec_ids.get(j).toString()) &&
                        days.get(j).equals("1")) {
                    if (checked.get(j)) {
                        positiontue = i;
                    }
                }
                if (searchRecipes.get(i).getId().toString().equals(rec_ids.get(j).toString()) &&
                        days.get(j).equals("2")) {
                    if (checked.get(j)) {
                        positionwen = i;
                    }
                }
                if (searchRecipes.get(i).getId().toString().equals(rec_ids.get(j).toString()) &&
                        days.get(j).equals("3")) {
                    if (checked.get(j)) {
                        positiontur = i;
                    }
                }
                if (searchRecipes.get(i).getId().toString().equals(rec_ids.get(j).toString()) &&
                        days.get(j).equals("4")) {
                    if (checked.get(j)) {
                        positionfry = i;
                    }
                }
                if (searchRecipes.get(i).getId().toString().equals(rec_ids.get(j).toString()) &&
                        days.get(j).equals("5")) {
                    if (checked.get(j)) {
                        positionsat = i;
                    }
                }
                if (searchRecipes.get(i).getId().toString().equals(rec_ids.get(j).toString()) &&
                        days.get(j).equals("6")) {
                    if (checked.get(j)) {
                        positionsun = i;
                    }
                }
            }
        }
    }

    private void setChecked(String rec_id, String day) { //za ispisivanje iz baze

        for (int i = 0; i < rec_ids.size(); i++) {

            if (rec_ids.get(i).toString().equals(rec_id.toString()) && days.get(i).toString().equals(day.toString())) {

                switch (Integer.parseInt(day.toString())) {
                    case 0:
                        chMon.setChecked(Boolean.parseBoolean(checked.get(i).toString()));
                        break;
                    case 1:
                        chTue.setChecked(Boolean.parseBoolean(checked.get(i).toString()));
                        break;
                    case 2:
                        chWen.setChecked(Boolean.parseBoolean(checked.get(i).toString()));
                        break;
                    case 3:
                        chTur.setChecked(Boolean.parseBoolean(checked.get(i).toString()));
                        break;
                    case 4:
                        chFri.setChecked(Boolean.parseBoolean(checked.get(i).toString()));
                        break;
                    case 5:
                        chSat.setChecked(Boolean.parseBoolean(checked.get(i).toString()));
                        break;
                    case 6:
                        chSun.setChecked(Boolean.parseBoolean(checked.get(i).toString()));
                        break;

                }
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        //sluzi za snimanje u bazu
        for (int i = 0; i < rec_ids.size(); i++) {
            if (rec_ids.get(i).toString().equals(buttonView.getTag().toString()) &&
                    days.get(i).toString().equals(Integer.toString(buttonView.getId()))) {

                rec_ids.set(i, buttonView.getTag().toString());
                days.set(i, Integer.toString(buttonView.getId()));
                checked.set(i, isChecked);

            }
        }

        DrawTable();

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.bSearch) {

            hiderowintable = null;
            int i = 1; //use for hiderowintable array
            searchRecipes = dbRecipes.getAllRecipeByCategory(category);

            if (!acRecipe.getText().toString().equals("")) {

                boolean existInCategory = false;
                for (int r = 0; r < searchRecipes.size(); r++) {
                    if (selection.equals(searchRecipes.get(r).getRecipe())) {
                        existInCategory = true;
                    }
                }

                if (existInCategory) {
                    //than show recipe
                    hiderowintable = new String[searchRecipes.size()];
                    for (int m = 0; m < searchRecipes.size(); m++) {
                        Log.d("searchRecipes" + m, searchRecipes.get(m).getRecipe().toString());
                        if (!searchRecipes.get(m).getRecipe().toString().equals(selection.toString())) {
                            hiderowintable[i] = searchRecipes.get(m).getId().toString();
                            i++;
                        }
                    }

                    //Delete empty string from string array hiderowintable
                    List<String> list = new ArrayList<String>();
                    for (String s : hiderowintable) {
                        if (s != null && s.length() > 0) {
                            list.add(s);
                        }
                    }
                    hiderowintable = list.toArray(new String[list.size()]);

                } else {
                    Toast.makeText(this, "No recipe " + selection + " in " + category + " category.", Toast.LENGTH_LONG).show();
                }


                acRecipe.setText("");

            }

            DrawTable();

        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (dbMenu.getDb() != null) {
            dbMenu.getDb().close();
        }
        if (dbRecipes.getDb() != null) {
            dbRecipes.getDb().close();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

}
