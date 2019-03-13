package com.rawzadigital.masjidnet.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.rawzadigital.masjidnet.R;
import com.rawzadigital.masjidnet.adapter.DoaGroupAdapter;
import com.rawzadigital.masjidnet.loader.DoaGroupLoader;
import com.rawzadigital.masjidnet.model.Doa;
import com.rawzadigital.masjidnet.utils.Tools;

import java.util.List;

public class DzikirActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Doa>> {

    private DoaGroupAdapter mAdapter;
    private ListView mListView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dzikir);

        //toolbar = findViewById(R.id.my_action_bar);
        View mToolbarShadow = findViewById(R.id.view_toolbar_shadow);
        //setSupportActionBar(toolbar);

        initToolbar();

        mListView = findViewById(R.id.doaListView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent;
                intent = new Intent(getBaseContext(),
                        DoaDetailActivity.class);

                Doa SelectedDoa = (Doa) parent.getAdapter().getItem(position);
                int doa_id = SelectedDoa.getReference();
                String doa_title = SelectedDoa.getTitle();

                intent.putExtra("doa_id", doa_id);
                intent.putExtra("doa_title", doa_title);

                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= 21) {
            mToolbarShadow.setVisibility(View.GONE);
        }

        // For Beta Testing
        Resources resource = getResources();
        String beta_version = resource.getString(R.string.beta_version);
        Toast.makeText(this, "Beta Version: " + beta_version, Toast.LENGTH_SHORT).show();
        // End of Beta Testing

        //nanti goleki gantine.. ojo lali
        getSupportLoaderManager().initLoader(0, null, this);

    }
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dzikir");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
/*
        getMenuInflater().inflate(R.menu.menu_doa_group, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return true;
            }
        });
*/

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            //Intent intent = new Intent(this, PreferencesActivity.class);
            //this.startActivity(intent);
        } else if (id == R.id.action_bookmarks) {
            //Intent intent = new Intent(this, BookmarksGroupActivity.class);
            //this.startActivity(intent);
        } else if (id == R.id.action_about) {
            //Intent intent = new Intent(this, AboutActivity.class);
            //this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Doa>> onCreateLoader(int id, Bundle args) {
        return new DoaGroupLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Doa>> loader, List<Doa> data) {
        if (mAdapter == null) {
            mAdapter = new DoaGroupAdapter(this, data);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Doa>> loader) {
        mAdapter.setData(null);
    }

}
