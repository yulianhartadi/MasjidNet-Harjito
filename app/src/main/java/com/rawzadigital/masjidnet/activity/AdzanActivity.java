package com.rawzadigital.masjidnet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.rawzadigital.masjidnet.R;
import com.rawzadigital.masjidnet.utils.Tools;

import java.util.Calendar;

public class AdzanActivity extends AppCompatActivity {
    TextView today;
    TextView subuh;
    TextView terbit;
    TextView dhuhur;
    TextView ashar;
    TextView magrib;
    TextView ishak;
    TextView waktu;
    TextView waktumundur;
    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                while (!thread.isInterrupted()) {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            waktu();
                        }
                    });
                }
            } catch (InterruptedException e) {
                Log.e("run: ", String.valueOf(e));
            }
        }
    };
    
    String hitungmundur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adzan);
        today = findViewById(R.id.today);
        subuh = findViewById(R.id.subuh);
        terbit = findViewById(R.id.terbit);
        dhuhur = findViewById(R.id.dhuhur);
        ashar = findViewById(R.id.ashar);
        magrib = findViewById(R.id.magrib);
        ishak = findViewById(R.id.ishak);
        waktu = findViewById(R.id.waktu);
        waktumundur = findViewById(R.id.waktumundur);
        waktu();
        Intent intent = getIntent();
        today.setText(intent.getStringExtra("today"));
        subuh.setText(intent.getStringExtra("subuh"));
        terbit.setText(intent.getStringExtra("terbit"));
        dhuhur.setText(intent.getStringExtra("dhuhur"));
        ashar.setText(intent.getStringExtra("ashar"));
        magrib.setText(intent.getStringExtra("magrib"));
        ishak.setText(intent.getStringExtra("ishak"));
        hitungmundur=intent.getStringExtra("timer");
        thread.start();
        initToolbar();

    }
    public void waktu() {
        String jam, menit, detik;

/*
        int mjam, mmenit, mdetik;
        int hjam, hmenit, hdetik;
        hjam=24;
        hmenit=60;
        hdetik=60;
        mjam= Integer.parseInt(hitungmundur.substring(0,2));
        mmenit= Integer.parseInt(hitungmundur.substring(2,2))-1;
        mdetik=60;
*/
        if (Calendar.getInstance().get(
                Calendar.HOUR_OF_DAY) < 10) {
            jam = "0" + Calendar.getInstance().get(
                    Calendar.HOUR_OF_DAY);
        } else {
            jam = String.valueOf(Calendar.getInstance().get(
                    Calendar.HOUR_OF_DAY));
        }
        if (Calendar.getInstance().get(
                Calendar.MINUTE) < 10) {
            menit = "0" + Calendar.getInstance().get(
                    Calendar.MINUTE);
        } else {
            menit= String.valueOf(Calendar.getInstance().get(
                    Calendar.MINUTE));
        }
        if (Calendar.getInstance().get(
                Calendar.SECOND) < 10) {
            detik = "0" + Calendar.getInstance().get(
                    Calendar.SECOND);
        } else {
            detik= String.valueOf(Calendar.getInstance().get(
                    Calendar.SECOND));
        }
/*
        hdetik=mdetik-Calendar.getInstance().get(
                Calendar.SECOND);
        if (mmenit>Integer.parseInt(menit)){
            hmenit=mmenit-Integer.parseInt(menit);
        }else {
            hmenit=60+mmenit-Integer.parseInt(menit);
            mjam=mjam-1;
        }
        if (mjam>Integer.parseInt(jam)){
            hjam=mmenit-Integer.parseInt(jam);
        }else {
            hjam=24+mmenit-Integer.parseInt(jam);
        }
*/

        waktu.setText(String.format("%s:%s:%s", jam, menit, detik));
//        waktu.setText(String.format("%s:%s:%s", hjam, hmenit, hdetik));
    }
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Jadwal Sholat");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
