package com.greycellofp.sampletwitter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.greycellofp.sampletwitter.R;
import com.greycellofp.sampletwitter.app.SampleTwitterApplication;

import java.util.Collection;
import java.util.List;
import java.util.zip.Inflater;

import twitter4j.Status;

/**
 * Created by pawan on 9/24/14.
 */
public class TweetListAdapter extends BaseAdapter {
    private static final String TAG = TweetListAdapter.class.getSimpleName();

    private List<Status> tweets;

    public TweetListAdapter(List<Status> tweets) {
        this.tweets = tweets;
    }

    @Override
    public int getCount() {
        return tweets.size();
    }

    @Override
    public Object getItem(int position) {
        return tweets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tweet_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.userThumb = (NetworkImageView) convertView.findViewById(R.id.user_thumb);
            holder.userScreenName = (TextView) convertView.findViewById(R.id.user_screen_name);
            holder.userName = (TextView) convertView.findViewById(R.id.user_name);
            holder.tweetText = (TextView) convertView.findViewById(R.id.tweet_text);
            convertView.setTag(holder);
        }

        Status tweet = (Status) getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.userThumb.setImageUrl(tweet.getUser().getBiggerProfileImageURL(), SampleTwitterApplication.getInstance().getImageLoader());
        holder.userScreenName.setText(tweet.getUser().getScreenName());
        holder.userName.setText("@" + tweet.getUser().getName());
        holder.tweetText.setText(tweet.getText());
        return convertView;
    }

    private class ViewHolder{
        NetworkImageView userThumb;
        TextView userScreenName;
        TextView userName;
        TextView tweetText;
    }

    public void clearTweets(){
        tweets.clear();
    }

    public void addAll(List<Status> status){
        tweets.addAll(status);
    }
}
