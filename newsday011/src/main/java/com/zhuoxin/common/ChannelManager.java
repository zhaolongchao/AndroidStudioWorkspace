package com.zhuoxin.common;

import com.zhuoxin.dao.ChannelDAO;
import com.zhuoxin.entity.ChannelItem;


/**
 * Created by l on 2016/11/16.
 */

public class ChannelManager {
    public static ChannelDAO dao;
    public static void addChannelToDB(){
        dao=ChannelDAO.getChannelDAO();
        ChannelItem ci0=new ChannelItem(0,0,"游戏",CommonUtils.GAME,2);
        ChannelItem ci1=new ChannelItem(1,1,"美食",CommonUtils.FOOD,2);
        ChannelItem ci2=new ChannelItem(2,2,"娱乐",CommonUtils.YULE,2);
        ChannelItem ci3=new ChannelItem(3,3,"财经",CommonUtils.CAIJING,2);
        ChannelItem ci4=new ChannelItem(4,4,"科技",CommonUtils.SCIENCE,1);
        ChannelItem ci5=new ChannelItem(5,5,"汽车",CommonUtils.CAR,1);
        ChannelItem ci6=new ChannelItem(6,6,"笑话",CommonUtils.JOKE,1);
        ChannelItem ci7=new ChannelItem(7,7,"时尚",CommonUtils.FASHION,1);
        ChannelItem ci8=new ChannelItem(8,8,"情感",CommonUtils.EMOTION,1);
        ChannelItem[] channelItems={ci0,ci1,ci2,ci3,ci4,ci5,ci6,ci7,ci8};
        for (int i = 0; i <channelItems.length ; i++) {
            dao.addChannelItem(channelItems[i]);
        }

    }
}
