package com.zhuoxin.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhuoxin.NewsApplication;
import com.zhuoxin.adapter.NewsAdapter;
import com.zhuoxin.dao.UserFavoriteDAO;
import com.zhuoxin.entity.TodayNews;
import com.zhuoxin.entity.UserFavoriteItem;
import com.zhuoxin.net.HttpConnUtils;
import com.zhuoxin.newsday01.ActivityWebView;
import com.zhuoxin.newsday01.NewsHSVActivity;
import com.zhuoxin.newsday01.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by l on 2016/11/14.
 */

public class FragmentPager extends Fragment {
    PullToRefreshListView pullToRefreshListView;//下拉刷新控件
    ListView lv;
    NewsHSVActivity activity; //fragment的关联的activity
    //创建线程处理联网操作
    private Thread t;
    //json数据
    String newsData;
    String url;//原始链接
    String urlRefresh;//下拉刷新的链接
    String urlGet;//上拉加载的链接
    int count=0;//刷新次数
    List<TodayNews> list;//新闻数据实体
    private ProgressDialog dialog;//加载对话框
    /**
     * 加载布局
     * @param inflater  --布局填充器
     * @param container  --当前fragment的父容器
     * @param savedInstanceState  --在fragment创建的时候绑定的数据
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_view_pager,null);
        pullToRefreshListView= (PullToRefreshListView) v.findViewById(R.id.pull_refresh_list);
        lv = pullToRefreshListView.getRefreshableView();//获取第三方封装的listview
        //获取传递过来的数据
        Bundle bundle=getArguments();
        url=bundle.getString("url");
        //初始化fragment关联的activity对象
        activity= (NewsHSVActivity) getActivity();
        ansyLoadNetData(url);//联网获取json数据
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(activity, ActivityWebView.class);
                intent.putExtra("url",list.get(i-1).getUrl() );
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
        initView();//下拉刷新，上拉加载
        return v;
    }
    //创建浮动窗口
    private void initPopupWindow(View view, final int i) {
        View v=activity.getLayoutInflater().inflate(R.layout.inflate_my_favorite,null);
        Button btnFavorite= (Button) v.findViewById(R.id.btn_favorite);
        Button btnShare= (Button) v.findViewById(R.id.btn_share);
        Button btnCancel= (Button) v.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow=new PopupWindow(
                v,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();//浮动窗口消失
                if(NewsApplication.getUserItem()!=null){//判断用户是否登录
                    //判断用户是否收藏过此条新闻
                    boolean isFavorite=
                            UserFavoriteDAO.getUserFavoriteDAO().findNewsByFavoriteLink
                                    (NewsApplication.getUserItem().getUserId(),list.get(i).getUrl());
                    if(!isFavorite){//添加到数据库
                        Toast.makeText(activity,"收藏成功",Toast.LENGTH_SHORT).show();
                        UserFavoriteItem userFavoriteItem=new UserFavoriteItem();
                        userFavoriteItem.setFavoriteNewsTitle(list.get(i).getTitle());
                        userFavoriteItem.setFavoriteNewsSummary(list.get(i).getDigest());
                        userFavoriteItem.setFavoriteNewsPic(list.get(i).getImgsrc());
                        userFavoriteItem.setFavoriteNewsLink(list.get(i).getUrl());
                        userFavoriteItem.setFavoriteUserId(NewsApplication.getUserItem().getUserId());
                        UserFavoriteDAO.getUserFavoriteDAO().addUserFavoriteItem(userFavoriteItem);
                    }else{
                        Toast.makeText(activity,"已经收藏",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(activity,"请先登录再收藏",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();//浮动窗口消失
                Toast.makeText(activity,"分享成功",Toast.LENGTH_SHORT).show();
                showShare(i);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();//浮动窗口消失
                Toast.makeText(activity,"取消成功",Toast.LENGTH_SHORT).show();
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
        //设置显示位置
        popupWindow.showAsDropDown(view,0,0);
    }
    //第三方分享
    private void showShare(int i) {
        ShareSDK.initSDK(activity);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(list.get(i).getTitle());
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(list.get(i).getUrl());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(list.get(i).getDigest());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(list.get(i).getUrl());
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("这条新闻不错，可以看看！");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(activity);
    }

    //下拉刷新，上拉加载
    private void initView() {
        // 设置模式
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);// 同时支持上拉下拉
        //设置刷新监听
        pullToRefreshListView.setOnRefreshListener(
                new PullToRefreshBase.OnRefreshListener2<ListView>() {
            //下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                count++;
                urlRefresh=url.replaceFirst("0-",""+count*10+"-");
                ansyLoadNetData(urlRefresh);
            }
            //上拉加载
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                if (count>0){
                    count--;
                    urlGet=urlRefresh.replaceFirst("[0-9]+0-",""+count*10+"-");
                    ansyLoadNetData(urlGet);
                }else{
                    Toast.makeText(activity, "别整啦，这不是无底洞!", Toast.LENGTH_SHORT).show();
                    count=0;
                    pullToRefreshListView.onRefreshComplete();
                }
            }
        });
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            lv.setVisibility(View.VISIBLE);
            pullToRefreshListView.onRefreshComplete();//往上加数据的方法
            //更新UI组件
            NewsAdapter adapter=new NewsAdapter(list,activity,lv);
            lv.setAdapter(adapter);
        }
    };
    /**
     * 显示进度对话框
     */
    public void showProgressDialog(){
        dialog=new ProgressDialog(activity);
        dialog.setTitle("正在加载...");
        dialog.setMessage("新闻数据给力的加载中...");
        dialog.setIcon(R.mipmap.a6);
        //设置进度条的风格
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //dialog.setCancelable(false);
        dialog.show();
    }
    /**
     * 联网获取新闻数据
     */
    private void ansyLoadNetData(final String url) {
        showProgressDialog();
        lv.setVisibility(View.INVISIBLE);
        t=new Thread(new Runnable() {
            @Override
            public void run() {
                //调用联网的业务方法
                try {
                    newsData= HttpConnUtils.useJavaNet(url);
                    list=new ArrayList<TodayNews>();
                    //解析新闻数据
                    List<TodayNews> data=HttpConnUtils.getTodayNewsList(newsData,url);
                    list.addAll(0,data);
                    if(list!=null){
                        //发送消息
                        handler.sendEmptyMessage(200);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    @Override
    public void onDestroy() {//页面销毁的时候终止线程
        super.onDestroy();
        if(t!=null){
            t.interrupt();
            t=null;
        }
    }

}
