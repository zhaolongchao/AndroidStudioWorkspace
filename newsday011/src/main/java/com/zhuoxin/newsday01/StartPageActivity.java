package com.zhuoxin.newsday01;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.zhuoxin.adapter.ViewPagerAdapter;
import com.zhuoxin.common.ChannelManager;
import com.zhuoxin.fragment.FragmentBasePager;
import com.zhuoxin.fragment.FragmentPager1;
import com.zhuoxin.fragment.FragmentPager2;
import com.zhuoxin.fragment.FragmentPager3;
import com.zhuoxin.fragment.FragmentPager4;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by l on 2016/11/20.
 */

public class StartPageActivity extends FragmentActivity implements View.OnClickListener {
    ViewPager vpStartPage;
    Button btnSkip;
    ArrayList<FragmentBasePager> list=new ArrayList<FragmentBasePager>();
    //小球们
    ImageView[] imageviews=new ImageView[4];
    File f;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity5);
        f=this.getDatabasePath("news.db");
        if(f.exists()){
            Intent intent=new Intent(this,LogoActivity.class);
            startActivity(intent);
            this.finish();
        }else{
            ChannelManager.addChannelToDB();
        }
        vpStartPage= (ViewPager) findViewById(R.id.vp_start_page);
        btnSkip= (Button) findViewById(R.id.btn_skip);
        btnSkip.setOnClickListener(this);
        initFragment();
        initData();
        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager(),list);
        vpStartPage.setAdapter(adapter);//适配
        vpStartPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**
             * 页面跳转完成时调用
             * @param position  当前切换到的页面的位置
             */
            @Override
            public void onPageSelected(int position) {
                //遍历小球，判断当前位置与小球位置相同的那个设置为选中效果
                for (int i = 0; i < imageviews.length; i++) {
                    if(i==position){
                        imageviews[i].setImageResource(R.mipmap.a4);
                        imageviews[i].setAlpha(1f);

                    }else{
                        imageviews[i].setImageResource(R.mipmap.a4);
                        imageviews[i].setAlpha(0.5f);
                    }
                }
                list.get(position).startAnim();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initFragment() {
        FragmentPager1 f1=new FragmentPager1();
        FragmentPager2 f2=new FragmentPager2();
        FragmentPager3 f3=new FragmentPager3();
        FragmentPager4 f4=new FragmentPager4();
        list.add(f1);
        list.add(f2);
        list.add(f3);
        list.add(f4);
    }

    private void initData() {
        //获取图片
        for (int i = 0; i < 4; i++) {
            imageviews[i]=(ImageView) findViewById(R.id.iv1+i);
            if(i==0){
                imageviews[i].setImageResource(R.mipmap.a4);
                imageviews[i].setAlpha(1f);
            }else{
                imageviews[i].setImageResource(R.mipmap.a4);
                imageviews[i].setAlpha(0.5f);
            }
        }
    }
    public void onClick(View view){//跳过
        Intent intent=new Intent(this,LogoActivity.class);
        startActivity(intent);
        StartPageActivity.this.finish();
    }
}
