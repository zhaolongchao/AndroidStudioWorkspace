package com.zhuoxin.entity;

/**
 * Created by l on 2016/11/17.
 */

public class TodayNews {
    private String source;//新闻来源
    private String digest;//新闻摘要
    private String imgsrc;//新闻图片的地址
    private String title;//新闻标题
    private String url;//新闻详情地址

    public TodayNews() {
    }

    public TodayNews(String source, String digest, String imgsrc, String title, String url) {
        this.source = source;
        this.digest = digest;
        this.imgsrc = imgsrc;
        this.title = title;
        this.url = url;
    }

    public TodayNews(String digest, String imgsrc, String title, String url) {
        this.digest = digest;
        this.imgsrc = imgsrc;
        this.title = title;
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "TodayNews{" +
                "source='" + source + '\'' +
                ", digest='" + digest + '\'' +
                ", imgsrc='" + imgsrc + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
