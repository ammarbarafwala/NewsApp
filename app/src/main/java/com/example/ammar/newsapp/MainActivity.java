package com.example.ammar.newsapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tv=null;
    ProgressBar pb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView);
        pb = (ProgressBar) findViewById(R.id.progressBar);
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

    class MyNetwork extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            NetworkUtils nu =new NetworkUtils();
            try {

                //upload your api key to this variable below
                final String APIKEY="";
                return nu.getResponseFromHttpUrl(nu.makeURL("the-next-web", "latest", APIKEY));
            }
            catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            pb.setVisibility(View.GONE);
            if(s==null)
                tv.setText("Sorry, no text was recieved!");
            else
                tv.setText(s);
        }
    }
}
