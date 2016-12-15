package com.zhuoxin.newsday01;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhuoxin.base.BaseActivity;

/**
 * Created by l on 2016/11/13.
 */
public class ActivityWebView extends BaseActivity {
    WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        wv= (WebView) findViewById(R.id.wv);
        setTitleBar(R.mipmap.btn_homeasup_default,R.string.title_name,0);
        Intent intent=getIntent();
        final String url=intent.getStringExtra("url");
        //设置一个监听,在本应用程序打开网页（不会调用第三方的浏览器）
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true;//必须返回true
            }
        });
        //调用加载网页
        wv.loadUrl(url);
        //设置支持JavaScript
        wv.getSettings().setJavaScriptEnabled(true);
    }
    /*
    键盘按下事件的监听
    1.键值
    2.键盘的事件对象
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){//返回键
            //判断wv是否还能继续返回
            if(wv.canGoBack()){
                wv.goBack();//返回到上一页和返回到网页最上方
            }else {
                finish();
            }
        }
        return true;
    }

    public void onClick(View view){
        finish();
    }

}
