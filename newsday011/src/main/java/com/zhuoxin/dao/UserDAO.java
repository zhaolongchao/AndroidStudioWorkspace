package com.zhuoxin.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zhuoxin.NewsApplication;
import com.zhuoxin.common.CommonUtils;
import com.zhuoxin.entity.UserItem;

/**
 * Created by l on 2016/11/25.
 */

public class UserDAO {
    private static UserDAO userDAO;
    //数据库辅助对象
    private SQLiteOpenHelper helper;
    private UserDAO(){
        helper= NewsApplication.getApplication().getDBHelper();
    }
    public static UserDAO getUserDAO(){
        if(userDAO==null){
            userDAO=new UserDAO();
        }
        return userDAO;
    }

    /**
     * 将用户数据添加到数据库
     * @param userItem
     */
    public void addUserItem(UserItem userItem){
        ContentValues cv=new ContentValues();
        cv.put(CommonUtils.USER_EMAIL,userItem.getUserEmail());
        cv.put(CommonUtils.USER_NAME,userItem.getUserName());
        cv.put(CommonUtils.USER_PWD,userItem.getUserPwd());
        //获取数据库操作对象
        SQLiteDatabase db=helper.getWritableDatabase();
        db.insert(CommonUtils.USER_TABLE,null,cv);
        db.close();
    }

    /**
     * 按邮箱查询用户数据
     * @param email
     * @return
     */
    public UserItem getUsers(String email){
        String sql="select * from "+CommonUtils.USER_TABLE +" where "+
                CommonUtils.USER_EMAIL +"='"+email+"'";
        UserItem userItem=new UserItem();
        SQLiteDatabase db=helper.getReadableDatabase();
        //查询数据
        Cursor cursor=db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            userItem.setUserId(cursor.getInt(cursor.getColumnIndex(CommonUtils.USER_ID)));
            userItem.setUserEmail(cursor.getString(cursor.getColumnIndex(CommonUtils.USER_EMAIL)));
            userItem.setUserName(cursor.getString(cursor.getColumnIndex(CommonUtils.USER_NAME)));
            userItem.setUserPwd(cursor.getString(cursor.getColumnIndex(CommonUtils.USER_PWD)));
        }
        cursor.close();
        db.close();
        return userItem;
    }

    /**
     * 按邮箱修改密码
     * 修改数据 update 表名 set 列名=值,列名=值...+where 条件
     * update student set sname='tom' where id=1;
     * @param email
     */
    public void upDateUserPwd(String pwd,String email){
        String sql="update "+CommonUtils.USER_TABLE +" set "+CommonUtils.USER_PWD+
                " =? "+" where "+CommonUtils.USER_EMAIL +"=?";
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL(sql,new String[]{pwd,email});
    }
}
