package com.zhuoxin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhuoxin.baseAdapter.BaseAdapter1;
import com.zhuoxin.entity.UserFavoriteItem;
import com.zhuoxin.newsday01.R;
import com.zhuoxin.utils.ImageLoad;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;

/**
 * Created by l on 2016/11/10.
 * 新闻标签适配器
 */

public class FavoriteAdapter extends BaseAdapter1<UserFavoriteItem> {
    //创建图片加载对象
    private ImageLoad imageLoad;
    //1.使用缓存图片优化listview
    HashMap<String,SoftReference<Bitmap>> map=new HashMap<String,SoftReference<Bitmap>>();
    //表示当前listview是否快速滑动
    boolean isFling=false;
    //应用程序图标
    Bitmap bitmap=null;
    public FavoriteAdapter(List baseData, Context context, final ListView listView) {
        super(baseData, context);
        //加载默认图片
        setDefaultBitmap();
        //滚动监听
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            /**
             * 滚动状态改变监听回调方法，会调用两到三次
             * 1.liseview对象
             * 2.滚动状态   分为三种情况
             *  0表示停止 1表示触摸状态下滚动  2表示手指离开屏幕，惯性滚动
             */
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                isFling=false;
                if(i== AbsListView.OnScrollListener.SCROLL_STATE_FLING){//快速滑动
                    isFling=true;
                }else if(i== AbsListView.OnScrollListener.SCROLL_STATE_IDLE){ //停止滑动
                    //重新适配
                    notifyDataSetChanged();
                }
            }
            /**
             * 屏幕滚动中多次调用
             * 1.listview对象
             * 2.屏幕上第一个可见的下标
             * 3.屏幕上的可见项(包括小半个)
             * 4.总个数
             */
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
        //初始化图片加载对象
        imageLoad=new ImageLoad(new ImageLoad.ImageLoadListener(){
            @Override
            public void imageLoadOK(String url, Bitmap bitmap) {
                /**
                 * 更新UI组件
                 * 在listview根据url查找当前适配bitmap数据的图片控件
                 */
                ImageView iv= (ImageView) listView.findViewWithTag(url);
                //有可能找的url对应的控件被销毁
                if(iv!=null){
                    iv.setImageBitmap(bitmap);
                }
            }
        },context);
    }
    /**
     * 设置默认图片
     */
    private void setDefaultBitmap() {
        bitmap= BitmapFactory.decodeResource(context.getResources(), R.mipmap.defaultpic);
    }
    //2.封装保存数据到软引用中的方法
    private void saveBitmapToCacheSoft(String url,Bitmap bitmap){
        //创建软引用对象封装bitmap
        SoftReference<Bitmap> sf=new SoftReference<Bitmap>(bitmap);
        map.put(url, sf);
    }
    // 3.从缓存中获取数据
    private Bitmap getBitmapFromCacheSoft(String url){
        Bitmap bitmap=null;
        //判断map中是否包含指定包名的图片对象
        if(map.containsKey(url)){
            SoftReference<Bitmap> sf=map.get(url);
            bitmap=sf.get();
        }
        return bitmap;
    }
    private Bitmap getBitmapSoft(String url,int position){
        //判断是否快速滑动
        if(isFling){
            return bitmap;
        }
        //从缓存中获取
        Bitmap bitmap=getBitmapFromCacheSoft(url);
        return bitmap;
    }
    /**
     * 返回当前适配的View
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if (view == null) {
            //加载布局
            view=inflater.inflate(R.layout.inflate_news_list_item,null);
            //实例化子布局中的控件
            holder=new ViewHolder(view);
            //缓存
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        //获取控件，从集合中取出数据给每一个控件设置
        UserFavoriteItem ufi=baseData.get(i);
        holder.tvTitle.setText(ufi.getFavoriteNewsTitle());//设置标题
        holder.tvSummary.setText(ufi.getFavoriteNewsSummary());//设置摘要
        String url=ufi.getFavoriteNewsPic();//获得图片链接地址
        //为了区分每一个控件需要加载的图片,绑定tag使用url
        holder.ivIcon.setTag(url);
        holder.ivIcon.setImageResource(R.mipmap.defaultpic);
        //加载图片
        //imageLoad.startAsyncTask(url);//启动异步任务
        Bitmap bitmap=imageLoad.getBitmap(url);
        if(bitmap!=null){
            holder.ivIcon.setImageBitmap(bitmap);
        }
        return view;
    }

    /**
     * 封装控件的实体类
     */
    class ViewHolder{
        TextView tvTitle;
        TextView tvSummary;
        ImageView ivIcon;
        public ViewHolder(View v){
            ivIcon= (ImageView) v.findViewById(R.id.iv_news);
            tvTitle= (TextView) v.findViewById(R.id.tv_title);
            tvSummary= (TextView) v.findViewById(R.id.tv_message);
        }
    }

}
