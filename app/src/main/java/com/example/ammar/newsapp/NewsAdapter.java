package com.example.ammar.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ammar on 6/29/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {

    ArrayList<NewsItem> newsItemData=null;
    Context context=null;
    NewsAdapter(Context c){
        context=c;
    }

    public ArrayList<NewsItem> getNewsItemData() {
        return newsItemData;
    }

    public void setNewsItemData(ArrayList<NewsItem> newsItemData) {
        this.newsItemData = newsItemData;
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.news_item_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder holder, int position) {
        final NewsItem newsItem= newsItemData.get(position);
        holder.description.setText(newsItem.getDescription());
        holder.title.setText(newsItem.getTitle());
        holder.time.setText(newsItem.getPublishedAt());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(newsItem.getUrl()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(newsItemData!=null)
            return newsItemData.size();
        return 0;
    }

    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder{
        final TextView title, description, time;
        public NewsAdapterViewHolder(View itemView) {
            super(itemView);
            time=(TextView) itemView.findViewById(R.id.time);
            title=(TextView) itemView.findViewById(R.id.title);
            description=(TextView)itemView.findViewById(R.id.description);
        }
    }
}
