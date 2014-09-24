package com.greycellofp.sampletwitter.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.greycellofp.sampletwitter.app.SampleTwitterApplication;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.TwitterException;

/**
 * Created by pawan on 9/24/14.
 */
public class FetchSearchResult extends AsyncTask<Query, Void, QueryResult> {
    private static final String TAG = FetchSearchResult.class.getSimpleName();

    @Override
    protected QueryResult doInBackground(Query... params) {
        if(params == null || params.length == 0)
        return null;

        try {
            QueryResult queryResult = SampleTwitterApplication.getInstance().getTwitterInstance().search(params[0]);
            Log.d(TAG, "query:" + queryResult.getQuery());
            return queryResult;
        } catch (TwitterException e) {
            Log.e(TAG, "Failed to make search request", e);
        }
        return null;
    }
}
