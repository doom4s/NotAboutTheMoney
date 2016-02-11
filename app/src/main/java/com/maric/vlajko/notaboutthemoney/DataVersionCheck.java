package com.maric.vlajko.notaboutthemoney;

import android.os.AsyncTask;
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
        if(MyCurrencyObjArray.timeStamp!="") {
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
        }
        return result;
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(result!=""){
        final String result1 = result;
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(result1);
                    if(MyCurrencyObjArray.timeStamp==jsonObject.get("timestamp").toString()){
                        isNew = false;
                    }else{isNew = true;}
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();}

        if(isNew){
            complete.asyncComplete3(true);
        }else {
            complete.asyncComplete3(false);
        }
    }
}