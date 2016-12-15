package com.zhuoxin.newsfragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by l on 2016/11/14.
 */

public class MyAdapter extends FragmentPagerAdapter {
    List<Fragment> list;//需要适配的fragment
    public MyAdapter(FragmentManager fm,List<Fragment> list) {
        super(fm);
        this.list=list;
    }

    /**
     * 当前页面适配的fragment
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
