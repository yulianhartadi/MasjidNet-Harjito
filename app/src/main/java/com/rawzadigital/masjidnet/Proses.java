package com.rawzadigital.masjidnet;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Proses {
    public String sendGetRequest(String ss) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(ss);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String s;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s).append("\n");
            }
        } catch (Exception ignored) {
        }
        return sb.toString();
    }
    String sendGetRequest2(String slinkapi) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(slinkapi);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String s;
            sb.append("{\"").append(Akses.TAG_JSON_ARRAY).append("\":[");
            while ((s = bufferedReader.readLine()) != null) {
                if (s.indexOf("lat")>0 & s.indexOf("lat")<19){
                    sb.append("{").append(s);
//                    Log.e("Test ", s +s.indexOf("lat"));
                }
                if (s.indexOf("lng")>0& s.indexOf("lng")<19){
                    sb.append(s).append(",");
//                    Log.e("Test ", s +s.indexOf("lng"));
                }
                if (s.indexOf("name")>0){
                    sb.append(s.replace(",","")).append("},");
//                    Log.e("Test ", s +s.indexOf("name"));
                }
            }
            sb.append("]}");
        } catch (Exception ignored) {
        }
        return sb.toString();
    }


    public String sendPostRequest(String requestURL,
                           HashMap<String, String> postDataParams) {
        //Membuat URL
        URL url;

        //Objek StringBuilder untuk menyimpan pesan diambil dari server
        StringBuilder sb = new StringBuilder();
        try {
            //Inisialisasi URL
            url = new URL(requestURL);

            //Membuat Koneksi HttpURLConnection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //Konfigurasi koneksi
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            //Membuat Keluaran Stream
            OutputStream os = conn.getOutputStream();

            //Menulis Parameter Untuk Permintaan
            //Kita menggunakan metode getPostDataString yang didefinisikan di bawah ini
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                String response;
                //Reading server response
                while ((response = br.readLine()) != null) {
                    sb.append(response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            Log.e("EEENNNTRY", entry.toString());
        }

        return result.toString();
    }
}
