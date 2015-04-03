package com.drprog.weather;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {

    private static final String PREF_KEY_LAST_LOCATION = "PREF_KEY_LAST_LOCATION";

    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.app_bar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            loadWeather(query);
        }else{
            loadWeather(getLastQuery());

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager =
                (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        mSearchView = null;
        if (searchItem != null) {
            mSearchView = (SearchView) searchItem.getActionView();
        }
        if (mSearchView != null) {
            mSearchView.setSearchableInfo(
                    searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
            mSearchView.setSubmitButtonEnabled(true);
            mSearchView.setIconifiedByDefault(true);
            mSearchView.setOnQueryTextListener(this);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        loadWeather(s);
        mSearchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    private void loadWeather(String query) {
        saveLastQuery(query);
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(getString(R.string.fragment_main_tag));
        if (fragment != null) {
            ((MainFragment) fragment).loadWeather(query);
        }
    }

    private void saveLastQuery(String query) {
        SharedPreferences sPref =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sPref.edit().putString(PREF_KEY_LAST_LOCATION, query).apply();
    }

    private String getLastQuery() {
        SharedPreferences sPref =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sPref.getString(PREF_KEY_LAST_LOCATION,"London");
    }
}
