package com.rawzadigital.masjidnet.activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rawzadigital.masjidnet.Akses;
import com.rawzadigital.masjidnet.Proses;
import com.rawzadigital.masjidnet.R;
import com.rawzadigital.masjidnet.model.Slidder;
import com.rawzadigital.masjidnet.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    TextInputLayout aemail;
    EditText epassword,repassword;
    Button daftar;
    ProgressBar login_progress;
    RelativeLayout Rloader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        daftar=findViewById(R.id.daftar);
        aemail=findViewById(R.id.aemail);
        epassword=findViewById(R.id.epassword);
        repassword=findViewById(R.id.repassword);
        login_progress=findViewById(R.id.login_progress);
        Rloader=findViewById(R.id.Rloader);
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aemail.getEditText().getText().equals("")){
                    Toast.makeText(SignUpActivity.this, "Silahkan Masukan Email Anda", Toast.LENGTH_SHORT).show();
                }else if (!epassword.getText().toString().equals(repassword.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "Silakan Periksa Password anda", Toast.LENGTH_SHORT).show();
                }else {
                    register();
                }
            }
        });

        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_setting, menu);
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
    private void register() {


        @SuppressLint("StaticFieldLeak")
        class REGISTER extends AsyncTask<Void, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Rloader.setVisibility(View.VISIBLE);
                runProgressDeterminateCircular();
            }

            @SuppressLint("SimpleDateFormat")
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSONObject jsonObject;
                try {
                    String result = new JSONObject(s).getString("result");
                    String status = new JSONObject(result).getString("status");
                    String info = new JSONObject(result).getString("info");
                    Toast.makeText(SignUpActivity.this, info, Toast.LENGTH_SHORT).show();

                    if (status.equals("Berhasil")){
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Rloader.setVisibility(View.GONE);
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                String res;
                Proses rh = new Proses();
                params.put(Akses.REGIS0, aemail.getEditText().getText().toString());
                params.put(Akses.REGIS1, epassword.getText().toString());
                res = rh.sendPostRequest(Akses.API_REGISTER,params);

                Log.e("AMBILSLIDDERi ",res);
                return res;
            }
        }

        REGISTER ARE= new REGISTER();
        ARE.execute();
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
