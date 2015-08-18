package com.yuzhou.twitter.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yuzhou.twitter.R;
import com.yuzhou.twitter.RestApplication;
import com.yuzhou.twitter.api.RestClient;

/**
 * Created by yuzhou on 2015/08/16.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter
{
    private final int tabTitles[] = new int[]{R.string.title_timeline, R.string.title_mentions};
    private final int pageCount = tabTitles.length;
    private Context context;

    public MainFragmentPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount()
    {
        return pageCount;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
        case 0:
            return new HomeTimelineFragment();
        case 1:
            return new MentionsTimelineFragment();
        default:
            return new AbstractTimelineFragment()
            {
                @Override
                protected void queryTweets(int page)
                {
                }
            };
        }
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return context.getString(tabTitles[position]).toUpperCase();
    }

    private static class HomeTimelineFragment extends AbstractTimelineFragment
    {
        @Override
        protected void queryTweets(int page)
        {
            RestClient client = RestApplication.getRestClient();
            client.getHomeTimeline(page, new HttpResponseHandler());
        }
    }

    private static class MentionsTimelineFragment extends AbstractTimelineFragment
    {
        @Override
        protected void queryTweets(int page)
        {
            RestClient client = RestApplication.getRestClient();
            client.getMentionsTimeline(page, new HttpResponseHandler());
        }
    }

}
