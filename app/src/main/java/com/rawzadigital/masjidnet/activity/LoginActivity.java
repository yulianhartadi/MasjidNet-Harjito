package com.rawzadigital.masjidnet.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rawzadigital.masjidnet.Akses;
import com.rawzadigital.masjidnet.Proses;
import com.rawzadigital.masjidnet.R;
import com.rawzadigital.masjidnet.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private View parent_view;
    TextView textemai, textpass;
    ProgressBar login_progress;
    RelativeLayout Rloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        parent_view = findViewById(android.R.id.content);
        textemai = findViewById(R.id.textemail);
        textpass = findViewById(R.id.textpass);
        login_progress = findViewById(R.id.login_progress);
        Rloader = findViewById(R.id.Rloader);

        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);

        ((View) findViewById(R.id.forgot_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(parent_view, "Lupa Password", Snackbar.LENGTH_SHORT).show();
            }
        });
        ((View) findViewById(R.id.masuk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textemai.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Silahkan Masukan Email", Toast.LENGTH_SHORT).show();
                }else if (textpass.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Silahkan Masukan Password", Toast.LENGTH_SHORT).show();
                }else {
                    login();
                }
            }
        });
        ((View) findViewById(R.id.sign_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(parent_view, "Daftar", Snackbar.LENGTH_SHORT).show();
                Intent halSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(halSignUp);
            }
        });

        ((View) findViewById(R.id.hal_takmir)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(parent_view, "ke hal takmir", Snackbar.LENGTH_SHORT).show();

                Intent halTakmir = new Intent(LoginActivity.this, TakmirActivity.class);
                startActivity(halTakmir);
            }
        });

    }

    private void login() {


        @SuppressLint("StaticFieldLeak")
        class LOGIN extends AsyncTask<Void, Void, String> {


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
                    if (s.isEmpty()){
                        Toast.makeText(LoginActivity.this, "Server Sedang Bermasalah.", Toast.LENGTH_SHORT).show();
                    }else{
                    String result = new JSONObject(s).getString("result");
                        Log.e("RESULT",result );
                    if (new JSONObject(result).getString("status").equals("user")) {
                        Toast.makeText(LoginActivity.this, "Menu Takmir dalam proses pengerjaan.\nTunggu update berikutnya.", Toast.LENGTH_SHORT).show();
                    } else {
                        String status = new JSONObject(result).getString("status");
                        String info = new JSONObject(result).getString("info");
                        Toast.makeText(LoginActivity.this, info, Toast.LENGTH_LONG).show();
                    }
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
                params.put(Akses.REGIS0, textemai.getText().toString().trim());
                params.put(Akses.REGIS1, textpass.getText().toString().trim());
                res = rh.sendPostRequest(Akses.API_LOGIN, params);

                Log.e("AMBILSLIDDERi ", res);
                return res;
            }
        }

        LOGIN LG = new LOGIN();
        LG.execute();
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
