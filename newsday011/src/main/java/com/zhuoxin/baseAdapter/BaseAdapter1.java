package com.zhuoxin.baseAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 2016/11/10.
 * 基本适配器的父类
 */

public abstract class BaseAdapter1<T> extends BaseAdapter{
    protected List<T> baseData=new ArrayList<T>();//适配器集合
    public Context context;//应用上下文对象
    public LayoutInflater inflater;//布局加载器

    public BaseAdapter1(List<T> baseData, Context context) {
        this.baseData = baseData;
        this.context = context;
        inflater=LayoutInflater.from(context);
    }

    public List<T> getBaseData() {
        return baseData;
    }

    public void setBaseData(List<T> baseData) {
        this.baseData = baseData;
    }
    /**
     * 适配数据的数量
     */
    @Override
    public int getCount() {
        return baseData.size();

    }
    /*
    返回当前适配的数据
     */
    @Override
    public T getItem(int i) {
        return baseData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


}
