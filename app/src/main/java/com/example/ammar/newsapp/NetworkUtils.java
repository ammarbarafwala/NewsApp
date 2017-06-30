package com.example.ammar.newsapp;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Ammar on 6/21/2017.
 */

public class NetworkUtils {
    final static String BASE_URL = "https://newsapi.org/v1/articles";
    final static String QUERY_PARAMETER = "source";
    final static String TAG = "NetworkUtils";
    final static String PARAM_SORT = "sortBy";
    final static String PARAM_API_Key= "apiKey";

    public static URL makeURL(String searchQuery, String sortBy, String apiKey){
        Uri uri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(QUERY_PARAMETER,searchQuery)
                .appendQueryParameter(PARAM_SORT, sortBy)
                .appendQueryParameter(PARAM_API_Key, apiKey).build();
        URL url = null;
        try{
            String urlString=uri.toString();
            Log.d(TAG, "URL: "+urlString);
            url= new URL(urlString);
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }
    public  static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
        try {
            InputStream in=urlConnection.getInputStream();
            Scanner input=new Scanner(in);
            input.useDelimiter("\\A");
            return (input.hasNext())? input.next():null;
        }finally {

            urlConnection.disconnect();
        }
    }
    public  static ArrayList<NewsItem> jsonParser(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray=jsonObject.getJSONArray("articles");
            ArrayList<NewsItem> arr=new ArrayList<>();
            for (int i=0; i< jsonArray.length() ; i++ ) {
                JSONObject jobj=jsonArray.getJSONObject(i);
                arr.add(new NewsItem(jobj.getString("author"), jobj.getString("title"), jobj.getString("description"),
                        jobj.getString("url"), jobj.getString("urlToImage"), jobj.getString("publishedAt")));
            }
            return arr;
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
