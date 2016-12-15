package com.zhuoxin.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zhuoxin.NewsApplication;
import com.zhuoxin.common.CommonUtils;
import com.zhuoxin.entity.UserFavoriteItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 2016/11/29.
 */

public class UserFavoriteDAO {
    private static UserFavoriteDAO userFavoriteDAO;
    //数据库辅助对象
    private SQLiteOpenHelper helper;
    private UserFavoriteDAO(){
        helper= NewsApplication.getApplication().getDBHelper();
    }
    public static UserFavoriteDAO getUserFavoriteDAO(){
        if(userFavoriteDAO==null){
            userFavoriteDAO=new UserFavoriteDAO();
        }
        return userFavoriteDAO;
    }

    /**
     * 将新闻添加到收藏表
     * @param userFavoriteItem
     */
    public void addUserFavoriteItem(UserFavoriteItem userFavoriteItem){
        ContentValues cv=new ContentValues();
        cv.put(CommonUtils.FAVORITE_NEWID,userFavoriteItem.getFavoriteNewsId());
        cv.put(CommonUtils.FAVORITE_NEWTITLE,userFavoriteItem.getFavoriteNewsTitle());
        cv.put(CommonUtils.FAVORITE_NEWSUMMARY,userFavoriteItem.getFavoriteNewsSummary());
        cv.put(CommonUtils.FAVORITE_NEWPIC,userFavoriteItem.getFavoriteNewsPic());
        cv.put(CommonUtils.FAVORITE_NEWLINK,userFavoriteItem.getFavoriteNewsLink());
        cv.put(CommonUtils.FAVORITE_USER_ID,userFavoriteItem.getFavoriteUserId());
        //获取数据库操作对象
        SQLiteDatabase db=helper.getWritableDatabase();
        db.insert(CommonUtils.FAVORITE_TABLE,null,cv);
        db.close();
    }

    /**
     * 根据用户id和新闻链接查询判断是否保存过新闻
     * @param link
     * @return
     */
    public boolean findNewsByFavoriteLink(int userId,String link){
        String sql="select count(*) from "+CommonUtils.FAVORITE_TABLE +" where "+
                CommonUtils.FAVORITE_USER_ID+" =? and "+CommonUtils.FAVORITE_NEWLINK +"=?";
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery(sql,new String[]{userId+"",link+""});
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
     * 根据用户ID查询收藏的新闻
     * @param userId
     * @return
     */
    public List<UserFavoriteItem> getFavoriteNews(int userId){
        String sql="select * from "+CommonUtils.FAVORITE_TABLE +" where "+
                CommonUtils.FAVORITE_USER_ID+" =? ";
        List<UserFavoriteItem> list=new ArrayList<UserFavoriteItem>();
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery(sql,new String[]{userId+""});
        while (cursor.moveToNext()){
            UserFavoriteItem userFavoriteItem=new UserFavoriteItem();
            userFavoriteItem.setFavoriteId(cursor.getInt(cursor.getColumnIndex(CommonUtils.FAVORITE_ID)));
            userFavoriteItem.setFavoriteNewsId(cursor.getInt(cursor.getColumnIndex(CommonUtils.FAVORITE_NEWID)));
            userFavoriteItem.setFavoriteNewsLink(cursor.getString(cursor.getColumnIndex(CommonUtils.FAVORITE_NEWLINK)));
            userFavoriteItem.setFavoriteNewsPic(cursor.getString(cursor.getColumnIndex(CommonUtils.FAVORITE_NEWPIC)));
            userFavoriteItem.setFavoriteNewsSummary(cursor.getString(cursor.getColumnIndex(CommonUtils.FAVORITE_NEWSUMMARY)));
            userFavoriteItem.setFavoriteNewsTitle(cursor.getString(cursor.getColumnIndex(CommonUtils.FAVORITE_NEWTITLE)));
            userFavoriteItem.setFavoriteUserId(cursor.getInt(cursor.getColumnIndex(CommonUtils.FAVORITE_USER_ID)));
            list.add(userFavoriteItem);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 根据用户ID和新闻ID删除收藏的新闻
     * @param userId
     */
    public void deleteFavoriteNews(int userId,String link){
        String sql="delete from "+CommonUtils.FAVORITE_TABLE +" where "+
                CommonUtils.FAVORITE_USER_ID+" =? and "+CommonUtils.FAVORITE_NEWLINK +"=?";
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL(sql,new Object[]{userId,link});
    }

}
