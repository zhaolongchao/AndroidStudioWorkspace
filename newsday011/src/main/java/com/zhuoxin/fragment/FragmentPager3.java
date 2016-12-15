package com.zhuoxin.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhuoxin.newsday01.R;

/**
 * Created by l on 2016/11/20.
 */

public class FragmentPager3 extends FragmentBasePager {
    private ImageView ivBiaopan;
    private ImageView ivZhizhen;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity3,null);
        assignViews(v);//实例化控件
        return v;
    }

    //实例化控件
    private void assignViews(View v) {
        ivBiaopan = (ImageView) v.findViewById(R.id.iv_biaopan);
        ivZhizhen = (ImageView) v.findViewById(R.id.iv_zhizhen);
    }

    @Override
    public void onResume() {
        super.onResume();
       // objectAnimator();
    }

    //rotation     --旋转
    private void objectAnimator() {
        ObjectAnimator rotation=ObjectAnimator.ofFloat(ivZhizhen,"rotation",-135,135);
        rotation.setDuration(2000);
        rotation.start();
    }

    @Override
    public void startAnim() {
        objectAnimator();
    }
}
