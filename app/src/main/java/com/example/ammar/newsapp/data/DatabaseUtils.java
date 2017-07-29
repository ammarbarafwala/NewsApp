package com.example.ammar.newsapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static com.example.ammar.newsapp.data.Contract.TABLE_NEWS.*;
import java.util.ArrayList;

// created to add or delete data from/to the database
public class DatabaseUtils {

    public static Cursor getAll(SQLiteDatabase db) {
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_NAME_PUBLISHED_AT + " DESC"
        );
        return cursor;
    }

    //inserts list of data to database
    public static void bulkInsert(SQLiteDatabase db, ArrayList<NewsItem> newsItems) {

        db.beginTransaction();
        try {
            for (NewsItem n : newsItems) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_NAME_TITLE, n.getTitle());
                cv.put(COLUMN_NAME_AUTHOR, n.getAuthor());
                cv.put(COLUMN_NAME_DESCRIPTION, n.getDescription());
                cv.put(COLUMN_NAME_URL, n.getUrl());
                cv.put(COLUMN_NAME_URL_TO_IMAGE, n.getUrlToImage());
                cv.put(COLUMN_NAME_PUBLISHED_AT, n.getPublishedAt());
                db.insert(TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    //deletes whole table
    public static void deleteAll(SQLiteDatabase db) {
        db.delete(TABLE_NAME, null, null);
    }

}
