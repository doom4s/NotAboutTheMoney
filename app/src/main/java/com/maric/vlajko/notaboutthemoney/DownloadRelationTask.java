package com.maric.vlajko.notaboutthemoney;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Vlajko on 10-Feb-16.
 */
public class DownloadRelationTask extends AsyncTask<String, String, String> {
    private LoadComplete complete;
    public DownloadRelationTask(LoadComplete complete){
        this.complete = complete;
    }
    @Override
    protected String doInBackground(String... urls) {
        String result = "";
        URL url ;
        HttpURLConnection urlConnection = null;
        try{
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            int data = inputStreamReader.read();
            while (data!=-1){
                char current = (char) data;
                result+=current;
                data = inputStreamReader.read();
            }

        }catch(IOException e){e.printStackTrace();}
        return result;
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        final String result1 = result;
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    ArrayList<String> keys = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(result1);
                    JSONObject jsonObjectRates = jsonObject.getJSONObject("rates");
                    MyCurrencyObjArray.timeStamp = jsonObject.get("timestamp").toString();
                    JSONArray jsonArray = new JSONArray();
                    Iterator iterator = jsonObjectRates.keys();

                    while(iterator.hasNext()){
                        String key = (String) iterator.next();
                        if(key!=""){
                            keys.add(key);
                            jsonArray.put(jsonObjectRates.get(key));
                            MainActivity.countriesList.add(key);
                        }
                    }
                    if(MyCurrencyObjArray.currencyObjects.size()!=0){
                    for(int i = 0 ;i<jsonArray.length();i++){
                        MyCurrencyObject object;
                        object = MyCurrencyObjArray.currencyObjects.get(i);
                        object.setValueAgainstDollar(jsonObjectRates.getString(keys.get(i)));
                    }
                    for (MyCurrencyObject object:MyCurrencyObjArray.currencyObjects){
                    }}

                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        complete.asyncComplete2(true);

    }
}
