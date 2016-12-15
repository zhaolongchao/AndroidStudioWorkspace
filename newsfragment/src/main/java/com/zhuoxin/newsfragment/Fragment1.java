package com.zhuoxin.newsfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by l on 2016/11/14.
 * 创建一个fragment
 * 重写onCreateView方法--加载布局实例化控件
 */

public class Fragment1 extends Fragment implements View.OnClickListener {
    /**
     * 给加载布局
     * @param inflater  --布局填充器
     * @param container  --当前fragment的父容器
     * @param savedInstanceState  --在fragment创建的时候绑定的数据
     * @return
     */
    MainActivity activity;//fragment的关联的activity
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.e("fragment1","onCreateView");
        //加载布局
        View v=inflater.inflate(R.layout.fragment_layout,null);
        TextView tv= (TextView) v.findViewById(R.id.tv1);
        //获取传递过来的数据
        Bundle bundle=getArguments();
        String msg=bundle.getString("msg");
        tv.setText(msg);
        ImageView iv= (ImageView) v.findViewById(R.id.iv1);
        iv.setOnClickListener(this);
        //初始化fragment关联的activity对象
        activity= (MainActivity) getActivity();
        return v;

    }
    @Override
    public void onClick(View view) {
        //activity切换fragment
        activity.addFragment2();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("fragment1","onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("fragment1","onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("fragment1","onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("fragment1","onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("fragment1","onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("fragment1","onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("fragment1","onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("fragment1","onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("fragment1","onDetach");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("fragment1","onActivityCreated");
    }



}
