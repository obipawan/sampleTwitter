package com.greycellofp.sampletwitter.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by pawan on 9/23/14.
 */
public class InfiniteScrollListView extends ListView implements AbsListView.OnScrollListener {
    private static final String TAG = InfiniteScrollListView.class.getSimpleName();

    OnLoadNewDataListener loadNewDataListener;

    public InfiniteScrollListView(Context context) {
        this(context, null);
    }

    public InfiniteScrollListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InfiniteScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(totalItemCount == 0){
            return;
        }
        int lastItem = firstVisibleItem + visibleItemCount;
        if(lastItem == totalItemCount && loadNewDataListener != null){
            Log.d(TAG, "should load more tweets");
            loadNewDataListener.onLoadNewData();
        }
    }

    public OnLoadNewDataListener getLoadNewDataListener() {
        return loadNewDataListener;
    }

    public void setLoadNewDataListener(OnLoadNewDataListener loadNewDataListener) {
        this.loadNewDataListener = loadNewDataListener;
    }

    public interface OnLoadNewDataListener{
        public void onLoadNewData();
    }
}
