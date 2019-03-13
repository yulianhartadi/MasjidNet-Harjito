package com.rawzadigital.masjidnet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.rawzadigital.masjidnet.R;
import com.rawzadigital.masjidnet.utils.Tools;

public class TakmirActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takmir);

        initToolbar();
        initComponent();


        FloatingActionButton idProfile = findViewById(R.id.takmir_profile);
        FloatingActionButton idAddIkhwan = findViewById(R.id.add_ikhwan);
        FloatingActionButton idMasjidku = findViewById(R.id.id_masjidku);
        FloatingActionButton idEventku = findViewById(R.id.id_eventku);
        FloatingActionButton idTambahArtikel = findViewById(R.id.fab_tambah_artikel);


        //FAB button dashboard intent
        idProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProfile = new Intent(TakmirActivity.this, ProfileActivity.class);
                startActivity(intentProfile);
            }
        });


        idAddIkhwan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddIkhwan = new Intent(TakmirActivity.this, AddIkhwanActivity.class);
                startActivity(intentAddIkhwan);
            }
        });

        idMasjidku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMasjidku = new Intent(TakmirActivity.this, DetailMasjidActivity.class);
                startActivity(intentMasjidku);
            }
        });

        idEventku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEventku = new Intent(TakmirActivity.this, EventActivity.class);
                startActivity(intentEventku);
            }
        });

        idTambahArtikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTambahArtikel = new Intent(TakmirActivity.this, AddArticleActivity.class);
                startActivity(intentTambahArtikel);
            }
        });



    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
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

    private void initComponent(){
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_2), R.drawable.img_slider2);
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_3), R.drawable.img_slider3);
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_4), R.drawable.img_slider4);
    }


}
