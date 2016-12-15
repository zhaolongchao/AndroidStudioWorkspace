package com.zhuoxin.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhuoxin.newsday01.LogoActivity;
import com.zhuoxin.newsday01.R;
import com.zhuoxin.newsday01.StartPageActivity;

/**
 * Created by l on 2016/11/20.
 */

public class FragmentPager4 extends FragmentBasePager {
    private ImageView ivQixi;
    private ImageView ivNan;
    private ImageView ivNv;
    StartPageActivity activity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity4,null);
        activity= (StartPageActivity) getActivity();
        assignViews(v);//实例化控件
        return v;
    }

    //实例化控件
    private void assignViews(View v) {
        ivQixi = (ImageView) v.findViewById(R.id.iv_qixi);
        ivNan = (ImageView) v.findViewById(R.id.iv_nan);
        ivNv = (ImageView) v.findViewById(R.id.iv_nv);
        ivQixi.setAlpha(0.3f);
        ivQixi.setScaleX(0.5f);
        ivQixi.setScaleY(0.5f);
    }

    @Override
    public void onResume() {
        super.onResume();
        //objectAnimator();
    }

    private void objectAnimator() {
        ObjectAnimator translationNan=ObjectAnimator.ofFloat(ivNan,"translationX",0,275f);
        ObjectAnimator translationNv=ObjectAnimator.ofFloat(ivNv,"translationX",0,-275f);
        ObjectAnimator scaleXQiXi=ObjectAnimator.ofFloat(ivQixi,"scaleX",0.5f,2f);
        ObjectAnimator scaleYQiXi=ObjectAnimator.ofFloat(ivQixi,"scaleY",0.5f,2f);
        ObjectAnimator alphaQiXi=ObjectAnimator.ofFloat(ivQixi,"alpha",0.3f,1f);
        AnimatorSet as1=new AnimatorSet();
        as1.playTogether(translationNan,translationNv);
        AnimatorSet as2=new AnimatorSet();
        as2.playTogether(scaleXQiXi,scaleYQiXi,alphaQiXi);
        AnimatorSet as=new AnimatorSet();
        //按照一定顺序执行--给定参数的顺序执行
        as.playSequentially(as1,as2);
        as.setDuration(2000);
        as.start();
        as.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Intent intent=new Intent(activity, LogoActivity.class);
                startActivity(intent);
                activity.finish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    @Override
    public void startAnim() {
        objectAnimator();
    }
}
