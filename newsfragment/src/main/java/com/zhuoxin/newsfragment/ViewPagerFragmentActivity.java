package com.zhuoxin.newsfragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.zhuoxin.newsfragment.frag.FragmentPager1;
import com.zhuoxin.newsfragment.frag.FragmentPager2;
import com.zhuoxin.newsfragment.frag.FragmentPager3;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerFragmentActivity extends FragmentActivity {
    ViewPager viewPager;
    List<Fragment> list=new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_fragment);
        viewPager= (ViewPager) findViewById(R.id.vp);
        initFragment();
        MyAdapter adapter=new MyAdapter(getSupportFragmentManager(),list);
        //适配
        viewPager.setAdapter(adapter);
    }

    private void initFragment() {
        FragmentPager1 f1=new FragmentPager1();
        FragmentPager2 f2=new FragmentPager2();
        FragmentPager3 f3=new FragmentPager3();
        list.add(f1);
        list.add(f2);
        list.add(f3);
    }
}
