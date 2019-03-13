package com.rawzadigital.masjidnet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.rawzadigital.masjidnet.adapter.AdapterMasjid;
import com.rawzadigital.masjidnet.model.ModelMasjid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AsyncTack extends AsyncTask<String, String, String> {

    private String TAG = AsyncTack.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    private TextView textView;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private int hal = 1;
    private String url;
    private List<ModelMasjid> datamasjid;


    public AsyncTack(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    //dijalankan pertama kali
    @Override
    protected void onPreExecute() {
        Log.e(TAG, "Start Asynctask");
    }

    @SuppressLint("WrongThread")
    @Override
    protected String doInBackground(String... strings) {
        OkHttpClient okHttpClient = new OkHttpClient();
        url = strings[0];
        if (url == "") {
            List<ModelMasjid> data = new ArrayList<>();
            ModelMasjid ModelMasjid = new ModelMasjid();
            ModelMasjid.setName("");
            ModelMasjid.setaddress("");
            ModelMasjid.setlatitude("");
            ModelMasjid.setlongitude("");
            ModelMasjid.setImage("");
            data.add(ModelMasjid);

            AdapterMasjid AdapterMasjid = new AdapterMasjid(data, context);
            recyclerView.setAdapter(AdapterMasjid);
        } else {
            //request ambil data
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try {
                //get request data dari api bukalapak
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //dijalankan setelah doInBackground
    @Override
    protected void onPostExecute(String s) {
        if (s != null) {
            //tampilkan data json result
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

                AdapterMasjid AdapterMasjid = new AdapterMasjid(data, context);
                recyclerView.setAdapter(AdapterMasjid);
                AdapterMasjid.setOnItemClickListener(new AdapterMasjid.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, ModelMasjid obj, int position) {

                        Snackbar.make(view, "Item " + obj.name + " clicked", Snackbar.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Lihat Detail Informasi Masjid "+obj.name+" ?");
                        builder.setMessage("Alamat"+obj.getaddress());
                        builder.show();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public static List<ModelMasjid> getMasjidData() {

        return null;
    }

}
