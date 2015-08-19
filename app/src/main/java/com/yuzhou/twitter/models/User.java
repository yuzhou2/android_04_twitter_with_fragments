package com.yuzhou.twitter.models;

import android.support.annotation.NonNull;

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
    @Getter @Setter private long id;
    @Getter @Setter private String name;
    @Getter @Setter private String screenName;
    @Getter @Setter private String profileImageUrl;
    @Getter @Setter private String profileBannerUrl;

    @Getter @Setter private int tweetsCount;
    @Getter @Setter private int followingCount;
    @Getter @Setter private int followersCount;

    public User()
    {
    }

    public User(@NonNull JSONObject json)
    {
        try {
            id = json.getLong("id");
            name = json.getString("name");
            screenName = json.getString("screen_name");
            profileImageUrl = json.optString("profile_image_url", null);
            profileBannerUrl = json.optString("profile_banner_url", null);

            tweetsCount = json.optInt("statuses_count", 0);
            followingCount = json.optInt("friends_count", 0);
            followersCount = json.optInt("followers_count", 0);
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

}
