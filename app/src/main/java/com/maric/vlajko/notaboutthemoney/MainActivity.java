package com.maric.vlajko.notaboutthemoney;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

public class MainActivity extends RoboActionBarActivity implements LoadComplete{

    @InjectView (R.id.myRelativeLayout)
    RelativeLayout myLayout;
    @InjectView (R.id.beingConverted)
    AutoCompleteTextView whatWeConvert;
    @InjectView (R.id.convertInto)
    AutoCompleteTextView whatWeConvertInto;
    @InjectView (R.id.progressBar)
    ProgressBar progressBar;
    @InjectView (R.id.convertButton)
    Button convertButton;
    @InjectView (R.id.amountToConvert)
    EditText editTextAmount;
    @InjectView (R.id.myResult)
    TextView myResult;
    @InjectView(R.id.equal)
    TextView equal;
    ArrayAdapter<String> adapter;
    private String url1,url2;
    float number1,number2,amount;
    static ArrayList <String> countriesList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myResult.setVisibility(myResult.GONE);
        equal.setVisibility(equal.GONE);
        adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line, countriesList);
        myLayout.setVisibility(View.GONE);
        url1 = getResources().getString(R.string.allCurrenciesUrl);
        url2 = getResources().getString(R.string.latestChangesCurrencies)+getResources().getString(R.string.apiID);
        whatWeConvert.setAdapter(adapter);
        whatWeConvert.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid(CharSequence text) {

                for (String word: countriesList) {
                    if(word.equals(whatWeConvert.getText().toString())){
                        return true;
                    }
                }
                Toast.makeText(getApplicationContext(),"Enter valid currency 1",Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public CharSequence fixText(CharSequence invalidText) {
                return null;
            }
        });
        whatWeConvertInto.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid(CharSequence text) {
                for (String word: countriesList) {
                    if(word.equals(whatWeConvertInto.getText().toString())){
                        return true;
                    }
                }
                Toast.makeText(getApplicationContext(),"Enter valid currency 2",Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public CharSequence fixText(CharSequence invalidText) {
                return null;
            }
        });
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                if(!editTextAmount.getText().toString().matches("")&&!whatWeConvertInto.getText().toString().matches("")&&!whatWeConvert.getText().toString().matches("")){
                    AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                       String text1 = whatWeConvert.getText().toString();
                        String text2 = whatWeConvertInto.getText().toString();
                        if(text1.length()>3){
                            for(int i=0;i<MyCurrencyObjArray.currencyObjects.size();i++){
                                if(text1.equals(MyCurrencyObjArray.currencyObjects.get(i).getFullCountryName())){
                                    number1 = Float.parseFloat(MyCurrencyObjArray.currencyObjects.get(i).getValueAgainstDollar());
                                }
                            }
                        }else{
                            for(int i=0;i<MyCurrencyObjArray.currencyObjects.size();i++){
                                if(text1.equals(MyCurrencyObjArray.currencyObjects.get(i).getNameId())){
                                    number1 = Float.parseFloat(MyCurrencyObjArray.currencyObjects.get(i).getValueAgainstDollar());
                                }
                            }
                        }
                        if(text2.length()>3){
                            for(int i=0;i<MyCurrencyObjArray.currencyObjects.size();i++){
                                if(text2.equals(MyCurrencyObjArray.currencyObjects.get(i).getFullCountryName())){
                                    number2 = Float.parseFloat(MyCurrencyObjArray.currencyObjects.get(i).getValueAgainstDollar());
                                }
                            }
                        }else{
                            for(int i=0;i<MyCurrencyObjArray.currencyObjects.size();i++){
                                if(text2.equals(MyCurrencyObjArray.currencyObjects.get(i).getNameId())){
                                    number2 = Float.parseFloat(MyCurrencyObjArray.currencyObjects.get(i).getValueAgainstDollar());
                                }
                            }
                        }
                        amount = (Float.parseFloat(editTextAmount.getText().toString())/number1)*number2;
                        whatWeConvertInto.getText();
                    }
                });
                InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                myResult.setText(Float.toString(amount));
                equal.setVisibility(equal.VISIBLE);
                myResult.setVisibility(myResult.VISIBLE);
                }else {Toast.makeText(getApplicationContext(),getResources().getString(R.string.noAmount),Toast.LENGTH_SHORT).show();}


            }
        });

        whatWeConvertInto.setAdapter(adapter);
        if(!checkIfSaved()) {
            if (isOnline()) {
                DownloadTask task = new DownloadTask(this, progressBar);
                task.execute(url1);
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.noNetwork), Toast.LENGTH_LONG).show();
            }
        }else{loadData();
        if(isOnline()){
            checkVersion();
        }
        }
    }

    @Override
    public void asyncComplete1(boolean success) {
        adapter.notifyDataSetChanged();
        DownloadRelationTask relationTask = new DownloadRelationTask(this);
        relationTask.execute(url2);
    }

    @Override
    public void asyncComplete2(boolean success) {
        myLayout.setVisibility(myLayout.VISIBLE);
        progressBar.setVisibility(progressBar.GONE);
        saveData();
        Toast.makeText(getApplicationContext(),getResources().getString(R.string.dataUpdatedAndSaved),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void asyncComplete3(boolean success) {
        if(success){
        DownloadRelationTask relationTask = new DownloadRelationTask(this);
        relationTask.execute(url2);}
        else {Toast.makeText(getApplicationContext(),"Data is up to date.",Toast.LENGTH_SHORT).show();}
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    private void saveData(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(MyCurrencyObjArray.currencyObjects);
        editor.putString("Timestamp",MyCurrencyObjArray.timeStamp);
        editor.putString("Data", json);
        editor.commit();
    }
    private void loadData(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        MyCurrencyObjArray.timeStamp = sharedPrefs.getString("Timestamp", null);
        String json = sharedPrefs.getString("Data", null);
        Type type = new TypeToken<ArrayList<MyCurrencyObject>>() {}.getType();
        MyCurrencyObjArray.currencyObjects = gson.fromJson(json, type);
        for(MyCurrencyObject object:MyCurrencyObjArray.currencyObjects){
            countriesList.add(object.getFullCountryName());
            countriesList.add(object.getNameId());
        }
        adapter.setNotifyOnChange(true);
        myLayout.setVisibility(myLayout.VISIBLE);
        progressBar.setVisibility(progressBar.GONE);

    }
    private boolean checkIfSaved(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(sharedPrefs.contains("Data"))
        {return true;}
        return false;
    }
    private void checkVersion(){
        DataVersionCheck versionCheck = new DataVersionCheck(this);
        versionCheck.execute(url1);
    }
}
