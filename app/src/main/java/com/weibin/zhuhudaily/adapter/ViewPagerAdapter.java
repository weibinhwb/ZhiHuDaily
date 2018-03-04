package com.weibin.zhuhudaily.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;


import com.weibin.zhuhudaily.fragment.ViewPagerFragment;
import com.weibin.zhuhudaily.gsondb.HomeNews;

import java.util.List;

/**
 * Created by weibinhuang on 18-2-1.
 * 轮播图适配器
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<HomeNews.Top_Stories> topStoriesList;

    public List<HomeNews.Top_Stories> getTopStoriesList() {
        return topStoriesList;
    }

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ViewPagerAdapter(FragmentManager fm, List<HomeNews.Top_Stories> topStoriesList) {
        super(fm);
        this.topStoriesList = topStoriesList;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        HomeNews.Top_Stories topStories = topStoriesList.get(position);
        bundle.putString("image",topStories.getImage());
        bundle.putString("title",topStories.getTitle());
        bundle.putInt("Id",topStories.getId());
        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return topStoriesList.size();
    }


}
