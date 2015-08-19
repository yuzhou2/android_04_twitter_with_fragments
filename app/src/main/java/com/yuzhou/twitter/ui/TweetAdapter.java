package com.yuzhou.twitter.ui;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yuzhou.twitter.R;
import com.yuzhou.twitter.models.Tweet;

import java.util.List;

/**
 * Created by yuzhou on 2015/08/10.
 */
public class TweetAdapter extends ArrayAdapter<Tweet>
{

    public TweetAdapter(Context context, List<Tweet> tweets)
    {
        super(context, R.layout.item_tweet, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            holder = new ViewHolder();
            holder.profileImage = (ImageView) convertView.findViewById(R.id.tweet__iv_profile_image);
            holder.userName = (TextView) convertView.findViewById(R.id.tweet__tv_user_name);
            holder.screenName = (TextView) convertView.findViewById(R.id.tweet__tv_screen_name);
            holder.createTime = (TextView) convertView.findViewById(R.id.tweet__tv_create_time);
            holder.textBody = (TextView) convertView.findViewById(R.id.tweet__tv_text_body);
            holder.retweetCount = (TextView) convertView.findViewById(R.id.tweet__tv_retweet);
            holder.favoriteCount = (TextView) convertView.findViewById(R.id.tweet__tv_favorite);
            holder.images = (GridView) convertView.findViewById(R.id.tweet__gv_images);
            convertView.setTag(holder);
        }

        final Tweet tweet = getItem(position);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(holder.profileImage);
        holder.userName.setText(tweet.getUser().getName());
        holder.screenName.setText(tweet.getUser().getQScreenName());
        holder.createTime.setText(getRelativeTime(tweet.getCreateTime()));
        holder.textBody.setText(tweet.getText());
        holder.retweetCount.setText(String.valueOf(tweet.getRetweetCount()));
        holder.favoriteCount.setText(String.valueOf(tweet.getFavoriteCount()));

        int imageCount = tweet.getExtendedEntities().getMediaList().size();
        int numColumns = (imageCount > 3) ? 3 : imageCount;

        ImageAdapter mediaAdapter = new ImageAdapter(getContext(), tweet.getExtendedEntities().getMediaList());
        holder.images.setNumColumns(numColumns);
        holder.images.setAdapter(mediaAdapter);

        holder.profileImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("user_id", tweet.getUser().getId());
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    private static final CharSequence getRelativeTime(long createTime)
    {
        return DateUtils.getRelativeTimeSpanString(createTime, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
    }

    private static final class ViewHolder
    {
        private ImageView profileImage;
        private TextView userName;
        private TextView screenName;
        private TextView createTime;
        private TextView textBody;
        private TextView retweetCount;
        private TextView favoriteCount;
        private GridView images;
    }

}
