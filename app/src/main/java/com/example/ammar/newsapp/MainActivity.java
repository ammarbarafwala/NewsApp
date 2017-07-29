package com.example.ammar.newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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

import java.io.IOException;
import java.util.ArrayList;

import com.example.ammar.newsapp.data.Contract;
import com.example.ammar.newsapp.data.DBHelper;
import com.example.ammar.newsapp.data.DatabaseUtils;
import com.example.ammar.newsapp.data.NewsItem;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsItem>>{

    TextView tv=null;
    ProgressBar pb = null;
    private RecyclerView mRecyclerView=null;
    private NewsAdapter newsAdapter=null;
    private static final int NEWS_LOADER = 1;
    private Cursor cursor;
    private SQLiteDatabase db;

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

        //Taking out value from SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirst = prefs.getBoolean("isfirst", true);
        Log.d("Ammar",isFirst+" hi");

        /*checking if data stored in shared preferences is first time, if it is then app is installed first
         time so call load method to get data and store in database and show in recycler view */
        if (isFirst) {
            load();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isfirst", false);
            editor.commit();
        }
        ScheduleUtilities.scheduleRefresh(this);

    }

    //getting data from database to add it to recycler view as the app was in pause mode
    @Override
    protected void onStart() {
        super.onStart();
        pb.setVisibility(View.GONE);
        db = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(db);
        newsAdapter=new NewsAdapter(cursor);
        mRecyclerView.setAdapter(newsAdapter);
        newsAdapter.notifyDataSetChanged();
    }

    //closing database and cursor objects
    @Override
    protected void onStop() {
        super.onStop();
        db.close();
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //calling the AsyncTaskLoader to refresh data
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.search) {
            load();
        }
        return true;
    }
    // Instead of AsyncTask using AsyncTaskLoader methods
    @Override
    public Loader<ArrayList<NewsItem>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<NewsItem>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                pb.setVisibility(View.VISIBLE);
            }

            @Override
            public ArrayList<NewsItem> loadInBackground() {
                RefreshTasks.refreshArticles(MainActivity.this);
                return null;
            }


        };
    }

    //notifying adapter about data changed to view it on the recycler view
    @Override
    public void onLoadFinished(Loader<ArrayList<NewsItem>> loader, ArrayList<NewsItem> data) {
        pb.setVisibility(View.GONE);

        db = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(db);
        newsAdapter=new NewsAdapter(cursor);
        mRecyclerView.setAdapter(newsAdapter);
        newsAdapter.notifyDataSetChanged();
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<NewsItem>> loader) {
    }
    //starts asynctaskloader to work in background
    public void load() {
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(NEWS_LOADER, null, this).forceLoad();
    }

}
