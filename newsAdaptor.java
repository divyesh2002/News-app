package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class newsAdaptor extends ArrayAdapter<News> {

    public newsAdaptor(@NonNull Context context, List<News> newsList) {
        super(context, 0,newsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listview=convertView;
        if(listview==null)
        {
            listview= LayoutInflater.from(getContext()).inflate(R.layout.custom_layout,parent,false);
        }
        News news=getItem(position);

        TextView title=(TextView) listview.findViewById(R.id.Titleofnews);
        title.setText(news.getMtitle());

        TextView date=(TextView) listview.findViewById(R.id.Date);
        date.setText(news.getMpub_date());

        TextView des=(TextView) listview.findViewById(R.id.Descreption);
        des.setText(news.getMdescreption());

        TextView from=(TextView) listview.findViewById(R.id.newsfrom);
        from.setText(news.getMnewsfrom());

        return listview;
    }
}
