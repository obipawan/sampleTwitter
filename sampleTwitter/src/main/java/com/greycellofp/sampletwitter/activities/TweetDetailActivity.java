package com.greycellofp.sampletwitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.greycellofp.sampletwitter.R;
import com.greycellofp.sampletwitter.fragments.TweetDetailFragment;

/**
 * Created by pawan on 9/24/14.
 */
public class TweetDetailActivity extends ActionBarActivity {
    private static final String TAG = TweetDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            TweetDetailFragment fragment = new TweetDetailFragment();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.tweet_detail_container, fragment)
                    .commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, TweetListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
