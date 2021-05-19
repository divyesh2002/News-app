package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>{

    private static final String NEWS_REQUEST_URL ="https://newsdata.io/api/1/news?apikey=pub_1388ed792806453aa6a1476977d92210fdf&language=en&category=";

    private static final int NEWS_LOADER_ID = 1;

    private ListView listView;
    private newsAdaptor madaptor;
    private TextView emptyview;
    private TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=findViewById(R.id.list);
        emptyview=findViewById(R.id.empty_view);
        header=findViewById(R.id.catogarytextview);

        madaptor=new newsAdaptor(this,new ArrayList<News>());
        listView.setAdapter(madaptor);
        listView.setEmptyView(emptyview);

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected)
        {
            LoaderManager loaderManager;
            loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        }
        else
        {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            emptyview.setText("No INTERNET connection");
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News current=madaptor.getItem(position);

                Uri go_to_website=Uri.parse(current.getMurl());

                Intent get_web=new Intent(Intent.ACTION_VIEW,go_to_website);

                startActivity(get_web);
            }
        });

    }


    @NonNull
    public Loader<List<News>> onCreateLoader(int i, @Nullable Bundle bundle) {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        String catogary=sharedPreferences.getString(getString(R.string.settings_catogary_key),"top");
        header.setText(catogary);
        String tamp_url=NEWS_REQUEST_URL;
        tamp_url=tamp_url+catogary;
        Uri baseuri=Uri.parse(tamp_url);
        //Uri.Builder builder=baseuri.buildUpon();

        return new newsLoader(this,baseuri.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        madaptor.clear();
        emptyview.setText(R.string.No_news);
        if(data!=null&&!data.isEmpty())
        {
            madaptor.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
         madaptor.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_settings)
        {
            Intent settingintent=new Intent(this,settings_activity.class);
            startActivity(settingintent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}