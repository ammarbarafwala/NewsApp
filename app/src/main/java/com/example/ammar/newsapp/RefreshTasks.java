/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.ammar.newsapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.example.ammar.newsapp.data.DBHelper;
import com.example.ammar.newsapp.data.DatabaseUtils;
import com.example.ammar.newsapp.data.NewsItem;

//This class gets the whole data from url and adds it to database

public class RefreshTasks {

    public static final String ACTION_REFRESH = "refresh";


    public static void refreshArticles(Context context) {
        ArrayList<NewsItem> result = null;
        final String APIKEY="deaadf14b6cd4e91b13cf6a54e10e67b";
        URL url = NetworkUtils.makeURL("the-next-web", "latest", APIKEY);

        SQLiteDatabase db = new DBHelper(context).getWritableDatabase();

        try {
            //deleting table from database
            DatabaseUtils.deleteAll(db);
            //getting json data from url
            String json = NetworkUtils.getResponseFromHttpUrl(url);
            //converting json data into list
            result = NetworkUtils.jsonParser(json);
            //adding data list to database
            DatabaseUtils.bulkInsert(db, result);

        } catch (IOException e) {
            e.printStackTrace();

        }

        db.close();
    }
}