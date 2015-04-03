package com.drprog.weather;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {

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
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(getString(R.string.fragment_main_tag));
        if (fragment != null) {
            ((MainFragment) fragment).loadWeather(query);
        }
    }
}
