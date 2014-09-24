package com.greycellofp.sampletwitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

import com.greycellofp.sampletwitter.R;
import com.greycellofp.sampletwitter.fragments.TweetDetailFragment;
import com.greycellofp.sampletwitter.fragments.TweetListFragment;

import twitter4j.Status;

/**
 * Created by pawan on 9/24/14.
 */
public class TweetListActivity extends ActionBarActivity
        implements TweetListFragment.Callbacks {

    private static final String TWO_PANE = "two_pane";

    private boolean isTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(TWO_PANE)){
                isTwoPane = savedInstanceState.getBoolean(TWO_PANE);
            }
        }
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_tweet_list);
        if (findViewById(R.id.tweet_detail_container) != null) {
            isTwoPane = true;
            if(savedInstanceState == null)
                ((TweetListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.tweet_list))
                    .setActivateOnItemClick(true);
        }
    }

    @Override
    public void onItemSelected(Status status) {
        if (isTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(TweetDetailFragment.ARG_ITEM_ID, status);
            Status test = (Status) arguments.getSerializable(TweetDetailFragment.ARG_ITEM_ID);
            TweetDetailFragment fragment = new TweetDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.tweet_detail_container, fragment)
                    .commit();

        } else {
            Bundle arguments = new Bundle();
            arguments.putSerializable(TweetDetailFragment.ARG_ITEM_ID, status);
            Intent detailIntent = new Intent(this, TweetDetailActivity.class);
            detailIntent.putExtras(arguments);
            startActivity(detailIntent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(TWO_PANE, isTwoPane);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onShowProgress() {
        showActionBarProgress();
    }

    @Override
    public void hideProgress() {
        hideActionBarProgress();
    }

    private void showActionBarProgress(){
        setProgressBarIndeterminateVisibility(true);
    }

    private void hideActionBarProgress(){
        setProgressBarIndeterminateVisibility(false);
    }


}
