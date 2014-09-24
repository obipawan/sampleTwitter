package com.greycellofp.sampletwitter.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.greycellofp.sampletwitter.R;
import com.greycellofp.sampletwitter.helpers.ManifestHelper;
import com.greycellofp.sampletwitter.utils.LruBitmapCache;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by pawan on 9/24/14.
 */
public class SampleTwitterApplication extends Application {
    private static final String TAG = SampleTwitterApplication.class.getSimpleName();

    private static SampleTwitterApplication instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private Twitter twitter;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static SampleTwitterApplication getInstance(){
        if(instance == null){
            throw new RuntimeException("Application not initialized. Check manifest");
        }
        return instance;
    }

    private Configuration buildOAuthConfiguration(){
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(ManifestHelper.getMetaValue(getResources().getString(R.string.api_key), ""));
        builder.setOAuthConsumerSecret(ManifestHelper.getMetaValue(getResources().getString(R.string.api_sec), ""));
        builder.setOAuthAccessToken(ManifestHelper.getMetaValue(getResources().getString(R.string.access_token), ""));
        builder.setOAuthAccessTokenSecret(ManifestHelper.getMetaValue(getResources().getString(R.string.access_token_secret), ""));
        return builder.build();
    }

    public Twitter getTwitterInstance(){
        if(twitter == null){
            twitter = new TwitterFactory(getInstance().buildOAuthConfiguration()).getInstance();
        }
        return twitter;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (imageLoader == null) {
            imageLoader = new ImageLoader(this.requestQueue,
                    new LruBitmapCache());
        }
        return this.imageLoader;
    }
}