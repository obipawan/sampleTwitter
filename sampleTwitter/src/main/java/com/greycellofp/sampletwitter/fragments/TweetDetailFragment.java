package com.greycellofp.sampletwitter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.android.volley.toolbox.NetworkImageView;
import com.greycellofp.sampletwitter.R;
import com.greycellofp.sampletwitter.app.SampleTwitterApplication;

import twitter4j.Status;

/**
 * Created by pawan on 9/24/14.
 */
public class TweetDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    private Status status;

    public TweetDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            status = (Status) getArguments().getSerializable(ARG_ITEM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tweet_detail, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (status != null) {
            ((TextView) view.findViewById(R.id.tweet_detail)).setText(status.getText());
            ((NetworkImageView) view.findViewById(R.id.user_detail_thumb)).setImageUrl(status.getUser().getBiggerProfileImageURL(),
                    SampleTwitterApplication.getInstance().getImageLoader());
            ((TextView)view.findViewById(R.id.detail_retweets)).setText("Retweets: " + status.getRetweetCount());
            ((TextView)view.findViewById(R.id.detail_favorites)).setText("Favorites:" + status.getFavoriteCount());
            ((TextView)view.findViewById(R.id.created_at)).setText("Created at:" + status.getCreatedAt().toString());
            ((TextView)view.findViewById(R.id.detail_user_name)).setText("@" + status.getUser().getName());
            ((TextView)view.findViewById(R.id.detail_user_screen_name)).setText(status.getUser().getScreenName());
        }
    }
}