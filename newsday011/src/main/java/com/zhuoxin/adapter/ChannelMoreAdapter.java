package com.zhuoxin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuoxin.baseAdapter.BaseAdapter1;
import com.zhuoxin.entity.ChannelItem;
import com.zhuoxin.newsday01.R;

import java.util.List;

/**
 * Created by l on 2016/11/17.
 */

public class ChannelMoreAdapter extends BaseAdapter1<ChannelItem> {
    //点击的位置
    private int position=-1;
    //最后一个元素是否可见
    private boolean isVisibile=true;
    public ChannelMoreAdapter(List<ChannelItem> baseData, Context context) {
        super(baseData, context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=inflater.inflate(R.layout.inflate_news_channel_item,null);
        TextView tv= (TextView) view.findViewById(R.id.tv_channel_item);
        //设置频道名字
        tv.setText(getItem(i).getChannelName());
        //设置新追加的元素不可见
        if(!isVisibile && i==baseData.size()-1){
            tv.setText("");
            tv.setSelected(true);
            tv.setEnabled(false);
        }
        //设置选中的tv内容不可见
        if(i==position){
            tv.setText("");
            //设置控件背景效果
            tv.setSelected(true);
            tv.setEnabled(false);
            position=-1;
        }
        return view;
    }
    //设置最后一个元素内容不可见
    public void setTextVisibile(boolean b) {
        isVisibile=b;
        //重新适配
        notifyDataSetChanged();
    }
    //保存当前点击的位置
    public void saveClickPosition1(int i) {
        position=i;
        //重新适配
        notifyDataSetChanged();
    }
}
