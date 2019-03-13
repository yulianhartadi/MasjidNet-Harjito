package com.rawzadigital.masjidnet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rawzadigital.masjidnet.R;

public class DetailMasjidActivity extends AppCompatActivity {

    TextView namamasjid,alamatmasjid;
    ImageView imagemasjid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_masjid);
        Intent intent=getIntent();
        Log.e(intent.getStringExtra("NamaMasjid"), intent.getStringExtra("AlamatMasjid") );
        namamasjid=findViewById(R.id.textNamamasjid);
        alamatmasjid=findViewById(R.id.alamatmasjid);
        imagemasjid=findViewById(R.id.image);
        namamasjid.setText(intent.getStringExtra("NamaMasjid"));
        alamatmasjid.setText(intent.getStringExtra("AlamatMasjid"));
        if (!intent.getStringExtra("thumbnail").equals("")) {
            Glide.with(this).load(intent.getStringExtra("thumbnail"))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imagemasjid);
        }
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_basic, menu);
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

}
