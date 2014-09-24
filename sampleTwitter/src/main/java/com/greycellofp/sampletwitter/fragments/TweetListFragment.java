package com.greycellofp.sampletwitter.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.greycellofp.sampletwitter.R;
import com.greycellofp.sampletwitter.adapters.TweetListAdapter;
import com.greycellofp.sampletwitter.asynctasks.FetchSearchResult;
import com.greycellofp.sampletwitter.widget.InfiniteScrollListView;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;

/**
 * Created by pawan on 9/24/14.
 */
public class TweetListFragment extends ListFragment {
    private static final String TAG = TweetListFragment.class.getSimpleName();

    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private static final String TWEETS = "tweets";
    private static final String CURRENT_SELECTION = "selection";
    private Callbacks callbacks;

    private int activatedPosition = ListView.INVALID_POSITION;

    private EditText searchEditText;
    private InfiniteScrollListView infiniteScrollListView;
    private TweetListAdapter tweetListAdapter;

    private long maxId;
    private boolean isLoadingNewTweets = false;
    private List<Status> tweets = new ArrayList<Status>();

    public TweetListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_content, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null){
            if(savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
                setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
            }
            if(savedInstanceState.containsKey(TWEETS)){
                tweets = (List<Status>) savedInstanceState.getSerializable(TWEETS);
                tweetListAdapter = new TweetListAdapter(tweets);
                setListAdapter(tweetListAdapter);
            }
        }
        searchEditText = (EditText) view.findViewById(R.id.search_tweets);
        searchEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                    FetchSearchResult fetchSearchResult = new FetchSearchResult() {

                        @Override
                        protected void onPreExecute() {
                            callbacks.onShowProgress();
                        }

                        @Override
                        protected void onPostExecute(QueryResult queryResult) {
                            if(queryResult == null){
                                Toast.makeText(getActivity(), "unable to fetch Search Result. Try again later", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Log.d(TAG, "maxID:" + queryResult.getMaxId());
                            maxId = queryResult.getTweets().get(queryResult.getTweets().size() - 1).getId() + 1;
                            if (tweetListAdapter == null) {
                                tweets.addAll(queryResult.getTweets());
                                tweetListAdapter = new TweetListAdapter(queryResult.getTweets());
                            } else {
                                tweets.clear();
                                tweetListAdapter.clearTweets();
                                tweets.addAll(queryResult.getTweets());
                                tweetListAdapter.addAll(queryResult.getTweets());
                            }
                            setListAdapter(tweetListAdapter);
//                            getListView().setSelection(0);
//                            getListView().performItemClick(getListView().getSelectedView(), 0, 0);
                            callbacks.hideProgress();
                        }
                    };

                    fetchSearchResult.execute(new Query(v.getText().toString()).count(50));
                    return true;
                }
                return false;
            }
        });

        infiniteScrollListView = (InfiniteScrollListView) getListView();
        infiniteScrollListView.setLoadNewDataListener(new InfiniteScrollListView.OnLoadNewDataListener() {
            @Override
            public void onLoadNewData() {
                if(isLoadingNewTweets)
                    return;
                isLoadingNewTweets = true;
                FetchSearchResult fetchSearchResult = new FetchSearchResult(){
                    @Override
                    protected void onPostExecute(QueryResult queryResult) {
                        maxId = queryResult.getTweets().get(queryResult.getTweets().size() - 1).getId() + 1;
                        Log.d(TAG, "maxID:" + maxId);
                        if(tweetListAdapter == null){
                            tweetListAdapter = new TweetListAdapter(queryResult.getTweets());
                            setListAdapter(tweetListAdapter);
                        }else{
                            tweets.addAll(queryResult.getTweets());
                            tweetListAdapter.addAll(queryResult.getTweets());
                            tweetListAdapter.notifyDataSetChanged();
                        }
                        isLoadingNewTweets = false;
                    }
                };
                Query query = new Query(searchEditText.getText().toString()).count(50);
                query.setMaxId(maxId);
                fetchSearchResult.execute(query);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        callbacks.onItemSelected((Status) listView.getAdapter().getItem(position));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (activatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, activatedPosition);
        }
        outState.putSerializable(TWEETS, (ArrayList<Status>)tweets);
        outState.putInt(CURRENT_SELECTION, getListView().getSelectedItemPosition());
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(activatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        activatedPosition = position;
    }


    public interface Callbacks {
        public void onItemSelected(Status status);
        public void onShowProgress();
        public void hideProgress();
    }
}