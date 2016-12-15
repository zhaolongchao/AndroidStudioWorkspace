package com.zhuoxin.newsrefresh;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    SwipeRefreshLayout srl;
    List<String> list=new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv= (ListView) findViewById(R.id.lv);
        srl= (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        //设置下拉刷新视图的颜色
        srl.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        initData("原数据");
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,list);
        lv.setAdapter(adapter);
        //设置下拉刷新监听
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    //当触摸下拉刷新的时候会调用此方法
                    @Override
                    public void run() {
                        //lv.setSelection(lv.getBottom());
                        initData("刷新数据");
                        adapter.notifyDataSetChanged();
                        //设置取消下拉刷新
                        srl.setRefreshing(false);
                    }
                },3000);
            }
        });
    }

    private void initData(String str) {
        for (int i = 0; i < 4; i++) {
            list.add(str+i);
        }
    }
    public void onClick(View v){
        Intent intent=new Intent(this,PullToRefreshListActivity.class);
        startActivity(intent);
    }
}
