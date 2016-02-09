package com.maric.vlajko.notaboutthemoney;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

public class MainActivity extends RoboActionBarActivity implements LoadComplete{

    public static ArrayList<String> myCurrenciesList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    @InjectView (R.id.myCurrenciesListView)
    ListView currenciesListView;
    @InjectView (R.id.progressBar)
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask(this, progressBar);
        task.execute(getResources().getString(R.string.allCurrenciesUrl));
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.my_text_view,myCurrenciesList);
        currenciesListView.setAdapter(arrayAdapter);
        currenciesListView.setVisibility(View.GONE);
    }

    @Override
    public void asyncComplete(boolean success) {
        arrayAdapter.notifyDataSetChanged();
        currenciesListView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(progressBar.GONE);
    }
}
