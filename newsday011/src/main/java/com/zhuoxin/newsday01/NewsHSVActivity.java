package com.zhuoxin.newsday01;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zhuoxin.NewsApplication;
import com.zhuoxin.adapter.ViewPagerAdapter;
import com.zhuoxin.dao.ChannelDAO;
import com.zhuoxin.entity.ChannelItem;
import com.zhuoxin.entity.UserItem;
import com.zhuoxin.fragment.FragmentLeft;
import com.zhuoxin.fragment.FragmentPager;
import com.zhuoxin.fragment.FragmentRight;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 2016/11/15.
 */

public class NewsHSVActivity extends FragmentActivity {
    HorizontalScrollView hsvScroll;//水平滚动条
    LinearLayout llScroll;//水平滚动条中的线性布局
    ViewPager vpScroll;//水平滚动条中的viewpager
    SlidingMenu slidingMenu;//侧拉菜单
    List<ChannelItem> listChannelItem=new ArrayList<ChannelItem>();
    List<Fragment> listFragment=new ArrayList<Fragment>();
    private int count;//线性布局中子控件的数量
    //创建一个fragment对象
    FragmentPager fragmentPager;//主界面fragment
    private FragmentManager fm;
    private FragmentTransaction ft;
    FragmentLeft fragmentLeft;//左侧拉fragment
    FragmentRight fragmentRight;//右侧拉fragment
    ChannelDAO dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_scroll);
        initView();//实例化控件
        dao=ChannelDAO.getChannelDAO();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();//添加实体类对象
        initTextView(this);//代码添加textview
        initFragment();
    }
    //适配fragment
    private void initFragment() {
        for (int i = 0; i <listChannelItem.size() ; i++) {
            fragmentPager=new FragmentPager();//创建fragment对象
            listFragment.add(fragmentPager);//添加到集合
            //将activity中的数据传递到fragment中
            Bundle bundle=new Bundle();
            bundle.putString("url",listChannelItem.get(i).getChannelLink());
            //使用setArguments绑定数据
            fragmentPager.setArguments(bundle);
//            //1.获取fragment管理对象
//            fm=getSupportFragmentManager();
//            //2.获取事务对象
//            ft=fm.beginTransaction();
//        /*
//        3.添加fragment对象
//        参数1为一个布局的资源id--activity中将要放置fragment位置的布局
//        参数2 fragment对象
//        参数3  每一个fragment的标签
//         */
//            ft.add(R.id.vp_scroll,fragmentPager,"frag");
//            //4.提交
//            ft.commit();

        }
        //创建适配器
        ViewPagerAdapter vpAdapter=new ViewPagerAdapter(getSupportFragmentManager(),listFragment);
        //适配fragment到viewpager
        vpScroll.setAdapter(vpAdapter);
    }
    //实例化控件+侧拉
    private void initView() {
        hsvScroll= (HorizontalScrollView) findViewById(R.id.hsv_scroll);
        llScroll= (LinearLayout) findViewById(R.id.ll_scroll);
        vpScroll= (ViewPager) findViewById(R.id.vp_scroll);
        slidingMenu=new SlidingMenu(this);
        //设置为左右两边菜单栏
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        //设置全屏范围都可以打开菜单栏
        //slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置边缘滑动打开菜单栏
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //设置菜单栏的宽度
        slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu);
        //设置菜单栏与类的关联：当前类显示的为菜单栏的中间界面
        slidingMenu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);
        //设置左菜单栏样式
        slidingMenu.setMenu(R.layout.left);
        //设置右菜单栏样式
        slidingMenu.setSecondaryMenu(R.layout.right);
        //把左右侧拉fragment添加到侧拉空布局上
        fragmentLeft=new FragmentLeft();
        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();
        ft.add(R.id.ll_left_menu,fragmentLeft);
        ft.commit();
        fragmentRight=new FragmentRight();
        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();
        ft.add(R.id.ll_right_menu,fragmentRight);
        ft.commit();
    }
    //添加实体类对象
    private void initData() {
        //判断用户是否登录
        UserItem userItem= NewsApplication.getUserItem();
        listChannelItem.clear();//清空集合
        if(userItem!=null){//用户登录状态
            int userId=userItem.getUserId();
            if(dao.findDataByUserId(userId)){//用户是否保存过频道数据
                listChannelItem=dao.getUserChannels(userId,1);
            }else{
                listChannelItem=dao.getChannels(1);
            }
        }else{
            listChannelItem=dao.getChannels(1);
        }
    }
    //代码添加textview
    private void initTextView(Context context) {
        //首先移除所有在LinearLayout的TextView
        llScroll.removeAllViews();
        for (int i = 0; i < listChannelItem.size(); i++) {
            final TextView tv=new TextView(this);
            //屏幕的宽度
            DisplayMetrics dm=new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int screenWidth=dm.widthPixels;
            //设置宽高参数
            LinearLayout.LayoutParams params=
                    new LinearLayout.LayoutParams(screenWidth/7,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            //设置左右边距
            params.leftMargin=15;
            params.rightMargin=15;
            tv.setLayoutParams(params);
            //设置内边距
            tv.setPadding(5,5,5,5);
            //设置可点击
            tv.setClickable(true);
            //设置字体颜色
            tv.setTextColor(Color.BLACK);
            //设置居中，字号
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(20);
            //获取文本内容
            tv.setText(listChannelItem.get(i).getChannelName());
            //设置背景选择器
            tv.setBackgroundResource(R.drawable.tv_bg);
            if(i==0){//首次进入该界面设置第一个为选中状态
                tv.setSelected(true);
                //滚动最左边
                hsvScroll.smoothScrollTo(0, 0);
            }
            //添加点击监听
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(NewsHSVActivity.this,
                            tv.getText().toString(),Toast.LENGTH_SHORT).show();
                    //获取线性布局中子控件的数量
                    count=llScroll.getChildCount();
                    //在线性布局中查找与当前点击的控件相等时改变TextView的状态并切换ViewPager
                    for (int j = 0; j < count; j++) {
                        View v=llScroll.getChildAt(j);
                        if(v!=view){//背景选择
                            v.setSelected(false);
                        }else{
                            v.setSelected(true);
                            vpScroll.setCurrentItem(j);//设置当前项目
                        }
                    }
                }
            });
            llScroll.addView(tv,i,params);//把创建的控件添加到线性布局中
            //给viewpager添加监听
            vpScroll.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    //遍历线性布局
                    for (int j = 0; j <listChannelItem.size() ; j++) {
                        View v=llScroll.getChildAt(j);
                        if(j==position){
                            /*计算滚动条的滚动距离(将屏幕宽度的1/2与当前被选中控件的
                             左外边距+控件宽度的1/2作比较，如果大就滚动，如果小就不滚动，
                             滚动距离就是去差值)
                             */
                            int left=v.getLeft();//获取控件的左外边距
                            int width=v.getWidth();//控件的宽度
                            //屏幕的宽度
                            DisplayMetrics dm=new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(dm);
                            int screenWidth=dm.widthPixels;
                            int scrollx=left+width/2-screenWidth/2;
                            //设置滚动条滚动的距离
                            hsvScroll.smoothScrollTo(scrollx,0);
                            v.setSelected(true);
                        }else{
                            v.setSelected(false);
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }
    //点击监听
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_add://+号
                Intent intent=new Intent(this,ChannelActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_left:
                slidingMenu.showMenu();//显示左边菜单栏
                break;
            case R.id.iv_right:
                slidingMenu.showSecondaryMenu();//显示右边菜单栏
                break;
        }
    }

}
