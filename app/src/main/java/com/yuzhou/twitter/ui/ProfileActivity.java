package com.yuzhou.twitter.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.yuzhou.twitter.R;
import com.yuzhou.twitter.RestApplication;
import com.yuzhou.twitter.api.RestClient;
import com.yuzhou.twitter.models.User;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setupActionBar();
        queryUserProfile();
        queryUserTimeline();
    }

    @Override
    public Intent getSupportParentActivityIntent()
    {
        finish();
        return null;
    }

    private void setupActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.title_activity_profile);
    }

    private void queryUserProfile()
    {
        RestClient client = RestApplication.getRestClient();
        client.getUserCredential(new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json)
            {
                // Response is automatically parsed into a JSONArray
                Log.d("DEBUG", "credential: " + json.toString());
                User user = new User(json);
                updateUserProfile(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void updateUserProfile(User user)
    {
        ImageView background = (ImageView) findViewById(R.id.profile__iv_background);
        background.setBackgroundColor(Color.parseColor(user.getQProfileBackgroundColor()));
        if (user.isProfileBackgroundTile() && !user.getProfileBackgroundImageUrl().isEmpty()) {
            Picasso.with(this).load(user.getProfileBackgroundImageUrl()).into((ImageView) findViewById(R.id.profile__iv_background));
        }

        Picasso.with(this).load(user.getProfileImageUrl()).into((ImageView) findViewById(R.id.profile__iv_profile_image));
        ((TextView) findViewById(R.id.profile__tv_user_name)).setText(user.getName());
        ((TextView) findViewById(R.id.profile__tv_screen_name)).setText(user.getQScreenName());
        ((TextView) findViewById(R.id.profile__tweet_count)).setText(String.valueOf(user.getTweetsCount()));
        ((TextView) findViewById(R.id.profile__following_count)).setText(String.valueOf(user.getFollowingCount()));
        ((TextView) findViewById(R.id.profile__followers_count)).setText(String.valueOf(user.getFollowersCount()));
    }

    private void queryUserTimeline()
    {
        UserTimelineFragment fragment = new UserTimelineFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.profile__timeline, fragment).commit();
    }

    private static class UserTimelineFragment extends AbstractTimelineFragment
    {
        @Override
        protected void queryTweets(int page)
        {
            RestClient client = RestApplication.getRestClient();
            client.getUserTimeline(page, new AbstractTimelineFragment.HttpResponseHandler());
        }
    }

}
