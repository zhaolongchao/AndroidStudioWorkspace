package com.zhuoxin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhuoxin.NewsApplication;
import com.zhuoxin.dao.UserDAO;
import com.zhuoxin.entity.UserItem;
import com.zhuoxin.newsday01.LoginActivity;
import com.zhuoxin.newsday01.MyAccountActivity;
import com.zhuoxin.newsday01.NewsHSVActivity;
import com.zhuoxin.newsday01.R;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by l on 2016/11/20.
 */

public class FragmentRight extends Fragment implements View.OnClickListener {
    NewsHSVActivity activity;//fragment的关联的activity
    private ImageView ivLogin;
    private Button btnLogin;
    private ImageView ivShareWeixin;
    private ImageView ivShareQq;
    private ImageView ivShareFriends;
    private ImageView ivShareWeibo;
    String name;//用户名
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.right_menu,null);
        activity= (NewsHSVActivity) getActivity();
        assignViews(v);//实例化控件
        ShareSDK.initSDK(activity,"197fd53a5798e");
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        UserItem userItem=NewsApplication.getUserItem();
        if(userItem==null){
            btnLogin.setText("立刻登录");
        }else{
            name= UserDAO.getUserDAO().getUsers(userItem.getUserEmail()).getUserName();
            btnLogin.setText(name);
        }
        if(NewsApplication.getBitmap()!=null){
            ivLogin.setImageBitmap(NewsApplication.getBitmap());
        }
        initListener();//设置监听
    }

    //实例化控件
    private void assignViews(View v) {
        ivLogin = (ImageView) v.findViewById(R.id.iv_login);
        btnLogin = (Button) v.findViewById(R.id.btn_login);
        ivShareWeixin = (ImageView) v.findViewById(R.id.iv_share_weixin);
        ivShareQq = (ImageView) v.findViewById(R.id.iv_share_qq);
        ivShareFriends = (ImageView) v.findViewById(R.id.iv_share_friends);
        ivShareWeibo = (ImageView) v.findViewById(R.id.iv_share_weibo);
    }
    //设置监听
    private void initListener() {
        ivLogin.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        ivShareWeixin.setOnClickListener(this);
        ivShareQq.setOnClickListener(this);
        ivShareFriends.setOnClickListener(this);
        ivShareWeibo.setOnClickListener(this);
    }
    //单项点击事件
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_login://登录头像
                if (btnLogin.getText().toString().equals("立刻登录")){
                    Intent intent=new Intent(activity, LoginActivity.class);
                    startActivity(intent);
                }else if(btnLogin.getText().toString().equals(name)){
                    Intent intent=new Intent(activity, MyAccountActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.btn_login://登录按钮
                if (btnLogin.getText().toString().equals("立刻登录")){
                    Intent intent1=new Intent(activity, LoginActivity.class);
                    startActivity(intent1);
                }else if(btnLogin.getText().toString().equals(name)){
                    Intent intent1=new Intent(activity, MyAccountActivity.class);
                    startActivity(intent1);
                }
                break;
            case R.id.iv_share_weixin:
                Toast.makeText(activity,"微信",Toast.LENGTH_SHORT).show();
                showShare(Wechat.NAME);
                break;
            case R.id.iv_share_qq:
                Toast.makeText(activity,"qq",Toast.LENGTH_SHORT).show();
                showShare(QQ.NAME);
                break;
            case R.id.iv_share_friends:
                Toast.makeText(activity,"朋友圈",Toast.LENGTH_SHORT).show();
                showShare(WechatMoments.NAME);
                break;
            case R.id.iv_share_weibo:
                Toast.makeText(activity,"微博",Toast.LENGTH_SHORT).show();
                showShare(SinaWeibo.NAME);
                break;
        }
    }
    OnekeyShare oks;
    private void showShare(String name) {
        ShareSDK.initSDK(activity);
        oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        //设置平台名称
        oks.setPlatform(name);
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(activity);
    }
}
