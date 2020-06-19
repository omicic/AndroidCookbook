package com.example.androidcookbook;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class ShowNoteForAll extends Activity implements OnClickListener, OnCheckedChangeListener {

    private SharedPreferences preferences;
    private Typeface tf;

    private ImageButton ibok;
    private int padding;
    private int size;
    private LinearLayout lltextviewandcheckbos;
    private LinearLayout llforokbutton;
    private LinearLayout llheader;
    private LinearLayout llnote;

    private ArrayList<String> checked;
    private Editor editor;

    private String[] ColorForRow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.row_ingredients);

        ColorForRow = new String[]{"#faf9a4", "#fcfcf1"};//, "#fefdd8", "#fcfcf1", "#fefdd8", "#fcfcf1"};
        tf = Typeface.createFromAsset(getAssets(), "fonts/quikhand.ttf");
        llheader = (LinearLayout) this.findViewById(R.id.llstickyheader);
        llnote = (LinearLayout) this.findViewById(R.id.llmakenote);

        preferences = this.getSharedPreferences("WeeklyShoppingList", this.MODE_PRIVATE);
        size = preferences.getInt("Size", -1); //ako postoji uzme size, a ako ne -1
        checked = new ArrayList<String>();

        if (size != -1) {
            ShowNote();
        } else {
            Toast.makeText(this, "There's no ingredients to show.", Toast.LENGTH_LONG).show();
            finish();
        }
    }


    @SuppressLint("ResourceType")
    private void ShowNote() {

        int l = 1;

        checked.clear();

        while (l <= size) {
            checked.add(String.valueOf(preferences.getBoolean("checked" + l, false)));
            l++;
        }

        llnote.removeAllViews();
        LinearLayout.LayoutParams llparam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, 1.0f);

        LayoutParams lpib = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lpib.gravity = Gravity.RIGHT;

        llnote.setVisibility(View.VISIBLE);
        //llnote.setBackgroundResource(R.drawable.sticky);

        llparam.setMargins(10, 5, 0, 5);

        ImageButton ibCloseAndDeletePref = (ImageButton) findViewById(R.id.ibdelete);
        ibCloseAndDeletePref.setImageResource(R.drawable.deleteicon);
        ibCloseAndDeletePref.setOnClickListener(this);

        int m = 0;
        for (int i = 1; i <= size; i++) {  //prikazuje samo one koji nisu prazni tj. ne nedostaju; TREBA NAPRAVITI U MAKENOTE.CLASS DA SE I NE UPISUJU U PREFERENCES!!!

            lltextviewandcheckbos = new LinearLayout(this);
            lltextviewandcheckbos.setOrientation(LinearLayout.HORIZONTAL);
            //lltextviewandcheckbos.setAlpha((float) 0.4);
            lltextviewandcheckbos.setId(size);

            if (m == 0) {
                lltextviewandcheckbos.setBackgroundColor(Color.parseColor(ColorForRow[0]));
                m=1;
            } else {
                lltextviewandcheckbos.setBackgroundColor(Color.parseColor(ColorForRow[1]));
                m = 0;
            }

            TextView tvingnote = new TextView(this);
            tvingnote.setTextSize(20);
            tvingnote.setId(i);
            tvingnote.setTypeface(tf);
            tvingnote.setTextColor(Color.parseColor("#2E2EB8"));
            tvingnote.setPadding(padding, 10, 0, 0);

            tvingnote.setText("  " + preferences.getString("Ingredient" + i, "") + ", " +
                    preferences.getString("Qu" + i, "") + " " +
                    preferences.getString("Mu" + i, ""));

            tvingnote.setLayoutParams(llparam);

            CheckBox cb = new CheckBox(this);
            cb.setId(i);

            cb.setChecked(Boolean.parseBoolean(checked.get(i - 1).toString()));
            cb.setOnCheckedChangeListener(this);

            lltextviewandcheckbos.addView(tvingnote);
            lltextviewandcheckbos.addView(cb);

            llnote.addView(lltextviewandcheckbos);

        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {

        preferences = this.getSharedPreferences("WeeklyShoppingList", this.MODE_PRIVATE);

        if (v.getId() == R.id.ibdelete) { //delete
            preferences.edit().clear().commit();
            File file = new File("/data/data/com.example.androidcookbook/shared_prefs/WeeklyShoppingList.xml");
            if (file.exists()) {
                file.delete();
            }
            finish();
        }

        if (v.getId() == -3) {
            int m = 0;
            for (String s : checked) {
                m++;
                String check = "checked" + m;
                editor = preferences.edit();
                editor.putBoolean(check, Boolean.parseBoolean(s));
                editor.commit();
            }

            this.finish();

        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void onCheckedChanged(CompoundButton cb, boolean check) {
        checked.set(cb.getId()-1, Boolean.toString(check));

        String checkk = "checked" + Integer.toString(cb.getId()-1);
        preferences.edit().putBoolean(checkk, check);;
        //editor.commit();
    }

    @Override
    public void onBackPressed() {
        preferences.edit().commit();
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
