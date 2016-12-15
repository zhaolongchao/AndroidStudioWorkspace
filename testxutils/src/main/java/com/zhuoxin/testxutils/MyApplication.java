package com.zhuoxin.testxutils;

import android.app.Application;

import org.xutils.x;

/**
 * Created by l on 2016/11/29.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Xutils框架
        x.Ext.init(this);
    }
}
