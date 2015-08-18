package com.yuzhou.twitter.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        userId = intent.getLongExtra("user_id", 0);

        setupActionBar();

        if (!isNetworkAvailable()) {
            Toast.makeText(this, R.string.error_unavailable_network, Toast.LENGTH_LONG).show();
            return;
        }

        queryUserProfile();
        queryUserTimeline();
    }

    @Override
    public Intent getSupportParentActivityIntent()
    {
        return super.getSupportParentActivityIntent();
        //finish();
        //return null;
    }

    private void setupActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.title_activity_profile);
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void queryUserProfile()
    {
        RestClient client = RestApplication.getRestClient();
        if (userId > 0) {
            client.getUser(userId, new UserProfileHttpResponseHandler());
        } else {
            client.getUserCredential(new UserProfileHttpResponseHandler());
        }
    }

    private void updateUserProfile(User user)
    {
        Picasso.with(this).load(user.getProfileBannerUrl()).into((ImageView) findViewById(R.id.profile__iv_profile_banner));
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

    private class UserTimelineFragment extends AbstractTimelineFragment
    {
        @Override
        protected void queryTweetsAt(int page)
        {
            RestClient client = RestApplication.getRestClient();
            if (userId > 0) {
                client.getUserTimeline(userId, page, new HttpResponseHandler());
            } else {
                client.getUserTimeline(page, new HttpResponseHandler());
            }
        }
    }

    private class UserProfileHttpResponseHandler extends JsonHttpResponseHandler
    {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject json)
        {
            // Response is automatically parsed into a JSONObject
            Log.d("DEBUG", "credential: " + json.toString());
            User user = new User(json);
            updateUserProfile(user);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject json)
        {
            super.onFailure(statusCode, headers, throwable, json);
        }
    }

}
