package com.example.ammar.newsapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.ammar.newsapp.data.Contract;
import com.example.ammar.newsapp.data.NewsItem;
import com.squareup.picasso.Picasso;

/**
 * Created by Ammar on 6/29/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {

    Cursor cursor;
    Context context;
    NewsAdapter(Cursor cursor){
        this.cursor=cursor;
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.news_item_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder holder, final int position) {
        holder.bind(position);

        // adding click event listener to each view of holder
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor.moveToPosition(position);
                String url = cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_URL));
                Log.d("Here", String.format("Url %s", url));

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            }
        });
    }


    public int getItemCount() {
        return cursor.getCount();
    }

    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder{

        final TextView title, description, time;
        ImageView img;

        public NewsAdapterViewHolder(View itemView) {
            super(itemView);
            time=(TextView) itemView.findViewById(R.id.time);
            title=(TextView) itemView.findViewById(R.id.title);
            description=(TextView)itemView.findViewById(R.id.description);
            img = (ImageView)itemView.findViewById(R.id.img);
        }

        public void bind(int position) {

            //attaching data to each view to be shown with its image
            cursor.moveToPosition(position);
            title.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_TITLE)));
            time.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_PUBLISHED_AT)));
            description.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_DESCRIPTION)));
            String url = cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_URL_TO_IMAGE));
            Log.d("NewsAdapter", url);
            if(url != null){
                Picasso.with(context)
                        .load(url)
                        .into(img);
            }
        }
    }
}
