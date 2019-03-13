package com.rawzadigital.masjidnet.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rawzadigital.masjidnet.R;
import com.rawzadigital.masjidnet.adapter.AdapterMasjid;
import com.rawzadigital.masjidnet.model.ModelMasjid;
import com.rawzadigital.masjidnet.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListMasjidActivity extends AppCompatActivity {


    public List<ModelMasjid> datamasjid;
    private View parent_view;
    private RecyclerView recyclerView;
    private AdapterMasjid mAdapter;
    ProgressBar login_progress;
    RelativeLayout Rloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_masjid);
        parent_view = findViewById(android.R.id.content);
        login_progress=findViewById(R.id.login_progress);
        Rloader=findViewById(R.id.Rloader);

        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List Masjid");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }

    private void initComponent() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
//        new AsyncTack(this, recyclerView).execute("http://masjid.fkam.or.id/api/masjid");
        listMASJID();

/*
        List<People> items = DataGenerator.getPeopleData(this);

        items.addAll(DataGenerator.getPeopleData(this));
        items.addAll(DataGenerator.getPeopleData(this));

        int sect_count = 0;
        int sect_idx = 0;

        List<String> jarak = DataGenerator.getStringsJarak(this);
        for (int i = 0; i < items.size() / 6; i++) {
            items.add(sect_count, new People(jarak.get(sect_idx), true));
            sect_count = sect_count + 5;
            sect_idx++;
        }

        //set data and list adapter
        mAdapter = new AdapterListSectioned(this, items);
        recyclerView.setAdapter(mAdapter);
*/

        // on item list clicked
/*
        mAdapter.setOnItemClickListener(new AdapterListSectioned.OnItemClickListener() {
            @Override
            public void onItemClick(View view, People obj, int position) {
                Snackbar.make(parent_view, "Item " + obj.name + " clicked", Snackbar.LENGTH_SHORT).show();

                //Show Dialog
                showConfirmDialog();
            }
        });
*/
    }

    //Confirm Dialog
    private void showConfirmDialog(final ModelMasjid obj) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lihat Detail Informasi Masjid "+obj.getName()+" ?");
        builder.setMessage("Alamat"+obj.getaddress());
        builder.setPositiveButton(R.string.INFOMASJID, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intentDetailMasjid = new Intent(ListMasjidActivity.this,
                        DetailMasjidActivity.class);
                intentDetailMasjid.putExtra("NamaMasjid", obj.getName());
                intentDetailMasjid.putExtra("AlamatMasjid", obj.getaddress());
                intentDetailMasjid.putExtra("thumbnail",obj.getImage());

                startActivity(intentDetailMasjid);

            }
        });

        // builder.setNegativeButton(R.string.MAPMASJID, null);
        builder.setNegativeButton(R.string.MAPMASJID, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                Snackbar.make(parent_view, "Ke Map Activity", Snackbar.LENGTH_LONG).show();
                Uri uri=Uri.parse("google.navigation:q="+obj.getlatitude()+","+obj.getlongitude());
                Intent intentMaps=new Intent(Intent.ACTION_VIEW,uri);
                intentMaps.setPackage("com.google.android.apps.maps");
                startActivity(intentMaps);

            }
        });

        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void listMASJID() {


        @SuppressLint("StaticFieldLeak")
        class LISTMASJID extends AsyncTask<Void, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Rloader.setVisibility(View.VISIBLE);
                runProgressDeterminateCircular();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                recyclerView=findViewById(R.id.recyclerView);
                if (s != null) {
                    //tampilkan data json result
                    String TAG = "";
                    Log.e(TAG, s);
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(s);

                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        List<ModelMasjid> data = new ArrayList<>();
                        Log.e(TAG, "onPostExecute: " + jsonArray.length());
                        if (jsonArray.getJSONObject(0).isNull("name")) {
                            Log.e(TAG, jsonArray.getJSONObject(0).getString("result"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                Log.e(TAG, object.getString("title"));

                                ModelMasjid ModelMasjid = new ModelMasjid();
                                ModelMasjid.setName(object.getString("name"));
                                ModelMasjid.setaddress(object.getString("address"));
                                ModelMasjid.setlatitude(object.getString("latitude"));
                                ModelMasjid.setlongitude(object.getString("longitude"));
                                ModelMasjid.setImage(object.getString("thumbnail"));
                                data.add(ModelMasjid);
                            }
                        } else {
                            Log.e(TAG, jsonArray.getJSONObject(0).getString("name"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                Log.e(TAG, object.getString("name"));

                                ModelMasjid ModelMasjid = new ModelMasjid();
                                ModelMasjid.setName(object.getString("name"));
                                ModelMasjid.setaddress(object.getString("address"));
                                ModelMasjid.setlatitude(object.getString("latitude"));
                                ModelMasjid.setlongitude(object.getString("longitude"));
                                ModelMasjid.setImage(object.getString("thumbnail"));
                                data.add(ModelMasjid);
                            }
                        }
                        datamasjid = data;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showlistMASJID();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                OkHttpClient okHttpClient = new OkHttpClient();
                //request ambil data
                Request request = new Request.Builder()
                        .url("http://masjid.fkam.or.id/api/masjid")
                        .get()
                        .build();

                try {
                    //get request data dari api bukalapak
                    Response response = okHttpClient.newCall(request).execute();
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }

        LISTMASJID LD = new LISTMASJID();
        LD.execute();
    }

    private void showlistMASJID() {
        AdapterMasjid AdapterMasjid = new AdapterMasjid(datamasjid, this);
        recyclerView.setAdapter(AdapterMasjid);
        AdapterMasjid.setOnItemClickListener(new AdapterMasjid.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ModelMasjid obj, int position) {

                Snackbar.make(view, "Item " + obj.name + " clicked", Snackbar.LENGTH_SHORT).show();
                showConfirmDialog(obj);
            }
        });
        Rloader.setVisibility(View.GONE);
    }
    private void runProgressDeterminateCircular() {
        final Handler mHandler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                int progress = login_progress.getProgress() + 10;
                login_progress.setProgress(progress);
                if (progress > 100) {
                    login_progress.setProgress(0);
                }
                mHandler.postDelayed(this, 1000);
            }
        };
        mHandler.post(runnable);
    }

}
