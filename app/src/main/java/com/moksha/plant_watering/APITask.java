package com.moksha.plant_watering;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by sanjay.rajput on 20/06/18.
 */

public class APITask extends AsyncTask<Void, Void, String> {
    private static final Logger logger = Logger.getLogger("APITask");
    private String url;
    private String method;
    private Map<String, String> headers;
    private Map<String, String> body;
    private String bodyJson;

    public APITask(String url, String method, Map<String, String> headers, Map<String, String> body) {
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.body = body;
    }

    @Override
    protected void onPreExecute() {
        Gson gson = new Gson();
        super.onPreExecute();
        System.out.println("url: " + url);
        System.out.println("method: " + method);
        System.out.println("headers: " + headers);
        if (body != null) {
            bodyJson = gson.toJson(body);
            System.out.println(bodyJson);
        }
    }

    @Override
    public String doInBackground(Void... voids) {
        String response = null;
        try {
            URL url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            try {
                conn.setRequestMethod(method);
                if (headers != null) {
                    for (String hKey : headers.keySet()) {
                        conn.setRequestProperty(hKey, headers.get(hKey));
                    }
                }
                if (bodyJson != null) {
                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                    wr.write(bodyJson.getBytes());
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    responseBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                response = responseBuilder.toString();
            } finally {
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        System.out.println("Response: " + response);
    }
}
