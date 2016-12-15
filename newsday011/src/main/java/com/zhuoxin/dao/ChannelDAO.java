package com.zhuoxin.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zhuoxin.NewsApplication;
import com.zhuoxin.common.CommonUtils;
import com.zhuoxin.entity.ChannelItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 2016/11/16.
 * 频道管理数据操作的业务类
 * 单例模式
 */

public class ChannelDAO {
    private static ChannelDAO channelDAO;
    //数据库辅助对象
    private SQLiteOpenHelper helper;
    private ChannelDAO(){
        helper= NewsApplication.getApplication().getDBHelper();
    }
    public static ChannelDAO getChannelDAO(){
        if(channelDAO==null){
            channelDAO=new ChannelDAO();
        }
        return channelDAO;
    }

    /**
     * 将频道数据添加到数据库
     * @param channelItem
     */
    public void addChannelItem(ChannelItem channelItem){
        ContentValues cv=new ContentValues();
        cv.put(CommonUtils.CHANNEL_NAME,channelItem.getChannelName());
        cv.put(CommonUtils.CHANNEL_LINK,channelItem.getChannelLink());
        cv.put(CommonUtils.CHANNEL_ORDER,channelItem.getOrderId());
        cv.put(CommonUtils.CHANNEL_TYPE,channelItem.getType());
        //获取数据库操作对象
        SQLiteDatabase db=helper.getWritableDatabase();
        db.insert(CommonUtils.CHANNEL_TABLE,null,cv);
        db.close();
    }

    /**
     * 查询频道  t_channel 默认频道数据与用户无关
     * @param type
     * @return
     */
    public List<ChannelItem> getChannels(int type){
        String sql="select * from "+CommonUtils.CHANNEL_TABLE +" where "+
                CommonUtils.CHANNEL_TYPE +"="+type;
        List<ChannelItem> list=new ArrayList<ChannelItem>();
        SQLiteDatabase db=helper.getReadableDatabase();
        //查询数据
        Cursor cursor=db.rawQuery(sql,null);
        while(cursor.moveToNext()){
            ChannelItem channelItem=new ChannelItem();
            channelItem.setChannelId(cursor.getInt(cursor.getColumnIndex(CommonUtils.CHANNEL_ID)));
            channelItem.setChannelName(cursor.getString(cursor.getColumnIndex(CommonUtils.CHANNEL_NAME)));
            channelItem.setChannelLink(cursor.getString(cursor.getColumnIndex(CommonUtils.CHANNEL_LINK)));
            channelItem.setOrderId(cursor.getInt(cursor.getColumnIndex(CommonUtils.CHANNEL_ORDER)));
            channelItem.setType(cursor.getInt(cursor.getColumnIndex(CommonUtils.CHANNEL_TYPE)));
            list.add(channelItem);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 用户频道数据添加
     * @param item 频道对象
     * @param userId 用户id
     */
    public void addUserChannel(ChannelItem item,int userId){
        String sql="insert into "+CommonUtils.USER_CHANNEL_TABLE+"("+
                CommonUtils.USER_CHANNEL_USER_ID+","+
                CommonUtils.USER_CHANNEL_CHANNEL_ID+","+
                CommonUtils.USER_CHANNEL_CHANNEL_ORDER+","+
                CommonUtils.USER_CHANNEL_CHANNEL_TYPE+") values (?,?,?,?)";
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL(sql,new Object[]{userId,item.getChannelId(),item.getOrderId(),item.getType()});
    }
    /**
     * 查询某一用户是否保存过频道数据
     * @param userId 用户id
     */
    public boolean findDataByUserId(int userId){
        String sql="select count(*) from "+
                CommonUtils.USER_CHANNEL_TABLE +" where "+
                CommonUtils.USER_CHANNEL_USER_ID+"="+userId;
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery(sql,null);
        int count=0;
        if(cursor.moveToNext()){
            count=cursor.getInt(0);
        }
        if(count==0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 用户频道数据查询
     * @param userId  用户id
     * @param type  频道类型
     * @return
     */
    public List<ChannelItem> getUserChannels(int userId,int type){
        List<ChannelItem> list=new ArrayList<ChannelItem>();
        String sql="select uc.*,c."+CommonUtils.CHANNEL_NAME+
                ",c."+CommonUtils.CHANNEL_LINK+
                " from "+CommonUtils.USER_CHANNEL_TABLE +" as uc join "+
                CommonUtils.CHANNEL_TABLE +" as c on "+
                "uc."+CommonUtils.USER_CHANNEL_ID+"=c."+CommonUtils.CHANNEL_ID+
                " and uc."+CommonUtils.USER_CHANNEL_USER_ID+"="+userId+
                " where uc."+CommonUtils.USER_CHANNEL_CHANNEL_TYPE+"="+type+
                " order by uc."+CommonUtils.USER_CHANNEL_CHANNEL_ORDER;
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery(sql,null);
        while(cursor.moveToNext()){
            ChannelItem channelItem=new ChannelItem();
            channelItem.setChannelId(cursor.getInt(2));
            channelItem.setChannelName(cursor.getString(5));
            channelItem.setChannelLink(cursor.getString(6));
            channelItem.setOrderId(cursor.getInt(3));
            channelItem.setType(type);
            list.add(channelItem);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 用户频道数据更新
     */
    public void updateUserChannels(ChannelItem channelItem,int userId){
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(CommonUtils.USER_CHANNEL_CHANNEL_ORDER,channelItem.getOrderId());
        cv.put(CommonUtils.USER_CHANNEL_CHANNEL_TYPE,channelItem.getType());
        db.update(CommonUtils.USER_CHANNEL_TABLE+"",cv,
                CommonUtils.USER_CHANNEL_CHANNEL_ID+"=? and "+CommonUtils.USER_CHANNEL_USER_ID+"=?",
                new String[]{channelItem.getChannelId()+"",userId+""});
        db.close();
    }
}
