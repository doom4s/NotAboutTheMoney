package com.maric.vlajko.notaboutthemoney;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vlajko on 11-Feb-16.
 */
public class DataVersionCheck extends AsyncTask<String, String, String> {
    LoadComplete complete;
    boolean isNew = true;
    public DataVersionCheck(LoadComplete complete){
        this.complete = complete;
    }
    @Override
    protected String doInBackground(String... urls) {
        String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = inputStreamReader.read();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        return result;
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        final String result1 = result;
            Log.i("DATA",MyCurrencyObjArray.timeStamp);
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(result1);
                    Log.i("OVDEPRIMETI",MyCurrencyObjArray.timeStamp+" : "+jsonObject.getString("timestamp").toString());
                    if(MyCurrencyObjArray.timeStamp==jsonObject.getString("timestamp").toString()){
                        isNew = true;

                    }else{isNew = false;}
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Log.i("ISNEW",String.valueOf(isNew));
        if(isNew){
            complete.asyncComplete3(isNew);
        }else {
            complete.asyncComplete3(isNew);
        }
    }
}