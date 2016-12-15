package com.zhuoxin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zhuoxin.NewsApplication;
import com.zhuoxin.newsday01.FavoriteActivity;
import com.zhuoxin.newsday01.NewsHSVActivity;
import com.zhuoxin.newsday01.R;

/**
 * Created by l on 2016/11/20.
 */

public class FragmentLeft extends Fragment implements View.OnClickListener {
    NewsHSVActivity activity;//fragment的关联的activity
    private RelativeLayout rlNews;
    private RelativeLayout rlFavorites;
    private RelativeLayout rlPictures;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.left_menu,null);
        activity= (NewsHSVActivity) getActivity();
        assignViews(v);//实例化控件
        initListener();//设置监听
        return v;
    }

    //实例化控件
    private void assignViews(View v) {
        rlNews = (RelativeLayout) v.findViewById(R.id.rl_news);
        rlFavorites = (RelativeLayout) v.findViewById(R.id.rl_favorites);
        rlPictures = (RelativeLayout) v.findViewById(R.id.rl_pictures);
    }
    //设置监听
    private void initListener() {
        rlNews.setOnClickListener(this);
        rlFavorites.setOnClickListener(this);
        rlPictures.setOnClickListener(this);
    }
    //单项点击事件
    public void onClick(View v){
        switch (v.getId()){
            case R.id.rl_news://新闻
                Toast.makeText(activity,"新闻",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(activity,NewsHSVActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_favorites://收藏
                if(NewsApplication.getUserItem()!=null){
                    if(NewsApplication.getUserItem().getUserId()!=-1){
                        Toast.makeText(activity,"收藏",Toast.LENGTH_SHORT).show();
                        Intent intent1=new Intent(activity, FavoriteActivity.class);
                        startActivity(intent1);
                    }
                }else{
                    Toast.makeText(activity,"请登录查看",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_pictures://图片
                Toast.makeText(activity,"暂无图片",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
