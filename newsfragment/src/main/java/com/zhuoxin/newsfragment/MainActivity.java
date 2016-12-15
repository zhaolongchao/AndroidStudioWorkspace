package com.zhuoxin.newsfragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 *如果当前的activity需要关联fragment，
 * activity要继承FragmentActivity--android.support.v4.app
 */
public class MainActivity extends FragmentActivity {
    //创建一个fragment对象
    Fragment1 fragment1;
    private FragmentManager fm;
    private FragmentTransaction ft;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv= (TextView) findViewById(R.id.tv);
        String msg=tv.getText().toString();
        Log.e("Activity","onCreate");
        fragment1=new Fragment1();
        //将activity中的数据传递到fragment中
        Bundle bundle=new Bundle();
        bundle.putString("msg",msg);
        //使用setArguments绑定数据
        fragment1.setArguments(bundle);
        //1.获取fragment管理对象
        fm=getSupportFragmentManager();
        //2.获取事务对象
        ft=fm.beginTransaction();
        /*
        3.添加fragment对象
        参数1为一个布局的资源id--activity中将要放置fragment位置的布局
        参数2 fragment对象
        参数3  每一个fragment的标签
         */
        ft.add(R.id.ll,fragment1,"frag");
        //4.提交
        ft.commit();
    }
    //封装一个添加fragment2的方法
    public void addFragment2(){
        //重新获取事务
        ft=fm.beginTransaction();
        Fragment2 fragment2=new Fragment2();
        //如果原来的布局id上有fragment需要替换
        ft.replace(R.id.ll,fragment2,"frag2");
        //提交
        ft.commit();
    }

    @Override
    protected void onStart() {
        Log.e("Activity","onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.e("Activity","onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e("Activity","onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.e("Activity","onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.e("Activity","onResume");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        Log.e("Activity","onRestart");
        super.onRestart();
    }
}
