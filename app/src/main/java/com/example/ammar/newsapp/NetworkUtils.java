package com.example.ammar.newsapp;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
}
