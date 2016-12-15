package com.zhuoxin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhuoxin.baseAdapter.BaseAdapter1;
import com.zhuoxin.entity.News;
import com.zhuoxin.newsday01.R;
import com.zhuoxin.utils.ImageLoad;

import java.util.List;

/**
 * Created by l on 2016/11/10.
 * 新闻标签适配器
 */

public class NewsAdapter extends BaseAdapter1<News> {
    //创建图片加载对象
    private ImageLoad imageLoad;
    public NewsAdapter(List baseData, Context context, final ListView listView) {
        super(baseData, context);
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
        News n=baseData.get(i);
        holder.tvTitle.setText(n.getTitle());//设置标题
        holder.tvSummary.setText(n.getSummary());//设置摘要
        String url=n.getIcon();//获得图片链接地址
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
