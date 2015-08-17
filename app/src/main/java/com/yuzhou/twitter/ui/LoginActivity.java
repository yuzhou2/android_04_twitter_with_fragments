package com.yuzhou.twitter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.yuzhou.twitter.R;
import com.yuzhou.twitter.api.RestClient;

public class LoginActivity extends OAuthLoginActionBarActivity<RestClient>
{
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        status = (TextView) findViewById(R.id.login__session_status);

        setupHomeLogo();
        setupLoginButton();
    }

    // OAuth authenticated successfully, launch primary authenticated activity
    // i.e Display application "homepage"
    @Override
    public void onLoginSuccess()
    {
        String output = "Status: Your login was successful " +
                "\nAuth Token Received: " +
                getClient().checkAccessToken().getToken();
        status.setText(output);
        startActivity(new Intent(this, MainActivity.class));
    }

    // OAuth authentication flow failed, handle the error
    // i.e Display an error dialog or toast
    @Override
    public void onLoginFailure(Exception e)
    {
        status.setText("Status: Login Failed");
        e.printStackTrace();
    }

    // Click handler method for the button used to start OAuth flow
    // Uses the client to initiate OAuth authorization
    // This should be tied to a button used to login
    public void loginToRest(View view)
    {
        getClient().connect();
    }

    private void setupHomeLogo()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.title_timeline));
        actionBar.setLogo(R.drawable.tw__ic_logo_default);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    private void setupLoginButton()
    {
        Button loginButton = (Button) findViewById(R.id.login__bn_login);
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loginToRest(v);
            }
        });
    }

}
