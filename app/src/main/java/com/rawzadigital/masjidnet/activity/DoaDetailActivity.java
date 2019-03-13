package com.rawzadigital.masjidnet.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.rawzadigital.masjidnet.R;
import com.rawzadigital.masjidnet.adapter.DoaDetailAdapter;
import com.rawzadigital.masjidnet.loader.DoaDetailsLoader;
import com.rawzadigital.masjidnet.model.Doa;

import java.util.List;

import me.grantland.widget.AutofitTextView;

public class DoaDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Doa>>{

    private int doaIdFromDuaListActivity;
    private String doaTitleFromDuaListActivity;
    private DoaDetailAdapter adapter;
    private ListView listView;

    private Toolbar toolbar;
    private TextView my_toolbar_doaGroup_number;
    private AutofitTextView my_autofit_toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doa_detail);

        toolbar = findViewById(R.id.my_detail_action_bar);
        my_toolbar_doaGroup_number = findViewById(R.id.txtReference_doaDetail);
        my_autofit_toolbar_title = findViewById(R.id.doa_detail_autofit_actionbar_title);
        View mToolbarShadow = findViewById(R.id.view_toolbar_shadow);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.listView = findViewById(R.id.doaDetailListView);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        doaIdFromDuaListActivity = bundle.getInt("doa_id");
        doaTitleFromDuaListActivity = bundle.getString("doa_title");

        my_toolbar_doaGroup_number.setText(Integer.toString(doaIdFromDuaListActivity));
        my_autofit_toolbar_title.setText(doaTitleFromDuaListActivity);
        setTitle("");

        if (Build.VERSION.SDK_INT >= 21) {
            mToolbarShadow.setVisibility(View.GONE);
        }

        getSupportLoaderManager().initLoader(0, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_doa_detail, menu);
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
            Intent intent = new Intent(this, PreferencesActivity.class);
            this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Doa>> onCreateLoader(int id, Bundle args) {
        return new DoaDetailsLoader(DoaDetailActivity.this, doaIdFromDuaListActivity);
    }

    @Override
    public void onLoadFinished(Loader<List<Doa>> loader, List<Doa> data) {
        if (adapter == null) {
            adapter = new DoaDetailAdapter(this, data, doaTitleFromDuaListActivity);
            listView.setAdapter(adapter);
        } else {
            adapter.setData(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Doa>> loader) {
        if (adapter != null) {
            adapter.setData(null);
        }
    }
}
