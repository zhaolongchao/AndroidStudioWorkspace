package com.zhuoxin.entity;

/**
 * Created by l on 2016/11/10.
 * 新闻实体
 */

public class News {
    private String icon; /**新闻图片地址*/
    private String link;/**新闻详情地址*/
    private int nid;/**新闻id*/
    private String stamp; /**新闻发布时间*/
    private String summary; /**新闻摘要*/
    private String title; /**新闻标题*/
    private int type;


    public News(String icon, String link, int nid, String summary, String title) {
        this.icon = icon;
        this.link = link;
        this.nid = nid;
        this.summary = summary;
        this.title = title;
    }

    /**新闻类型*/

    public News(String icon, String link, int nid, String stamp, String summary, String title, int type) {
        this.icon = icon;
        this.link = link;
        this.nid = nid;
        this.stamp = stamp;
        this.summary = summary;
        this.title = title;
        this.type = type;
    }

    public News() {
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "News{" +
                "icon='" + icon + '\'' +
                ", link='" + link + '\'' +
                ", nid=" + nid +
                ", stamp='" + stamp + '\'' +
                ", summary='" + summary + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                '}';
    }
}
