package com.rawzadigital.masjidnet.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
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
import com.rawzadigital.masjidnet.adapter.AdapterEvent;
import com.rawzadigital.masjidnet.model.ModelEvent;
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

public class EventActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<ModelEvent> dataEvent;
    ProgressBar login_progress;
    RelativeLayout Rloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        login_progress=findViewById(R.id.login_progress);
        Rloader=findViewById(R.id.Rloader);

        initToolbar();
        initComponent();

/*
        //Detail event
        Button tombolDetailEvent = findViewById(R.id.detail_event);
        tombolDetailEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailEvent = new Intent(EventActivity.this, DetailMasjidActivity.class);
                startActivity(detailEvent);
            }
        });
*/


    }

    private void initComponent() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        listEvent();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Event Kajian");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
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

    private void listEvent() {


        @SuppressLint("StaticFieldLeak")
        class LISTEVENT extends AsyncTask<Void, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Rloader.setVisibility(View.VISIBLE);
                runProgressDeterminateCircular();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s != null) {
                    //tampilkan data json result
                    String TAG = "";
                    Log.e(TAG, s);
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(s);

                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        List<ModelEvent> data = new ArrayList<>();
                        Log.e(TAG, "onPostExecute: " + jsonArray.length());
                        if (jsonArray.getJSONObject(0).isNull("name")) {
                            Log.e(TAG, jsonArray.getJSONObject(0).getString("result"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                Log.e(TAG, object.getString("title"));

                                ModelEvent ModelMasjid = new ModelEvent();
                                ModelMasjid.setName(object.getString("name"));
                                ModelMasjid.setaddress(object.getString("address"));
                                ModelMasjid.setImage(object.getString("thumbnail"));
                                ModelMasjid.setustadz(object.getString("ustadz"));
                                ModelMasjid.setmateri(object.getString("materi"));
                                ModelMasjid.setdeskripsi(object.getString("deskripsi"));
                                ModelMasjid.setbrochure(object.getString("brochure"));
                                data.add(ModelMasjid);
                            }
                        } else {
                            Log.e(TAG, jsonArray.getJSONObject(0).getString("name"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                Log.e(TAG, object.getString("name"));

                                ModelEvent ModelMasjid = new ModelEvent();
                                ModelMasjid.setName(object.getString("name"));
                                ModelMasjid.setaddress(object.getString("address"));
                                ModelMasjid.setImage(object.getString("thumbnail"));
                                ModelMasjid.setustadz(object.getString("ustadz"));
                                ModelMasjid.setmateri(object.getString("materi"));
                                ModelMasjid.setdeskripsi(object.getString("deskripsi"));
                                ModelMasjid.setbrochure(object.getString("brochure"));
                                data.add(ModelMasjid);
                            }
                        }
                        dataEvent = data;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                showlistEvent();
            }

            @Override
            protected String doInBackground(Void... v) {
                OkHttpClient okHttpClient = new OkHttpClient();
                //request ambil data
                Request request = new Request.Builder()
                        .url("http://masjid.fkam.or.id/api/event")
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

        LISTEVENT LE = new LISTEVENT();
        LE.execute();
    }

    private void showlistEvent() {
        AdapterEvent AdapterEvent = new AdapterEvent(dataEvent, this);
        recyclerView.setAdapter(AdapterEvent);
        AdapterEvent.setOnItemClickListener(new AdapterEvent.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ModelEvent obj, int position) {
                Toast.makeText(EventActivity.this, "Item " + obj.name + " clicked", Toast.LENGTH_SHORT).show();
                Intent intentDetailMasjid = new Intent(EventActivity.this,
                        DetailMasjidActivity.class);
                intentDetailMasjid.putExtra("NamaMasjid", obj.getName());
                intentDetailMasjid.putExtra("AlamatMasjid", obj.getaddress());
                intentDetailMasjid.putExtra("thumbnail", obj.getImage());

                startActivity(intentDetailMasjid);
            }
        });
        AdapterEvent.setOnBagiClickListener(new AdapterEvent.OnBagiClickListener() {
            @Override
            public void onItemClick(View view, ModelEvent obj, int position) {
                Toast.makeText(EventActivity.this, "Item " + obj.name + " clicked", Toast.LENGTH_SHORT).show();
                Intent intentDetailMasjid = new Intent();
                intentDetailMasjid.setAction(Intent.ACTION_SEND);
                intentDetailMasjid.putExtra(Intent.EXTRA_SUBJECT, obj.getmateri());
                intentDetailMasjid.putExtra(Intent.EXTRA_TEXT, obj.getdeskripsi() + "\nUstad: " + obj.getustadz() + "\nMateri: " + obj.getmateri() + "\nTempat: " + obj.getName() + "\nAlamat: " + obj.getaddress());
                intentDetailMasjid.setType("text/plain");

                startActivity(Intent.createChooser(intentDetailMasjid, "Share Via"));
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
