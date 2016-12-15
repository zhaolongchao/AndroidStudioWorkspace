package com.zhuoxin.fragment;

import android.animation.AnimatorSet;
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

public class FragmentPager2 extends FragmentBasePager {
    private ImageView ivBicker;
    private ImageView ivBlimp;
    private ImageView ivBrightness;
    private ImageView ivBickwheel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity2,null);
        assignViews(v);//实例化控件
        return v;
    }

    //实例化控件
    private void assignViews(View v) {
        ivBicker = (ImageView) v.findViewById(R.id.iv_bicker);
        ivBlimp = (ImageView) v.findViewById(R.id.iv_blimp);
        ivBrightness = (ImageView) v.findViewById(R.id.iv_brightness);
        ivBickwheel = (ImageView) v.findViewById(R.id.iv_bickwheel);
    }

    @Override
    public void onResume() {
        super.onResume();
        objectAnimator();
    }

    private void objectAnimator() {
        ObjectAnimator rotationBicker=ObjectAnimator.ofFloat(ivBicker,"rotation",0,360);
        ivBicker.setPivotX(0);
        ivBicker.setPivotY(0);
        ObjectAnimator taBicker=ObjectAnimator.ofFloat(ivBicker,"translationX",0,150f);
        AnimatorSet as1=new AnimatorSet();
        as1.playTogether(rotationBicker,taBicker);

        ObjectAnimator rotationBlimp=ObjectAnimator.ofFloat(ivBlimp,"rotation",0,360);
        ivBlimp.setPivotX(0);
        ivBlimp.setPivotY(0);
        ObjectAnimator taBlimp=ObjectAnimator.ofFloat(ivBlimp,"translationX",0,-150f);
        AnimatorSet as2=new AnimatorSet();
        as2.playTogether(rotationBlimp,taBlimp);

        ObjectAnimator rotationBickwheel=ObjectAnimator.ofFloat(ivBickwheel,"rotation",0,360);
        ivBickwheel.setPivotX(0);
        ivBickwheel.setPivotY(0);
        ObjectAnimator taBickwheel=ObjectAnimator.ofFloat(ivBickwheel,"translationY",0,150f);
        AnimatorSet as3=new AnimatorSet();
        as3.playTogether(rotationBickwheel,taBickwheel);

        ObjectAnimator rotationBrightness=ObjectAnimator.ofFloat(ivBrightness,"rotation",0,360);
        ivBrightness.setPivotX(0);
        ivBrightness.setPivotY(0);
        ObjectAnimator taBrightness=ObjectAnimator.ofFloat(ivBrightness,"translationY",0,-150f);
        AnimatorSet as4=new AnimatorSet();
        as4.playTogether(rotationBrightness,taBrightness);

        AnimatorSet as=new AnimatorSet();
        as.setDuration(2000);
        as.playTogether(as1,as2,as3,as4);
        as.start();
    }

    @Override
    public void startAnim() {
        objectAnimator();
    }
}
