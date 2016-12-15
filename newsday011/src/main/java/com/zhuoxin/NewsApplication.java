package com.zhuoxin;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import com.zhuoxin.db.DBHelper;
import com.zhuoxin.entity.UserItem;

/**
 * Created by l on 2016/11/16.
 * 应用程序类
 * 封装一些在整个应用程序进程中可能会使用的数据（不区分模块，不分组件）
 * 在整个应用程序进程中有且只有一个application对象--单例模式
 */

public class NewsApplication extends Application {
    //应用程序对象
    private static NewsApplication application;
    //数据库辅助对象
    private SQLiteOpenHelper helper;
    private static UserItem userItem;
    private static Bitmap bitmap;
    //重写onCreate方法  在application对象创建之后会调用此方法，
    //获取application对象
    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
    }
    public static NewsApplication getApplication(){
        return application;
    }
    //获取数据库辅助对象
    public SQLiteOpenHelper getDBHelper(){
        if(helper==null){
            helper=new DBHelper(this,"news.db",null,1);
        }
        return helper;
    }

    public static UserItem getUserItem() {
        return userItem;
    }

    public static void setUserItem(UserItem userItem) {
        NewsApplication.userItem = userItem;
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public static void setBitmap(Bitmap bitmap) {
        NewsApplication.bitmap = bitmap;
    }
}
