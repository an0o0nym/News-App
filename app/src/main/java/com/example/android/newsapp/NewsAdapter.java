package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.R.attr.category;

/**
 * Created by an0o0nym on 18/07/17.
 */

public class NewsAdapter extends ArrayAdapter<News>{
    public NewsAdapter(Context c, List<News> news) {
        super(c, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView titleView = (TextView) listItemView.findViewById(R.id.news_title);
        TextView publishedDateView = (TextView) listItemView.findViewById(R.id.news_published_date);
        TextView categoryView = (TextView) listItemView.findViewById(R.id.news_category);
        TextView authorView = (TextView) listItemView.findViewById(R.id.news_author);

        titleView.setText(currentNews.getTitle());
        if (currentNews.hasPublishedDate()) {
            publishedDateView.setText(formatDate(currentNews.getPublished()));
        } else {
            publishedDateView.setVisibility(View.GONE);
        }
        categoryView.setText(currentNews.getCategory());
        if(currentNews.hasAuthor()) {
            authorView.setText("By: " + currentNews.getAuthor());
        } else {
            authorView.setText("By: Unknown");
        }

        return listItemView;

    }

    private static String formatDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(date);
    }
}
