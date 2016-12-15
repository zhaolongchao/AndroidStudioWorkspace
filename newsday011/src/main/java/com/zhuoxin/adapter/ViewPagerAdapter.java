package com.zhuoxin.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by l on 2016/11/14.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    List<?> list;//需要适配的fragment
    public ViewPagerAdapter(FragmentManager fm,List<?> list) {
        super(fm);
        this.list=list;
    }

    @Override
    public Fragment getItem(int position) {

        return (Fragment) list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
