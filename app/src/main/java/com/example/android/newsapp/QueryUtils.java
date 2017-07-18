package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created by an0o0nym on 18/07/17.
 */

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * No one should be able to create QueryUtils object.
     */
    public QueryUtils() { }

    public static List<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem when making the request.", e);
        }

        List<News> newsData = extractNewsData(jsonResponse);

        return newsData;
    }


    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem when building the URL");
        }

        return url;
    }

    private static String makeRequest(URL url) throws IOException{
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConn = null;
        InputStream streamIn = null;

        try {
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setReadTimeout(10000);
            urlConn.setConnectTimeout(15000);
            urlConn.setRequestMethod("GET");
            urlConn.connect();

            if (urlConn.getResponseCode() == 200) {
                streamIn = urlConn.getInputStream();
                jsonResponse = readFromStream(streamIn);
            } else {
                Log.e(LOG_TAG, "Error! Response code: " + urlConn.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem when retrieving news data.", e);
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }
            if (streamIn != null) {
                streamIn.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream streamIn) throws  IOException{
        StringBuilder sb = new StringBuilder();

        if (streamIn != null) {
            InputStreamReader streamInReader = new InputStreamReader(streamIn, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(streamInReader);
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
        }

        return sb.toString();
    }

    private static List<News> extractNewsData(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        Log.v(LOG_TAG, "jsonResponse: " + jsonResponse);

        List<News> newsData = new ArrayList<>();
        try {
            JSONObject baseResponse = (new JSONObject(jsonResponse)).getJSONObject("response");
            JSONArray newsArray = baseResponse.getJSONArray("results");
            int numOfNews = newsArray.length();

            for (int i = 0; i < numOfNews; i++) {
                JSONObject currentNews = newsArray.getJSONObject(i);
                String title = currentNews.getString("webTitle");
                String category = currentNews.getString("sectionName");
                String url = currentNews.getString("webUrl");
                String datePublishedStr = currentNews.getString("webPublicationDate");

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Date datePublished = null;
                try {
                    datePublished = df.parse(datePublishedStr);
                } catch (ParseException e) {
                    Log.e(LOG_TAG, "Problem when parsing date published.", e);
                }
                News news = new News(title, category, url, datePublished);
                newsData.add(news);
                Log.v(LOG_TAG, "\nNews object:" + news.toString());
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem when parsing JSON response.", e);
        }

        return newsData;
    }

}
