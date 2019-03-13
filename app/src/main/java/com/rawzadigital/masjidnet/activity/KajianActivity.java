package com.rawzadigital.masjidnet.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

import com.rawzadigital.masjidnet.Akses;
import com.rawzadigital.masjidnet.R;
import com.rawzadigital.masjidnet.adapter.AdapterKajian;
import com.rawzadigital.masjidnet.adapter.AdapterMasjid;
import com.rawzadigital.masjidnet.fragment.FragmentTabsArtikel;
import com.rawzadigital.masjidnet.fragment.FragmentTabsKajian;
import com.rawzadigital.masjidnet.fragment.FragmentTabsYoutube;
import com.rawzadigital.masjidnet.model.Kajian;
import com.rawzadigital.masjidnet.model.ModelMasjid;
import com.rawzadigital.masjidnet.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class KajianActivity extends AppCompatActivity {

    List<Kajian> DataKajian;
    List<ModelMasjid> DataYoutube;
    private RecyclerView recyclerView, recyclerViewyoutube;
    private ViewPager view_pager;
    private SectionsPagerAdapter viewPagerAdapter;
    private TabLayout tab_layout;
    ProgressBar login_progress;
    RelativeLayout Rloader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kajian);
        login_progress=findViewById(R.id.login_progress);
        Rloader=findViewById(R.id.Rloader);

        initComponent();
        initToolbar();

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kajian");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }

    private void initComponent() {
        view_pager = findViewById(R.id.view_pager);
        setupViewPager(view_pager);


        tab_layout = findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);
        //Set Tab icon
//        tab_layout.getTabAt(0).setIcon(R.drawable.ic_music);
        tab_layout.getTabAt(0).setIcon(R.drawable.ic_movie);
        tab_layout.getTabAt(1).setIcon(R.drawable.ic_book);
        // set icon color pre-selected
        tab_layout.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tab_layout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.grey_20),
                PorterDuff.Mode.SRC_IN);
//        tab_layout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.grey_20),
//                PorterDuff.Mode.SRC_IN);

        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
/*
                    case 0:
                        Toast.makeText(KajianActivity.this, "0", Toast.LENGTH_SHORT).show();
                        break;
*/
                    case 0:
                        if (recyclerViewyoutube == null) {
                            listkajianyoutube();
                        }
//                        Toast.makeText(KajianActivity.this, "1", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        if (recyclerView == null) {
                            listkajianartikel();
                        }
//                        Toast.makeText(KajianActivity.this, "2", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.grey_20),
                        PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //End Tab


    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        viewPagerAdapter.addFragment(FragmentTabsKajian.newInstance(), "AUDIO");    // index 0
        viewPagerAdapter.addFragment(FragmentTabsYoutube.newInstance(), "VIDEO");   // index 1
        viewPagerAdapter.addFragment(FragmentTabsArtikel.newInstance(), "ARTIKEL");    // index 2
        viewPager.setAdapter(viewPagerAdapter);
        listkajianyoutube();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_search, menu);
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

    private void listkajianartikel() {


        @SuppressLint("StaticFieldLeak")
        class LISTARTIKEL extends AsyncTask<Void, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Rloader.setVisibility(View.VISIBLE);
                runProgressDeterminateCircular();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                recyclerView = findViewById(R.id.recyclerViewKajian);
                Log.e("Artikel", s);
                //tampilkan data json result
                String TAG = "";
                Log.e(TAG, s);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(s);

                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    List<Kajian> kajians = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Log.e("MAna", object.getString("title") + " " + object.getString("materi"));
                        Kajian kajian = new Kajian();
                        kajian.setjudul(object.getString("title"));
                        kajian.setmateri(object.getString("materi"));
                        kajians.add(kajian);
                    }
                    DataKajian = kajians;
                    showartikelkajian();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                OkHttpClient okHttpClient = new OkHttpClient();
                //request ambil data
                Request request = new Request.Builder()
                        .url("http://masjid.fkam.or.id/api/materi")
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

        LISTARTIKEL listartikel = new LISTARTIKEL();
        listartikel.execute();
    }

    private void showartikelkajian() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        Log.e("hjk", "showartikelkajian: " + DataKajian);
        AdapterKajian adapterKajian = new AdapterKajian(DataKajian, this);
        recyclerView.setAdapter(adapterKajian);
        recyclerView.setVisibility(View.VISIBLE);
        Log.e("CEKCK", "showartikelkajian: ");
        Rloader.setVisibility(View.GONE);


    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void listkajianyoutube() {


        @SuppressLint("StaticFieldLeak")
        class LISTYOUTUBE extends AsyncTask<Void, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Rloader.setVisibility(View.VISIBLE);
                runProgressDeterminateCircular();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                Log.e("Youtube", s);
                recyclerViewyoutube = findViewById(R.id.recyclerViewYoutube);

                //tampilkan data json result
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    List<ModelMasjid> modelMasjids = new ArrayList<>();
//                    Log.e("Youtube Json", String.valueOf(jsonArray));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
//                        Log.e("MAna", " " + object.getString("snippet"));
                        String id = object.getString("id");
                        String kind = new JSONObject(id).getString("kind");
                        Log.e("kind", kind);
                        String Spinnet = object.getString("snippet");
                        String spinet = new JSONObject(Spinnet).getString("title");
                        String description = new JSONObject(Spinnet).getString("description");
                        String thumbnails = new JSONObject(Spinnet).getString("thumbnails");
                        String medium = new JSONObject(thumbnails).getString("medium");
                        String url = new JSONObject(medium).getString("url");
                        if (kind.equals("youtube#video")) {
                            String videoId = new JSONObject(id).getString("videoId");
                            Log.e("Spinet", spinet);
                            Log.e("description", description);
                            Log.e("url", url);
                            ModelMasjid modelMasjid = new ModelMasjid();
                            modelMasjid.setName(spinet);
                            modelMasjid.setlatitude(videoId);
                            modelMasjid.setaddress(description);
                            modelMasjid.setImage(url);
                            modelMasjids.add(modelMasjid);
                        }
                    }
                    DataYoutube = modelMasjids;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showlistYoutube();
            }

            @Override
            protected String doInBackground(Void... v) {
                OkHttpClient okHttpClient = new OkHttpClient();
                //request ambil data
                Request request = new Request.Builder()
                        .url(Akses.API_YOUTUBE)
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

        LISTYOUTUBE listyoutube = new LISTYOUTUBE();
        listyoutube.execute();
    }

    private void showlistYoutube() {
        recyclerViewyoutube.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewyoutube.setHasFixedSize(true);
        AdapterMasjid AdapterMasjid = new AdapterMasjid(DataYoutube, this);
        recyclerViewyoutube.setAdapter(AdapterMasjid);
        AdapterMasjid.setOnItemClickListener(new AdapterMasjid.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ModelMasjid obj, int position) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://youtu.be/" + DataYoutube.get(position).getlatitude())));

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
