package com.zhuoxin.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zhuoxin.common.CommonUtils;

/**
 * Created by l on 2016/11/16.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context,
                    String name,
                    SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }
    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        //频道管理表
        String sql="create table "+
                CommonUtils.CHANNEL_TABLE+"("+
                CommonUtils.CHANNEL_ID+" integer primary key autoincrement,"+
                CommonUtils.CHANNEL_NAME+" text,"+
                CommonUtils.CHANNEL_LINK+" text,"+
                CommonUtils.CHANNEL_ORDER+" int,"+
                CommonUtils.CHANNEL_TYPE+" int)";
        db.execSQL(sql);
        //用户管理表
        String sqlUser="create table "+
                CommonUtils.USER_TABLE+"("+
                CommonUtils.USER_ID+" integer primary key autoincrement,"+
                CommonUtils.USER_NAME+" text,"+
                CommonUtils.USER_EMAIL+" text,"+
                CommonUtils.USER_PWD+" text)";
        db.execSQL(sqlUser);
        //用户频道管理表
        String sqlUserChannel="create table "+
                CommonUtils.USER_CHANNEL_TABLE+"("+
                CommonUtils.USER_CHANNEL_ID+" integer primary key autoincrement,"+
                CommonUtils.USER_CHANNEL_USER_ID+" int,"+
                CommonUtils.USER_CHANNEL_CHANNEL_ID+" int,"+
                CommonUtils.USER_CHANNEL_CHANNEL_ORDER+" int,"+
                CommonUtils.USER_CHANNEL_CHANNEL_TYPE+" int)";
        db.execSQL(sqlUserChannel);
        //收藏表
        String sqlFavorite="create table "+
                CommonUtils.FAVORITE_TABLE+"("+
                CommonUtils.FAVORITE_ID+" integer primary key autoincrement,"+
                CommonUtils.FAVORITE_NEWID+" int,"+
                CommonUtils.FAVORITE_NEWTITLE+" text,"+
                CommonUtils.FAVORITE_NEWSUMMARY+" text,"+
                CommonUtils.FAVORITE_NEWPIC+" text,"+
                CommonUtils.FAVORITE_NEWLINK+" text,"+
                CommonUtils.FAVORITE_USER_ID+" int)";
        db.execSQL(sqlFavorite);
    }
    //更新数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql="drop table "+CommonUtils.CHANNEL_TABLE;
        db.execSQL(sql);
        String sqlUser="drop table "+CommonUtils.USER_TABLE;
        db.execSQL(sqlUser);
        String sqlUserChannel="drop table "+CommonUtils.USER_CHANNEL_TABLE;
        db.execSQL(sqlUserChannel);
        String sqlFavorite="drop table "+CommonUtils.FAVORITE_TABLE;
        db.execSQL(sqlFavorite);
        onCreate(db);
    }
}
