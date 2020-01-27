package com.example.androidcookbook;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;

import com.example.androidcookbook.mydb.MyDB;
import com.example.androidcookbook.mydb.RecipeDB;
import com.example.androidcookbook.object.Recipe;
import com.example.androidcookbook.work.ShowListOfRecipe;


public class SearchableActivity extends Activity {

    private MyDB db;
    private Recipe tn;
    private Recipe tr;
    private RecipeDB recept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_show_list_layout);
        handleIntent(getIntent());
        recept = new RecipeDB(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String que = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(que, null);

            doMySearch(que);
        }
    }

    private void doMySearch(String q) {

        Intent i = new Intent(this, ShowListOfRecipe.class);
        i.putExtra("searchValue", q);
        startActivity(i);

    }

}
