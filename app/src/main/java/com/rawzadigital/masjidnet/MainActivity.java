package com.rawzadigital.masjidnet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.rawzadigital.masjidnet.activity.AdzanActivity;
import com.rawzadigital.masjidnet.activity.DzikirActivity;
import com.rawzadigital.masjidnet.activity.EventActivity;
import com.rawzadigital.masjidnet.activity.KajianActivity;
import com.rawzadigital.masjidnet.activity.KiblatActivity;
import com.rawzadigital.masjidnet.activity.ListMasjidActivity;
import com.rawzadigital.masjidnet.activity.LoginActivity;
import com.rawzadigital.masjidnet.data.Dates;
import com.rawzadigital.masjidnet.data.HGDate;
import com.rawzadigital.masjidnet.data.PrayerTimes;
import com.rawzadigital.masjidnet.model.Image;
import com.rawzadigital.masjidnet.model.Slidder;
import com.rawzadigital.masjidnet.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.rawzadigital.masjidnet.data.PrayerTimes.Mazhab.PTC_MAZHAB_HANAFI;
import static com.rawzadigital.masjidnet.data.PrayerTimes.Way.PTC_WAY_MWL;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    //Location
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private static int[] array_image_place = {
            R.drawable.image_12,
            R.drawable.image_13,
            R.drawable.image_14,
            R.drawable.image_15,
            R.drawable.image_8,
    };
    private static String[] array_title_place = {
            "Dui fringilla ornare finibus, orci odio",
            "Mauris sagittis non elit quis fermentum",
            "Mauris ultricies augue sit amet est sollicitudin",
            "Suspendisse ornare est ac auctor pulvinar",
            "Vivamus laoreet aliquam ipsum eget pretium",
    };
    private static String[] array_brief_place = {
            "Foggy Hill",
            "The Backpacker",
            "River Forest",
            "Mist Mountain",
            "Side Park",
    };
    RelativeLayout RelatifWaktu;
    GoogleApiClient mGoogleApiClient;
    TextView KotaLokasi;
    List<Address> addresses;
    PrayerTimes prayerTimes;
    String subuh, terbit, dhuhur, ashar, magrib, ishak;
    int MenitSekarang = 0;
    TextView WaktuAdzan, WaktuSholat;
    String ApiWaktuSholat;
    int ihours, imenute;
    //TanggalHijriyah
    String TanggalHariIni;
    TextView TanggalHijriyah;
    //WaktuAdzan
    private SimpleDateFormat format;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    //Slider
    private View parent_view;
    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;
    private Runnable runnable = null;
    private Handler handler = new Handler();
    List<Slidder> DataSlidder;
    HashMap<String, String> params;
    String urlslidder;
    LinearLayout cardView;
    //End Slider

    //loader
    RelativeLayout RLoading;
    TextView textproses;
    ProgressBar progress_determinate;
    int prosesbaner, proseslokasi, proseswaktu = 0;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //loader
        textproses = findViewById(R.id.textproses);

        initToolbar();

        //Menu Item
        MaterialRippleLayout tombolMasjid = findViewById(R.id.cv_masjid);
        MaterialRippleLayout tombolTakmir = findViewById(R.id.cv_takmir);
        MaterialRippleLayout tombolEvent = findViewById(R.id.cv_event);
        MaterialRippleLayout tombolKajian = findViewById(R.id.cv_kajian);
        MaterialRippleLayout tombolDzikir = findViewById(R.id.cv_dzikir);
        MaterialRippleLayout tombolKiblat = findViewById(R.id.cv_kiblat);
        MaterialRippleLayout tombolZakat = findViewById(R.id.cv_zakat);
        MaterialRippleLayout tombolAdzan = findViewById(R.id.cv_adzan);
        MaterialRippleLayout tombolTentang = findViewById(R.id.cv_tentang);
        RelatifWaktu = findViewById(R.id.RelatifWaktu);
        //LokasiKota
        KotaLokasi = findViewById(R.id.KotaLokasi);

        //WaktuAdzan
        WaktuAdzan = findViewById(R.id.WaktuAdzan);
        WaktuSholat = findViewById(R.id.WaktuSholat);
        ihours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        imenute = Calendar.getInstance().get(Calendar.MINUTE);
        Log.e(TAG, ihours + ":" + imenute);
        format = new SimpleDateFormat("HH:mm", Locale.getDefault());

        //TanggalHijriyah
        TanggalHijriyah = findViewById(R.id.TanggalHijriyah);
        TanggalHariIni = new SimpleDateFormat("dd MMMM yyyy").format(new Date());
        //ambiltanggalhijriyah();
        HGDate hgDate = new HGDate();
        hgDate.toHigri();
        ambilslider();
        cardView = findViewById(R.id.urlslidder);
        TanggalHijriyah.setText(
                MessageFormat.format("{1} {2} {3}H\n{0}", TanggalHariIni, hgDate.getDay(),
                        Dates.islamicMonthName(this, hgDate.getMonth() - 1),
                        String.valueOf(hgDate.getYear())
                ));
        //Menu Intent
        tombolMasjid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RLoading.getVisibility() == View.GONE) {
                    //Toast.makeText(getApplicationContext(),"Clik Masjid", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, ListMasjidActivity.class);
                    startActivity(intent);
                }
            }
        });

        tombolTakmir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RLoading.getVisibility() == View.GONE) {

                    //Toast.makeText(getApplicationContext(),"Clik Takmir", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        tombolEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RLoading.getVisibility() == View.GONE) {

                    //Toast.makeText(getApplicationContext(),"Clik Event ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, EventActivity.class);
                    startActivity(intent);
                }
            }
        });

        tombolKajian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RLoading.getVisibility() == View.GONE) {

/*
                Toast.makeText(MainActivity.this, "Afwan, Fitur masih dalam tahap pengerjaan",
                        Toast.LENGTH_SHORT).show();
*/
                    //Toast.makeText(getApplicationContext(),"Clik Kajian ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, KajianActivity.class);
                    startActivity(intent);
                }
            }
        });
        tombolDzikir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RLoading.getVisibility() == View.GONE) {

                    Intent intent = new Intent(MainActivity.this, DzikirActivity.class);
                    startActivity(intent);
                }
            }
        });

        tombolKiblat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RLoading.getVisibility() == View.GONE) {

                    //Toast.makeText(getApplicationContext(),"Clik Kiblat ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, KiblatActivity.class);
                    startActivity(intent);
                }
            }
        });

        tombolZakat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RLoading.getVisibility() == View.GONE) {

/*
                Intent intent = new Intent(MainActivity.this, ZakatActivity.class);
                startActivity(intent);
*/
                    Toast.makeText(MainActivity.this, "Afwan, Fitur masih dalam tahap pengerjaan",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        RelatifWaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RLoading.getVisibility() == View.GONE) {

                    Intent intent = new Intent(MainActivity.this, AdzanActivity.class);
                    intent.putExtra("today", TanggalHijriyah.getText());
                    intent.putExtra("subuh", subuh);
                    intent.putExtra("terbit", terbit);
                    intent.putExtra("dhuhur", dhuhur);
                    intent.putExtra("ashar", ashar);
                    intent.putExtra("magrib", magrib);
                    intent.putExtra("ishak", ishak);
                    startActivity(intent);

                }
            }
        });
        tombolAdzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RLoading.getVisibility() == View.GONE) {

                    Intent intent = new Intent(MainActivity.this, AdzanActivity.class);
                    intent.putExtra("today", TanggalHijriyah.getText());
                    intent.putExtra("subuh", subuh);
                    intent.putExtra("terbit", terbit);
                    intent.putExtra("dhuhur", dhuhur);
                    intent.putExtra("ashar", ashar);
                    intent.putExtra("magrib", magrib);
                    intent.putExtra("ishak", ishak);
                    intent.putExtra("timer", WaktuAdzan.getText());
                    startActivity(intent);
                }
            }
        });

        tombolTentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RLoading.getVisibility() == View.GONE) {

                    //Toast.makeText(getApplicationContext(), "Clik Kajian ", Toast.LENGTH_SHORT).show();
                    showDialogTentang();
                }
            }
        });
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Prompt the user for permission.
            getLocationPermission();
            // Turn on the My Location layer and the related control on the map.
            // Get the current location of the device and set the position of the map.
            getDeviceLocation();
        } else {
            buildGoogleApiClient();
        }
        //loader
        RLoading = findViewById(R.id.RLoading);
        progress_determinate = findViewById(R.id.progress_determinate);
        RLoading.setVisibility(View.VISIBLE);
        runProgressDeterminate();
    }

    private void runProgressDeterminate() {
        final Handler mHandler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                int progress = 0;
                int jumlahproses = prosesbaner + proseslokasi + proseswaktu;
                switch (jumlahproses) {
                    case 0:
                        if (progress < 33) {
                            progress = progress_determinate.getProgress() + 1;
                            progress_determinate.setProgress(progress);
                        }
                        break;
                    case 1:
                        progress = 33;
                        progress_determinate.setProgress(progress);
                        if (progress < 66) {
                            progress = progress_determinate.getProgress() + 1;
                            progress_determinate.setProgress(progress);
                        }
                        break;
                    case 2:
                        progress = 66;
                        progress_determinate.setProgress(progress);
                        if (progress < 99) {
                            progress = progress_determinate.getProgress() + 1;
                            progress_determinate.setProgress(progress);
                        }
                        break;
                    case 3:
                        RLoading.setVisibility(View.GONE);
                        break;
                }
                mHandler.postDelayed(this, 1000);
            }
        };
        mHandler.post(runnable);
    }

    private void initToolbar() {
        layout_dots = findViewById(R.id.layout_dots);
        viewPager = findViewById(R.id.pager);
        // getSupportActionBar().setTitle("MasjidNet");
        Tools.setSystemBarColor(this);
        // Toolbar toolbar =findViewById(R.id.toolbar);
    }

    private void initComponent() {
        layout_dots = findViewById(R.id.layout_dots);
        viewPager = findViewById(R.id.pager);
        adapterImageSlider = new AdapterImageSlider(this, new ArrayList<Image>());

        final List<Image> items = new ArrayList<>();
        for (int i = 0; i < DataSlidder.size(); i++) {
            Image obj = new Image();
            obj.name = DataSlidder.get(i).gettitle();
            obj.brief = DataSlidder.get(i).getdate();
            obj.url = DataSlidder.get(i).geturl();
            obj.thumbnail = DataSlidder.get(i).getImage();
            items.add(obj);
        }

        adapterImageSlider.setItems(items);
        viewPager.setAdapter(adapterImageSlider);

        // displaying selected image first
        viewPager.setCurrentItem(0);
        addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);
        ((TextView) findViewById(R.id.title)).setText(items.get(0).name);
        ((TextView) findViewById(R.id.brief)).setText(items.get(0).brief);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                ((TextView) findViewById(R.id.title)).setText(items.get(pos).name);
                ((TextView) findViewById(R.id.brief)).setText(items.get(pos).brief);
                addBottomDots(layout_dots, adapterImageSlider.getCount(), pos);
                urlslidder = items.get(pos).url;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        startAutoSlider(adapterImageSlider.getCount());
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(urlslidder)));
            }
        });
        textproses.setText("Kegiatan FKAM Terkini Termuat");
        prosesbaner = 1;
    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle_outline);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setImageResource(R.drawable.shape_circle);
        }
    }

    private void startAutoSlider(final int count) {
        runnable = new Runnable() {
            @Override
            public void run() {
                int pos = viewPager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                viewPager.setCurrentItem(pos);
                handler.postDelayed(runnable, 5000);
            }
        };
        handler.postDelayed(runnable, 5000);
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        @SuppressLint("RestrictedApi") LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi
                        .checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                Log.e("Code", status.toString());
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.e("Code", "Succ");
                        // All location settings are satisfied.
                        // You can initialize location requests here.
                        int permissionLocation = ContextCompat
                                .checkSelfPermission(MainActivity.this,
                                        android.Manifest.permission.ACCESS_FINE_LOCATION);
                        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                            Log.e(TAG, "onResult: GRANTed");
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied.
                        // But could be fixed by showing the user a dialog.
                        Log.e("Code", "Req");
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            // Ask to turn on GPS automatically
                            status.startResolutionForResult(MainActivity.this,
                                    REQUEST_CHECK_SETTINGS_GPS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("Code", "Un");
                        // Location settings are not satisfied.
                        // However, we have no way
                        // to fix the
                        // settings so we won't show the dialog.
                        // finish();
                        break;
                }
            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @SuppressLint("ShowToast")
    @Override
    public void onLocationChanged(Location location) {
        proseslokasi = 1;

        /*
        Log.e(TAG,
                "onLocationChanged: " + location.getLatitude() + " , " + location.getLongitude());
*/
        textproses.setText("Memuat Lokasi Terkini");
        ihours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        imenute = Calendar.getInstance().get(Calendar.MINUTE);
        Log.e(TAG, ihours + ":" + imenute);
        try {
            Geocoder geo = new Geocoder(
                    MainActivity.this.getApplicationContext(),
                    Locale.getDefault());
            addresses = geo.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            if (imenute != MenitSekarang) {
                MenitSekarang = imenute;
//                ambilwaktusholat();
                prayerTimes = new PrayerTimes(Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                        Calendar.getInstance().get(Calendar.MONTH) + 1,
                        Calendar.getInstance().get(Calendar.YEAR), location.getLatitude(),
                        location.getLongitude(),
                        Calendar.getInstance().getTimeZone().getRawOffset() / (1000 * 60 * 60),
                        false, PTC_MAZHAB_HANAFI, PTC_WAY_MWL);
                Date[] date = prayerTimes.get();
                Log.e("Prayer", String.format("%s %s %s %s %s %s", format.format(date[0]), date[1], date[2], date[3], date[4], date[5]));
                subuh = format.format(date[0]);
                terbit = format.format(date[1]);
                dhuhur = format.format(date[2]);
                ashar = format.format(date[3]);
                magrib = format.format(date[4]);
                ishak = format.format(date[5]);
                textproses.setText("Memuat Jadwal Sholat");
                waktusholat();
            }
            if (addresses.isEmpty()) {
                Toast.makeText(MainActivity.this, "Belum Dapat",
                        Toast.LENGTH_SHORT);
            } else {
                String KotaKab = addresses.get(0).getSubAdminArea();
                KotaKab = KotaKab.replace("Kabupaten ", "");
                KotaKab = KotaKab.replace("Kota ", "");
                KotaLokasi.setText(
                        String.format("%s,%s", KotaKab, addresses.get(0).getCountryName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(
                this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onDestroy() {
        if (runnable != null) handler.removeCallbacks(runnable);
        super.onDestroy();
    }


    private void ambilslider() {


        @SuppressLint("StaticFieldLeak")
        class AMBILSLIDDER extends AsyncTask<Void, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                textproses.setText("Memuat Kegiatan FKAM Terkini");
            }

            @SuppressLint("SimpleDateFormat")
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray(Akses.TAG_SLIDDER_ARRAY);
                    List<Slidder> slidderArrayList = new ArrayList<>();
                    for (int i = 0; i < 6; i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Slidder slidder = new Slidder();
                        slidder.settitle(object.getString(Akses.SLIDDER0));
                        slidder.setImage(object.getString(Akses.SLIDDER1));
                        slidder.seturl(object.getString(Akses.SLIDDER2));
                        slidder.setdate(object.getString(Akses.SLIDDER3));
                        slidderArrayList.add(slidder);
                    }
                    DataSlidder = slidderArrayList;
                    initComponent();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(Void... v) {
                String res;
                Proses rh = new Proses();
                String url = Akses.API_SLIDDER;
                res = rh.sendGetRequest(url);

                Log.e("AMBILSLIDDERi ", res);
                return res;
            }
        }

        AMBILSLIDDER AS = new AMBILSLIDDER();
        AS.execute();
    }

    //Tombol Dialog Tentang App
    private void showDialogTentang() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //sebelumya
        dialog.setContentView(R.layout.dialog_tentang_app);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView) dialog.findViewById(R.id.tv_version)).setText(
                "Versi " + BuildConfig.VERSION_NAME);

        (dialog.findViewById(R.id.bt_getcode)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                Toast.makeText(getApplicationContext(), "ke web clicked",
                        Toast.LENGTH_SHORT).show();
*/
                //Intent intentWeb = new Intent(Intent.ACTION_VIEW);
                //intentWeb.setData(Uri.parse("https://rawzadigital.com"));
                //startActivity(intentWeb);
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://rawzadigital.com")));
            }
        });

        (dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        (dialog.findViewById(R.id.bt_rate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.rateAction(MainActivity.this);
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    private void waktusholat() {
        int jam, menit;
        jam = Integer.parseInt(subuh.substring(0, 2));
        menit = Integer.parseInt(subuh.substring(3, 5));
        Log.e(TAG, "waktusholat: " + jam + ":" + subuh.substring(0, 2));
        Log.e(TAG, "III: " + ihours);
        if (ihours < jam) {
            WaktuAdzan.setText(subuh);
            WaktuSholat.setText("Sholat Subuh");
        } else {
            if (ihours == jam & imenute < menit) {
                WaktuAdzan.setText(subuh);
                WaktuSholat.setText("Sholat Subuh");
            } else {
                jam = Integer.parseInt(dhuhur.substring(0, 2));
                menit = Integer.parseInt(dhuhur.substring(3, 5));
                Log.e("S1", ihours + "VS" + jam);
                if (ihours < jam) {
                    WaktuAdzan.setText(dhuhur);
                    WaktuSholat.setText("Sholat Dhuhur");
                } else {
                    if (ihours == jam & imenute < menit) {
                        WaktuAdzan.setText(dhuhur);
                        WaktuSholat.setText("Sholat Dhuhur");
                    } else {
                        jam = Integer.parseInt(ashar.substring(0, 2));
                        menit = Integer.parseInt(ashar.substring(3, 5));
                        Log.e("S2", ihours + "VS" + jam);
                        if (ihours < jam) {
                            WaktuAdzan.setText(ashar);
                            WaktuSholat.setText("Sholat Ashar");
                        } else {
                            if (ihours == jam & imenute < menit) {
                                WaktuAdzan.setText(ashar);
                                WaktuSholat.setText("Sholat Ashar");
                            } else {
                                jam = Integer.parseInt(magrib.substring(0, 2));
                                menit = Integer.parseInt(magrib.substring(3, 5));
                                Log.e("S3", ihours + "VS" + jam);
                                if (ihours < jam) {
                                    WaktuAdzan.setText(magrib);
                                    WaktuSholat.setText("Sholat Magrib");
                                } else {
                                    if (ihours == jam & imenute < menit) {
                                        WaktuAdzan.setText(magrib);
                                        WaktuSholat.setText("Sholat Magrib");
                                    } else {
                                        jam = Integer.parseInt(ishak.substring(0, 2));
                                        menit = Integer.parseInt(ishak.substring(3, 5));
                                        Log.e("S4", ihours + "VS" + jam);
                                        if (ihours < jam) {
                                            WaktuAdzan.setText(ishak);
                                            WaktuSholat.setText("Sholat Isha");
                                        } else {
                                            if (ihours == jam & imenute < menit) {
                                                WaktuAdzan.setText(ishak);
                                                WaktuSholat.setText("Sholat Isha");
                                            } else {
                                                WaktuAdzan.setText(subuh);
                                                WaktuSholat.setText("Sholat Shubuh");
                                                Log.e("S5", ihours + "VS" + jam);
                                            }
                                        }

                                    }
                                }

                            }
                        }

                    }
                }
            }
        }
        textproses.setText("Lokasi Dan Jadwal Sholat Termuat");
        proseswaktu = 1;
    }

    private static class AdapterImageSlider extends PagerAdapter {

        private Activity act;
        private List<Image> items;

        private AdapterImageSlider.OnItemClickListener onItemClickListener;

        // Constructor
        private AdapterImageSlider(Activity activity, List<Image> items) {

            this.act = activity;
            this.items = items;
        }

        public void setOnItemClickListener(AdapterImageSlider.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        public Image getItem(int pos) {
            return items.get(pos);
        }

        public void setItems(List<Image> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final Image o = items.get(position);
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_slider_image, container, false);

            ImageView image = v.findViewById(R.id.image);
            MaterialRippleLayout lyt_parent = v.findViewById(R.id.lyt_parent);
            Tools.displayImageOriginal(act, image, o.thumbnail);
            lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, o);
                    }
                }
            });

            container.addView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);

        }

        private interface OnItemClickListener {
            void onItemClick(View view, Image obj);
        }

    }
}
