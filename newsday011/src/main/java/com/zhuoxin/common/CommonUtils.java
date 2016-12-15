package com.zhuoxin.common;

/**
 * Created by l on 2016/11/10.
 * 常量
 */

public class CommonUtils {
    /**新闻客户端核心板网址*/
    public static final String path1=
            "http://118.244.212.82:9092/newsClient/news_list?ver=1&subid=1&dir=1&nid=1&stamp=20140321&cnt=20";
    public static final String GAME= "http://c.m.163.com/nc/article/list/T1348654151579/0-20.html";//游戏
    public static final String FOOD= "http://c.m.163.com/nc/article/list/T1399700447917/0-20.html";//美食
    public static final String YULE= "http://c.m.163.com/nc/article/list/T1348648517839/0-20.html";//娱乐
    public static final String CAIJING= "http://c.m.163.com/nc/article/list/T1348648756099/0-20.html";//财经
    public static final String SCIENCE= "http://c.m.163.com/nc/article/list/T1348649580692/0-20.html";//科技
    public static final String CAR= "http://c.m.163.com/nc/article/list/T1348654060988/0-20.html";//汽车
    public static final String JOKE= "http://c.m.163.com/nc/article/list/T1350383429665/0-20.html";//笑话
    public static final String FASHION= "http://c.m.163.com/nc/article/list/T1348650593803/0-20.html";//时尚
    public static final String EMOTION= "http://c.m.163.com/nc/article/list/T1348650839000/0-20.html";//情感

    /**====================数据库相关表===============*/
    /**================频道管理表的表名和字段名===============*/
    public static final String CHANNEL_TABLE="t_channel";//表名
    public static final String CHANNEL_ID="id";//频道id
    public static final String CHANNEL_NAME="name";//频道名
    public static final String CHANNEL_LINK="link";//频道链接
    public static final String CHANNEL_ORDER="order_id";//排序
    public static final String CHANNEL_TYPE="type";//类型
    /**================用户管理表表名和字段名===============*/
    public static final String USER_TABLE="t_user";//表名
    public static final String USER_ID="id";//用户id
    public static final String USER_NAME="name";//用户名
    public static final String USER_EMAIL="email";//邮箱
    public static final String USER_PWD="password";//密码
    /**==========用户频道表表名和字段名============*/
    public static final String USER_CHANNEL_TABLE="t_user_channel";//频道表
    public static final String USER_CHANNEL_ID="id";//id
    public static final String USER_CHANNEL_USER_ID="user_id";//用户id
    public static final String USER_CHANNEL_CHANNEL_ID="channel_id";//频道id
    public static final String USER_CHANNEL_CHANNEL_TYPE="channel_type";//频道类型
    public static final String USER_CHANNEL_CHANNEL_ORDER="channel_order_id";//频道排序
    /**===========收藏表表名和字段名==============*/
    public static final String FAVORITE_TABLE="t_favorite";//收藏表
    public static final String FAVORITE_ID="id";//收藏id
    public static final String FAVORITE_NEWID="news_id";//新闻id
    public static final String FAVORITE_NEWTITLE="news_title";//新闻标题
    public static final String FAVORITE_NEWSUMMARY="news_summary";//新闻摘要
    public static final String FAVORITE_NEWPIC="news_pic";//新闻图片
    public static final String FAVORITE_NEWLINK="news_link";//新闻链接
    public static final String FAVORITE_USER_ID="user_id";//用户id

}
