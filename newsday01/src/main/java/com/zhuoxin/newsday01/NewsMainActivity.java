package com.zhuoxin.newsday01;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.zhuoxin.adapter.NewsAdapter;
import com.zhuoxin.common.CommonUtils;
import com.zhuoxin.entity.News;
import com.zhuoxin.net.HttpConnUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;


public class NewsMainActivity extends Activity {
    //创建线程处理联网操作
    private Thread t;
    //json数据
    String newsData;
    ListView lv;
    List<News> list;//新闻数据实体
    private ProgressDialog dialog;//加载对话框
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_main);
        lv= (ListView) findViewById(R.id.lv_news);
        ansyLoadNetData();//联网获取json数据

    }
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            lv.setVisibility(View.VISIBLE);
           //更新UI组件
            NewsAdapter adapter=new NewsAdapter(list,NewsMainActivity.this,lv);
            lv.setAdapter(adapter);
        }
    };
    /**
     * 显示进度对话框
     */
    public void showProgressDialog(){
        dialog=new ProgressDialog(this);
        dialog.setTitle("正在加载...");
        dialog.setMessage("新闻数据给力的加载中...");
        dialog.setIcon(R.mipmap.a6);
        //设置进度条的风格
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.show();
    }
    /**
     * 联网获取新闻数据
     */
    private void ansyLoadNetData() {
        showProgressDialog();
        lv.setVisibility(View.INVISIBLE);
        t=new Thread(new Runnable() {
            @Override
            public void run() {
                //调用联网的业务方法
                try {
                    newsData=HttpConnUtils.useJavaNet(CommonUtils.path1);
                    //解析新闻数据
                    list=HttpConnUtils.getNewsList(newsData);
                    //通知更新UI组件
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
    protected void onDestroy() {//页面销毁的时候终止线程
        super.onDestroy();
        if(t!=null){
            t.interrupt();
            t=null;
        }
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:
                Toast.makeText(this,"暂不可用",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
