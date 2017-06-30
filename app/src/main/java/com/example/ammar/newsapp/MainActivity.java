package com.example.ammar.newsapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView tv=null;
    ProgressBar pb = null;
    private RecyclerView mRecyclerView=null;
    private NewsAdapter newsAdapter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.description);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        newsAdapter=new NewsAdapter(this);
        mRecyclerView.setAdapter(newsAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.search)
            new MyNetwork().execute();
        return true;
    }

    class MyNetwork extends AsyncTask<String, Void, ArrayList<NewsItem>>{

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<NewsItem> doInBackground(String... params) {
            NetworkUtils nu =new NetworkUtils();
            try {

                //upload your api key to this variable below
                final String APIKEY="YOUR API KEY HERE";

                return nu.jsonParser(nu.getResponseFromHttpUrl(nu.makeURL("the-next-web", "latest", APIKEY)));
            }
            catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<NewsItem> arr) {
            pb.setVisibility(View.GONE);

                newsAdapter.setNewsItemData(arr);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}
