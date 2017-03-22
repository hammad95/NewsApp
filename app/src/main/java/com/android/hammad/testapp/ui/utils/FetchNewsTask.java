package com.android.hammad.testapp.ui.utils;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.android.hammad.testapp.R;
import com.android.hammad.testapp.ui.adapters.NewsListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Hassan on 3/20/2017.
 */

public class FetchNewsTask extends AsyncTask<String, Void, ArrayList<ContentValues>> {

    // Sort by argument for fetching news data from newsapi
    private final String SORT_FILTER = "latest";

    Context mContext;

    // Array holding the news sources to fetch news from
    String[] mNewsSourcesArray;

    // A weak reference to the RecyclerView passed into the constructor of this class
    WeakReference<RecyclerView> mRecyclerViewReference;

    public FetchNewsTask(Context context, RecyclerView recyclerView) {
        mContext = context;

        // Get a weak reference to the RecyclerView inside NewsFragment
        mRecyclerViewReference = new WeakReference<>(recyclerView);

        // Get the source names array
        mNewsSourcesArray = context.getResources().getStringArray(R.array.array_news_sources);
    }

    @Override
    protected void onPostExecute(final ArrayList<ContentValues> resultsArray) {
        super.onPostExecute(resultsArray);

        // Get the RecyclerView from the WeakReference
        RecyclerView recyclerView = mRecyclerViewReference.get();

        // Set a NewsListAdapter to the RecyclerView with the newly fetched data
        recyclerView.setAdapter(
                new NewsListAdapter(recyclerView, resultsArray, mContext));
    }

    @Override
    protected ArrayList<ContentValues> doInBackground(String... strings) {

        // ArrayList to hold all news data content values objects
        ArrayList<ContentValues> newsCVArray = new ArrayList<>();

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String newsJSONString = null;

        // Query parameter keys needed to construct the uri
        final String BASE_URL = "https://newsapi.org/v1/articles";
        final String SOURCE = "source";
        final String SORT_BY = "sortBy";
        final String API_KEY = "apiKey";

        // Fetch news data from all the news sources located in mNewsSourcesArray iteratively
        for(int i = 0; i < mNewsSourcesArray.length; i++) {
            try {

                // Construct the URI by appending the required query parameters to BASE_URI
                Uri uri = Uri.parse(BASE_URL).buildUpon().
                        appendQueryParameter(SOURCE, mNewsSourcesArray[i]).
                        appendQueryParameter(SORT_BY, SORT_FILTER).
                        appendQueryParameter(API_KEY, mContext.getString(R.string.newsapi_api_key)).
                        build();

                // Assign uri to url needed to open the connection
                URL url = new URL(uri.toString());

                // Create the request to TMDb, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                newsJSONString = buffer.toString();

            } catch (IOException e) {
                // If the code didn't successfully get the news data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }

            // Parse JSON data and add to the array of content values
            try {
                newsCVArray.add(getNewsDataFromJSON(newsJSONString));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return newsCVArray;
    }

    /**
     * Take the String representing the complete data in JSON Format and
     * pull out the data needed
     */
    private ContentValues getNewsDataFromJSON(String newsJSONString)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String NEWSAPI_SOURCE = "source";
        final String NEWSAPI_ARRAY_ARTICLES = "articles";
        final String NEWSAPI_ARTICLE_DESCRIPTION = "description";
        final String NEWSAPI_ARTICLE_URL = "url";
        final String NEWSAPI_ARTICLE_URL_TO_IMAGE = "urlToImage";
        final String NEWSAPI_ARTICLE_PUBLISHED_DATE = "publishedAt";

        // Create a new JSON object from the JSON String
        JSONObject newsJSON = new JSONObject(newsJSONString);

        // Get the "source" from the JSON object
        String source = newsJSON.getString(NEWSAPI_SOURCE);

        // Get the "articles" array from the JSON object
        JSONArray articlesArray = newsJSON.getJSONArray(NEWSAPI_ARRAY_ARTICLES);

        // Insert the news data into an array list of content values
        ArrayList<ContentValues> resultsList = new ArrayList<>();

        /*
            Construct a ContentValues object for the desired data for the
            first news article and add the object to the results array

            We want the description, url, release urlToImage and publishedAt date
        */

        String description;
        String url;
        String urlToImage;
        String publishedAt;

        // Get the JSON object holding the news data at position 0 in the JSON array
        JSONObject articleDataJSONObject = articlesArray.getJSONObject(0);

        // Get the article description
        description = articleDataJSONObject.getString(NEWSAPI_ARTICLE_DESCRIPTION);

        // Get the url of the article
        url = articleDataJSONObject.getString(NEWSAPI_ARTICLE_URL);

        // Get the url of the image in the article
        urlToImage = articleDataJSONObject.getString(NEWSAPI_ARTICLE_URL_TO_IMAGE);

        // Get the published date of the article
        publishedAt = articleDataJSONObject.getString(NEWSAPI_ARTICLE_PUBLISHED_DATE);

        // Create and add all news data content value objects to resultsList
        ContentValues cvData = new ContentValues();
        cvData.put(mContext.getString(R.string.key_article_source), source);
        cvData.put(mContext.getString(R.string.key_article_description), description);
        cvData.put(mContext.getString(R.string.key_article_url), url);
        cvData.put(mContext.getString(R.string.key_article_url_to_image), urlToImage);
        cvData.put(mContext.getString(R.string.key_article_published_date), publishedAt);

        // Return the content values object holding the news data
        // to be added to the array list of content values
        return cvData;

    }
}
