package com.zhuoxin.entity;

/**
 * Created by l on 2016/11/15.
 * 频道数据实体类
 */

public class ChannelItem {
    private int channelId;//菜单的id  频道id
    private int orderId;//排序id
    private String channelName;//菜单名字
    private String channelLink;//菜单新闻连接
    private int type;//我的频道，更多频道1,2
    public ChannelItem() {
    }

    public ChannelItem(int channelId,
                       int orderId,
                       String channelName,
                       String channelLink,
                       int type) {
        this.channelId = channelId;
        this.orderId = orderId;
        this.channelName = channelName;
        this.channelLink = channelLink;
        this.type = type;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelLink() {
        return channelLink;
    }

    public void setChannelLink(String channelLink) {
        this.channelLink = channelLink;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ChannelItem{" +
                "channelId=" + channelId +
                ", orderId=" + orderId +
                ", channelName='" + channelName + '\'' +
                ", channelLink='" + channelLink + '\'' +
                ", type=" + type +
                '}';
    }
}
