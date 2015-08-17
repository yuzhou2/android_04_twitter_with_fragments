package com.yuzhou.twitter.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yuzhou.twitter.R;
import com.yuzhou.twitter.RestApplication;
import com.yuzhou.twitter.api.RestClient;
import com.yuzhou.twitter.models.Tweet;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuzhou on 2015/08/17.
 */
public class TimelineFragment extends Fragment
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

    private void setupTimeline()
    {
        ListView lvTimeline = (ListView) view.findViewById(R.id.timeline__lv_list);
        lvTimeline.setEmptyView(view.findViewById(R.id.timeline__tv_empty));
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

    private void queryNewTweets()
    {
        adapter.clear();
        queryTweets(1);
    }

    private void queryTweets(int page)
    {
        RestClient client = RestApplication.getRestClient();
        client.getHomeTimeline(page, new JsonHttpResponseHandler()
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
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

}