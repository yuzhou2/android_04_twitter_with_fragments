package com.yuzhou.twitter.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yuzhou.twitter.R;
import com.yuzhou.twitter.models.Tweet;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuzhou on 2015/08/17.
 */
abstract public class AbstractTimelineFragment extends Fragment
{
    private View view;
    private TweetAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_timeline, container, false);
        adapter = new TweetAdapter(view.getContext(), new ArrayList<Tweet>());

        setupTimeline();
        setupPullToRefresh();
        queryNewTweets();

        return view;
    }

    private void setupTimeline()
    {
        ListView lvTimeline = (ListView) view.findViewById(R.id.timeline__lv_list);
        lvTimeline.setAdapter(adapter);
        lvTimeline.setOnScrollListener(new EndlessScrollListener()
        {
            @Override
            public void onLoadMore(int page, int totalItemsCount)
            {
                queryTweets(page);
            }
        });
    }

    private void setupPullToRefresh()
    {
        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.timeline__rl_swipe);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                queryNewTweets();
                swipeLayout.setRefreshing(false);
            }

        });

        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void queryNewTweets()
    {
        adapter.clear();
        queryTweets(1);
    }

    private void queryTweets(int page)
    {
        if (!isNetworkAvailable()) {
            Toast.makeText(view.getContext(), R.string.error_unavailable_network, Toast.LENGTH_LONG).show();
            return;
        }
        queryTweetsAt(page);
    }

    abstract protected void queryTweetsAt(int page);

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    protected class HttpResponseHandler extends JsonHttpResponseHandler
    {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray json)
        {
            // Response is automatically parsed into a JSONArray
            Log.d("DEBUG", "timeline: " + json.toString());
            List<Tweet> tweets = Tweet.fromJson(json);
            adapter.addAll(tweets);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray json)
        {
            super.onFailure(statusCode, headers, throwable, json);
        }
    }

}