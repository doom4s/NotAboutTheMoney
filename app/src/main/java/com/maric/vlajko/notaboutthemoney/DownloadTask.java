package com.maric.vlajko.notaboutthemoney;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

/**
 * Created by Vlajko on 09-Feb-16.
 */
public class DownloadTask extends AsyncTask<String,Integer,String> {

    private LoadComplete complete;
    private ProgressBar progressBar;
    public DownloadTask(LoadComplete complete, ProgressBar progressBar){
        this.complete = complete;
        this.progressBar = progressBar;
    }

    @Override
    protected String doInBackground(String... urls) {
        String result = "";
        URL url;
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
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = new JSONArray();
            Iterator iterator = jsonObject.keys();
            while(iterator.hasNext()){
                String key = (String) iterator.next();
                jsonArray.put(jsonObject.get(key));
            }
            for(int i = 0 ;i<jsonArray.length();i++){
                MainActivity.myCurrenciesList.add(jsonArray.get(i).toString());
            }
            complete.asyncComplete(true);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}