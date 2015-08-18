package com.yuzhou.twitter.models;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by yuzhou on 2015/08/11.
 */
@EqualsAndHashCode
@ToString
public class User
{
    @Getter @Setter private String name;
    @Getter @Setter private String screenName;
    @Getter @Setter private String profileImageUrl;

    @Getter @Setter private String profileBackgroundColor;
    @Getter @Setter private boolean profileBackgroundTile;
    @Getter @Setter private String profileBackgroundImageUrl;

    @Getter @Setter private int tweetsCount;
    @Getter @Setter private int followingCount;
    @Getter @Setter private int followersCount;

    public User()
    {
    }

    public User(JSONObject json)
    {
        try {
            name = json.getString("name");
            screenName = json.getString("screen_name");
            profileImageUrl = json.getString("profile_image_url");

            profileBackgroundColor = json.getString("profile_background_color");
            profileBackgroundTile = json.getBoolean("profile_background_tile");
            profileBackgroundImageUrl = json.getString("profile_background_image_url");

            tweetsCount = json.getInt("statuses_count");
            followingCount = json.getInt("friends_count");
            followersCount = json.getInt("followers_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getQScreenName()
    {
        if (screenName != null && !screenName.isEmpty()) {
            return "@" + screenName;
        }
        return screenName;
    }

    public String getQProfileBackgroundColor()
    {
        if (profileBackgroundColor != null && !profileBackgroundColor.isEmpty()) {
            return "#" + profileBackgroundColor;
        }
        return profileBackgroundColor;
    }

}
