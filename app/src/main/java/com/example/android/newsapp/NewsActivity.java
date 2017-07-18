package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>{

    private static final String LOG_TAG = News.class.getSimpleName();
    private static final String REQUEST_URL =
            "http://content.guardianapis.com/search?section=technology&api-key=test";
    private static final int NEWS_LOADER_ID = 1;

    private NewsAdapter mAdapter;
    private TextView emptyView;
    private ProgressBar loadView;

    private ConnectivityManager connMgr;
    private NetworkInfo netInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ListView newsListView = (ListView) findViewById(R.id.list);
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(mAdapter);

        emptyView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(emptyView);

        loadView = (ProgressBar) findViewById(R.id.load_view);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                News currentNews = mAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(webIntent);
            }
        });

        if(hasInternetConn()) {
            getLoaderManager().initLoader(NEWS_LOADER_ID, null, NewsActivity.this);
        }

    }

    /**
     * Helper method used to determine whether device has internet connection
     *
     * @return true if device has internet connection, false otherwise.
     */
    private boolean hasInternetConn() {
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = connMgr.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            mAdapter.clear();
            loadView.setVisibility(View.GONE);
            emptyView.setText(getString(R.string.no_internet_connection));
            return false;
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("from-date", generatePastDate());

        return new NewsLoader(this, baseUri.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        mAdapter.clear();
        loadView.setVisibility(View.GONE);

        if(news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        } else {
            emptyView.setText(getString(R.string.no_news_found));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }

    /**
     * Helper method used to return date that was 7 days ago.
     * @return String containing date in format yyyy-MM-dd
     */
    private static String generatePastDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        return df.format(calendar.getTime());
    }
}
