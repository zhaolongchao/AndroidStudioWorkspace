package com.zhuoxin.newsday01;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.zhuoxin.NewsApplication;
import com.zhuoxin.adapter.FavoriteAdapter;
import com.zhuoxin.base.BaseActivity;
import com.zhuoxin.dao.UserFavoriteDAO;
import com.zhuoxin.entity.UserFavoriteItem;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by l on 2016/12/2.
 */

public class FavoriteActivity extends BaseActivity {
    ListView lv;
    List<UserFavoriteItem> list=new ArrayList<UserFavoriteItem>();
    FavoriteAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        //设置导航条
        setTitleBar(R.mipmap.btn_homeasup_default,R.string.user_favorite,0);
        lv= (ListView) findViewById(R.id.lv_favorite);
        ShareSDK.initSDK(this,"197fd53a5798e");
        list=UserFavoriteDAO.getUserFavoriteDAO().
                getFavoriteNews(NewsApplication.getUserItem().getUserId());
        adapter=new FavoriteAdapter(list,this,lv);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(FavoriteActivity.this,ActivityWebView.class);
                intent.putExtra("url",list.get(i).getFavoriteNewsLink());
                startActivity(intent);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                initPopupWindow(view,i);//创建浮动窗口
                return true;
            }
        });
    }
    //创建浮动窗口
    private void initPopupWindow(View view, final int i) {
        View v=getLayoutInflater().inflate(R.layout.inflate_favorite_item,null);
        Button btnDelete= (Button) v.findViewById(R.id.btn_delete);
        Button btnShare1= (Button) v.findViewById(R.id.btn_share1);
        Button btnCancel1= (Button) v.findViewById(R.id.btn_cancel1);
        final PopupWindow popupWindow=new PopupWindow(
                v,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();//浮动窗口消失
                Toast.makeText(FavoriteActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                UserFavoriteDAO.getUserFavoriteDAO().deleteFavoriteNews
                        (NewsApplication.getUserItem().getUserId(),
                                list.get(i).getFavoriteNewsLink());
                list.remove(i);
                adapter.notifyDataSetChanged();
            }
        });
        btnShare1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();//浮动窗口消失
                Toast.makeText(FavoriteActivity.this,"选择分享",Toast.LENGTH_SHORT).show();
                showShare(i);
            }
        });
        btnCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();//浮动窗口消失
                Toast.makeText(FavoriteActivity.this,"取消成功",Toast.LENGTH_SHORT).show();
            }
        });
        //设置焦点
        popupWindow.setFocusable(true);
        //设置点击边界外使浮动窗口消失
        popupWindow.setOutsideTouchable(true);
        //设置背景 -- 为了在点击浮动窗口以外的区域使浮动窗口消失
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        //设置动画
        //popupWindow.setAnimationStyle(R.style.popupwindowAnimation);
        //设置显示位置，每一项子视图的左下方
        popupWindow.showAsDropDown(view,0,0);
    }
    //第三方分享
    private void showShare(int i) {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(list.get(i).getFavoriteNewsTitle().toString());
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(list.get(i).getFavoriteNewsTitle());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(list.get(i).getFavoriteNewsSummary());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(list.get(i).getFavoriteNewsLink());
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("这条新闻不错，可以看看！");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }

    public void onClick(View v){
        finish();//返回键
    }

}
