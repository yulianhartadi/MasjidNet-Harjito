package com.rawzadigital.masjidnet.activity;


import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rawzadigital.masjidnet.R;
import com.rawzadigital.masjidnet.data.Compass;
import com.rawzadigital.masjidnet.data.GPSTracker;
import com.rawzadigital.masjidnet.utils.Tools;

import static android.view.View.INVISIBLE;


public class KiblatActivity extends AppCompatActivity {

    private static final String TAG = "CompassActivity";
    private Compass compass;
    private ImageView arrowViewQiblat;
    private ImageView imageDial;
    private TextView text_atas;
    private TextView text_bawah;
    public Menu menu;
    public MenuItem item;
    private float currentAzimuth;
    SharedPreferences prefs;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiblat);

        prefs = getSharedPreferences("", MODE_PRIVATE);
        gps = new GPSTracker(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        arrowViewQiblat = findViewById(R.id.main_image_qiblat);
        imageDial = findViewById(R.id.main_image_dial);
        text_atas = findViewById(R.id.teks_atas);
        text_bawah = findViewById(R.id.teks_bawah);

        //init kompas
        setupCompass();

        //Toolbar & Component
        initToolbar();
        //initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Qiblat");
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
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "start compass");
        compass.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        compass.stop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        compass.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Stop compass");
        compass.stop();
    }

    private void setupCompass() {

        getBearing();

        compass = new Compass(this);
        Compass.CompassListener cl = new Compass.CompassListener() {

            @Override
            public void onNewAzimuth(float azimuth) {
                adjustGambarDial(azimuth);
                adjustArrowQiblat(azimuth);
            }
        };

        compass.setListener(cl);
    }

    public void adjustGambarDial(float azimuth) {

        Animation an = new RotateAnimation(-currentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAzimuth = (azimuth);
        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);
        imageDial.startAnimation(an);

    }

    public void adjustArrowQiblat(float azimuth) {

        float kiblat_derajat = GetFloat("kiblat_derajat");
        Animation an = new RotateAnimation(-(currentAzimuth)+ kiblat_derajat, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAzimuth = (azimuth);
        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);
        arrowViewQiblat.startAnimation(an);
    }

    //Cek Permission device kemudian set tulisan
    public void getBearing(){
        // Get the location manager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
        float kiblat_derajat = GetFloat("kiblat_derajat");
        if(kiblat_derajat > 0.0001){

            text_bawah.setText("Lokasi anda: mengambil dari lokasi terakhir");
            text_atas.setText("Arah Ka'bah: " + kiblat_derajat + " derajat dari utara");

        }else
        {
            fetch_GPS();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                } else {

                    Toast.makeText(getApplicationContext(), "This app requires Access Location", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



    public void SaveFloat(String Judul, Float bbb){
        SharedPreferences.Editor edit = prefs.edit();
        edit.putFloat(Judul, bbb);
        edit.apply();
    }
    public Float GetFloat(String Judul){
        Float xxxxxx = prefs.getFloat(Judul, 0);
        return xxxxxx;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        this.menu = menu;
        getMenuInflater().inflate(R.menu.gps, menu);
        MenuItem item = menu.findItem(R.id.gps);
        // inflater.inflate(R.menu.gps, menu);
        item = menu.findItem(R.id.gps);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.gps:
                //logout code
                fetch_GPS();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //Fetching GPS
    public void fetch_GPS(){
        double result = 0;
        gps = new GPSTracker(this);
        if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            // \n is for new line
            text_bawah.setText("Lokasi anda: \nLatitude: " + latitude + " Longitude: " + longitude);
            // Toast.makeText(getApplicationContext(), "Lokasi anda: - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            Log.e("TAG", "GPS is on");
            double lat_saya = gps.getLatitude ();
            double lon_saya = gps.getLongitude ();
            if(lat_saya < 0.001 && lon_saya < 0.001) {
                // arrowViewQiblat.isShown(false);
                arrowViewQiblat .setVisibility(INVISIBLE);
                arrowViewQiblat .setVisibility(View.GONE);
                text_atas.setText("Location not ready, try again");
                text_bawah.setText("Location not ready, try again");

                //item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.gps_off));
                Toast.makeText(getApplicationContext(), "Location not ready, Please Restart Application", Toast.LENGTH_LONG).show();
            }else{
                //item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.gps_on));
                double longitude2 = 39.826206;
                double longitude1 = lon_saya;
                double latitude2 = Math.toRadians(21.422487);
                double latitude1 = Math.toRadians(lat_saya);
                double longDiff= Math.toRadians(longitude2-longitude1);
                double y= Math.sin(longDiff)*Math.cos(latitude2);
                double x=Math.cos(latitude1)* Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);
                result = (Math.toDegrees(Math.atan2(y, x))+360)%360;
                float result2 = (float)result;
                SaveFloat("kiblat_derajat", result2);
                text_atas.setText("Arah Ka'bah: " + result2 + " derajat dari utara");
                Toast.makeText(getApplicationContext(), "Arah Ka'bah: " + result2 + " derajat dari utara", Toast.LENGTH_LONG).show();
            }
            //  Toast.makeText(getApplicationContext(), "lat_saya: "+lat_saya + "\nlon_saya: "+lon_saya, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();

            // arrowViewQiblat.isShown(false);
            arrowViewQiblat .setVisibility(INVISIBLE);
            arrowViewQiblat .setVisibility(View.GONE);
            text_atas.setText("Please enable Location first");
            text_bawah.setText("Please enable Location first");
            //item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.gps_off));
            // Toast.makeText(getApplicationContext(), "Please enable Location first and Restart Application", Toast.LENGTH_LONG).show();
        }
    }

}
