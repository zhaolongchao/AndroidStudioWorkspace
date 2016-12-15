package com.zhuoxin.entity;

/**
 * Created by l on 2016/11/29.
 * 用户新闻收藏实体类
 */

public class UserFavoriteItem {
    private int favoriteId;//收藏id
    private int favoriteNewsId;//新闻id
    private String favoriteNewsTitle;//新闻标题
    private String favoriteNewsSummary;//新闻摘要
    private String favoriteNewsPic;//新闻图片
    private String favoriteNewsLink;//新闻链接
    private int favoriteUserId;//用户id

    public UserFavoriteItem() {
    }

    public UserFavoriteItem(int favoriteId, int favoriteNewsId,
                            String favoriteNewsTitle, String favoriteNewsSummary,
                            String favoriteNewsLink, String favoriteNewsPic,
                            int favoriteUserId) {
        this.favoriteId = favoriteId;
        this.favoriteNewsId = favoriteNewsId;
        this.favoriteNewsTitle = favoriteNewsTitle;
        this.favoriteNewsSummary = favoriteNewsSummary;
        this.favoriteNewsLink = favoriteNewsLink;
        this.favoriteNewsPic = favoriteNewsPic;
        this.favoriteUserId = favoriteUserId;
    }

    public int getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
    }

    public int getFavoriteNewsId() {
        return favoriteNewsId;
    }

    public void setFavoriteNewsId(int favoriteNewsId) {
        this.favoriteNewsId = favoriteNewsId;
    }

    public String getFavoriteNewsTitle() {
        return favoriteNewsTitle;
    }

    public void setFavoriteNewsTitle(String favoriteNewsTitle) {
        this.favoriteNewsTitle = favoriteNewsTitle;
    }

    public String getFavoriteNewsLink() {
        return favoriteNewsLink;
    }

    public void setFavoriteNewsLink(String favoriteNewsLink) {
        this.favoriteNewsLink = favoriteNewsLink;
    }

    public String getFavoriteNewsPic() {
        return favoriteNewsPic;
    }

    public void setFavoriteNewsPic(String favoriteNewsPic) {
        this.favoriteNewsPic = favoriteNewsPic;
    }

    public String getFavoriteNewsSummary() {
        return favoriteNewsSummary;
    }

    public void setFavoriteNewsSummary(String favoriteNewsSummary) {
        this.favoriteNewsSummary = favoriteNewsSummary;
    }

    public int getFavoriteUserId() {
        return favoriteUserId;
    }

    public void setFavoriteUserId(int favoriteUserId) {
        this.favoriteUserId = favoriteUserId;
    }
}
