package com.maric.vlajko.notaboutthemoney;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import roboguice.activity.RoboActionBarActivity;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class MainActivity extends RoboActionBarActivity implements LoadComplete{

    public static ArrayList<String> myCurrenciesList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    @InjectView (R.id.myCurrenciesListView)
    ListView currenciesListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask(this);
        task.execute(getResources().getString(R.string.allCurrenciesUrl));
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.my_text_view,myCurrenciesList);
        currenciesListView.setAdapter(arrayAdapter);
    }

    @Override
    public void asyncComplete(boolean success) {
        arrayAdapter.notifyDataSetChanged();
    }
}
