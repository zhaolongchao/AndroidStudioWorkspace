package com.zhuoxin.newsday01;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuoxin.NewsApplication;
import com.zhuoxin.adapter.ChannelMoreAdapter;
import com.zhuoxin.adapter.ChannelUserAdapter;
import com.zhuoxin.dao.ChannelDAO;
import com.zhuoxin.entity.ChannelItem;
import com.zhuoxin.entity.UserItem;
import com.zhuoxin.view.MoreGridView;
import com.zhuoxin.view.MyGridView;

import java.util.List;

/**
 * Created by l on 2016/11/17.
 * 频道管理
 */

public class ChannelActivity extends Activity implements AdapterView.OnItemClickListener {
    MyGridView mygv;
    MoreGridView moregv;
    List<ChannelItem> userChannels;//我的频道
    List<ChannelItem> moreChannels;//更多频道
    ChannelDAO dao;//频道数据库操作业务 对象
    ChannelUserAdapter userAdapter;//我的频道适配器
    ChannelMoreAdapter moreAdapter;//更多频道适配器
    boolean isMoving = false;
    UserItem userItem;//用户实体类
    int userId;//用户id
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_gridview);
        dao=ChannelDAO.getChannelDAO();
        initView();
        initData();
    }

    //初始化数据
    private void initData() {
        userItem= NewsApplication.getUserItem();//获取保存的用户实体类
        if(userItem==null){//用户未登录
            //默认情况下从数据库表t_channel中查询数据
            userChannels=dao.getChannels(1);//我的频道的数据
            moreChannels=dao.getChannels(2);//更多频道的数据
        }else{//用户已登录
            userId=userItem.getUserId();//用户id
            //判断用户是否保存过频道，保存过就从用户频道拿，没有就拿默认的
            if(dao.findDataByUserId(userId)){
                userChannels=dao.getUserChannels(userId,1);//我的频道
                moreChannels=dao.getUserChannels(userId,2);//更多频道
            }else{
                userChannels=dao.getChannels(1);//我的频道的数据
                moreChannels=dao.getChannels(2);//更多频道的数据
            }
        }
        userAdapter=new ChannelUserAdapter(userChannels,this);
        moreAdapter=new ChannelMoreAdapter(moreChannels,this);
        mygv.setAdapter(userAdapter);
        moregv.setAdapter(moreAdapter);
    }
    //初始化控件
    private void initView() {
        mygv= (MyGridView) findViewById(R.id.mygv);
        moregv= (MoreGridView) findViewById(R.id.moregv);
        //注册单项点击监听
        mygv.setOnItemClickListener(this);
        moregv.setOnItemClickListener(this);
    }
    public void onClick(View view){
        finish();
    }

    /**
     * 处理监听
     a.通过view可以获取到当前点击的控件
     (内容,屏幕中的位置--动画开始的位置，动画层中的视图样式)
     b.创建出动画层（执行动画的控件和动画层的容器）平移动画--补间动画
     c.数据的处理
     --控制点击位置的控件的视图效果--内容不可见
     --对方的集合追加数据，适配数据,找到最后一个控件在屏幕中的位置--动画结束的位置
     (可能存在延迟--使用handler延迟执行动画)
     d.对动画添加监听，在动画结束之后（点击者的gridview真正的移出数据，
     接受者显示内容，清除动画层的控件）
     * @param adapterView   gridview
     * @param view  当前点击的视图
     * @param i 位置
     * @param l  id
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        if (isMoving){
            return;
        }
        //获取当前点击的控件
        TextView tv= (TextView) view.findViewById(R.id.tv_channel_item);
        //动画开始的位置---控件左上角的位置
        final int[] startPosition=new int[2];
        //获取当前控件在屏幕中的位置
        tv.getLocationInWindow(startPosition);
//        //==========不带动画效果移动频道============
//        switch (adapterView.getId()){
//            case R.id.mygv://我的频道
//                //点击位置对应的频道数据
//                ChannelItem channelItem=userChannels.get(i);
//                //向另外一个集合中追加
//                moreAdapter.getBaseData().add(channelItem);
//                userAdapter.getBaseData().remove(channelItem);
//                moreAdapter.notifyDataSetChanged();
//                userAdapter.notifyDataSetChanged();
//                break;
//            case R.id.moregv://更多频道
//                //点击位置对应的频道数据
//                ChannelItem channelItem1=moreChannels.get(i);
//                //向另外一个集合中追加
//                userAdapter.getBaseData().add(channelItem1);
//                moreAdapter.getBaseData().remove(channelItem1);
//                userAdapter.notifyDataSetChanged();
//                moreAdapter.notifyDataSetChanged();
//                break;
//        }
        //=========带动画移动频道数据========
        switch (adapterView.getId()){
            case R.id.mygv://我的频道
                if(i<=1){
                    return;
                }
                //点击位置对应的频道数据
                final ChannelItem channelItem=userChannels.get(i);
                //向另外一个集合中追加
                moreAdapter.getBaseData().add(channelItem);
                //保存点击的位置--重新适配，对该位置的数据设置为不显示
                userAdapter.saveClickPosition(i);
                //设置最后一个元素内容不可见
                moreAdapter.setTextVisibile(false);
                moreAdapter.notifyDataSetChanged();
                //创建动画层
                final ViewGroup group=getViewGroup();
                //获取textview的缓存视图,将动画层中添加控件
                final ImageView imageView=setAnimView(getCacheView(tv),group);
                //设置延迟获取动画的终点坐标
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //获取更多频道中最后一个位置的视图控件
                        TextView tv= (TextView) moregv.getChildAt(moregv.getLastVisiblePosition())
                                .findViewById(R.id.tv_channel_item);
                        //获取坐标
                        int[] endPosition=new int[2];
                        tv.getLocationInWindow(endPosition);
                        //创建动画
                        startAnim(channelItem,startPosition,endPosition,
                                            imageView,mygv,group);
                    }
                },100);
                break;
            case R.id.moregv://更多频道
                //点击位置对应的频道数据
                final ChannelItem channelItem1=moreChannels.get(i);
                //向另外一个集合中追加
                userAdapter.getBaseData().add(channelItem1);
                //保存点击的位置--重新适配，对该位置的数据设置为不显示
                moreAdapter.saveClickPosition1(i);
                //设置最后一个元素内容不可见
                userAdapter.setTextVisibile1(false);
                userAdapter.notifyDataSetChanged();
                //创建动画层
                final ViewGroup group1=getViewGroup();
                //获取textview的缓存视图,将动画层中添加控件
                final ImageView imageView1=setAnimView(getCacheView(tv),group1);
                //设置延迟获取动画的终点坐标
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //获取我的频道中最后一个位置的视图控件
                        TextView tv= (TextView) mygv.getChildAt(mygv.getLastVisiblePosition())
                                                .findViewById(R.id.tv_channel_item);
                        //获取坐标
                        int[] endPosition=new int[2];
                        tv.getLocationInWindow(endPosition);
                        //创建动画
                        startAnim(channelItem1,startPosition,endPosition,
                                imageView1,moregv,group1);
                    }
                },100);
                break;
        }

    }



    /**
     * 从缓存中获取视图
     * @param tv
     * @return
     */
    private ImageView getCacheView(TextView tv) {
        //销毁之前的缓存
        tv.destroyDrawingCache();
        //开启缓存
        tv.setDrawingCacheEnabled(true);
        //获取缓存对象
        Bitmap bitmap=Bitmap.createBitmap(tv.getDrawingCache());
        //关闭缓存
        tv.setDrawingCacheEnabled(false);
        //创建imageview
        ImageView iv=new ImageView(this);
        //设置bitmap
        iv.setImageBitmap(bitmap);
        return iv;
    }
    /**
     * 创建动画层
     * 根据界面的绘制结构获取根视图（DecorView）--FrameLayout,可以叠加多层视图
     */
    private ViewGroup getViewGroup(){
        ViewGroup group= (ViewGroup) getWindow().getDecorView();
        //创建一个布局容器---线性布局
        LinearLayout ll=new LinearLayout(this);
        //设置宽高
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(params);
        //将线性布局添加到根视图中
        group.addView(ll);
        return ll;
    }
    /**
     * 将缓存视图控件添加到动画层容器中
     */
    private ImageView setAnimView(ImageView iv,ViewGroup group){
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin=0;//左外边距
        params.topMargin=0;//上外边距
        iv.setLayoutParams(params);
        //添加到容器中
        group.addView(iv);
        return iv;
    }

    /**
     * 创建动画
     * @param channelItem
     * @param startPosition 动画开始的位置
     * @param endPosition 动画 结束的位置
     * @param imageView 动画层控件
     * @param gridView 网格视图
     * @param group 动画层容器
     */
    private void startAnim(final ChannelItem channelItem,
                           int[] startPosition,
                           int[] endPosition,
                           final ImageView imageView,
                           final GridView gridView,
                           final ViewGroup group) {
        //创建平移动画
        TranslateAnimation ta=new TranslateAnimation(
                startPosition[0],endPosition[0],
                startPosition[1],endPosition[1]);
        ta.setFillAfter(true);
        ta.setDuration(500);
        //启动动画
        imageView.startAnimation(ta);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isMoving=true;
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
            @Override//动画结束之后更新界面
            public void onAnimationEnd(Animation animation) {
                isMoving=false;
                //我的频道
                if(gridView==mygv){
                    //点击的数据从我的频道适配器中移除
                    userAdapter.getBaseData().remove(channelItem);
                    userAdapter.notifyDataSetChanged();
                    //设置更多频道新追加的那一项内容可见
                    moreAdapter.setTextVisibile(true);
                }else if(gridView==moregv){//更多频道
                    //点击的数据从更多频道适配器中移除
                    moreAdapter.getBaseData().remove(channelItem);
                    moreAdapter.notifyDataSetChanged();
                    //设置我的频道新追加的那一项内容可见
                    userAdapter.setTextVisibile1(true);
                }
                //清除动画层的内容
                group.removeView(imageView);
            }
        });

    }
    //判断用户是否登录，进行数据的更改
    @Override
    protected void onPause() {
        super.onPause();
        userItem=NewsApplication.getUserItem();
        if(userItem!=null){
            userId=userItem.getUserId();
            if(dao.findDataByUserId(userId)){//用户保存过频道数据
                //更新频道信息
                for (int i=0;i<userChannels.size();i++){
                    //我的频道
                    userChannels.get(i).setOrderId(i+1);
                    userChannels.get(i).setType(1);
                    dao.updateUserChannels(userChannels.get(i),userId);
                }
                for (int j=0;j<moreChannels.size();j++){
                    //更多频道
                    moreChannels.get(j).setOrderId(j+1);
                    moreChannels.get(j).setType(2);
                    dao.updateUserChannels(moreChannels.get(j),userId);
                }
            }else {//没有保存过频道数据
                //添加频道信息
                for (int i = 0; i < userChannels.size(); i++) {
                    //我的频道
                    userChannels.get(i).setType(1);
                    userChannels.get(i).setOrderId(i + 1);
                    dao.addUserChannel(userChannels.get(i), userId);
                }

                for (int j = 0; j < moreChannels.size(); j++) {
                    //更多频道
                    moreChannels.get(j).setOrderId(j + 1);
                    moreChannels.get(j).setType(2);
                    dao.addUserChannel(moreChannels.get(j), userId);
                }
            }
        }

    }
}
