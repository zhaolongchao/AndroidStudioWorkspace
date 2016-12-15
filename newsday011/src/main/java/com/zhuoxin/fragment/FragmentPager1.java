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

public class FragmentPager1 extends FragmentBasePager {
    private ImageView ivArt;
    private ImageView ivBag;
    private ImageView ivBook;
    private ImageView ivFashion;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity1,null);
        assignViews(v);//实例化控件
        return v;
    }

    //实例化控件
    private void assignViews(View v) {
        ivArt = (ImageView) v.findViewById(R.id.iv_art);
        ivBag = (ImageView) v.findViewById(R.id.iv_bag);
        ivBook = (ImageView) v.findViewById(R.id.iv_book);
        ivFashion = (ImageView) v.findViewById(R.id.iv_fashion);
    }

    @Override
    public void onResume() {
        super.onResume();
        //属性动画ObjectAnimator实现将控件本身的属性跟随动画结果变化
        objectAnimator();
    }

    //属性动画ObjectAnimator
    private void objectAnimator() {
        /*给属性设置数据，可以选择int，float，动画取值为float
		 * 1.translationX --平移x
		 * 2.translationY --平移y
		 * 3.rotation     --旋转
		 * 4.rotationX    --旋转x
		 * 5.rotationY    ---旋转y
		 * 6.alpha        透明度
		 * 7.scaleX       --伸缩x
		 * 8.scaleY       --伸缩y
		 */
        //渐变
        ObjectAnimator alphaArt=ObjectAnimator.ofFloat(ivArt,"alpha",0f,1f);
        ObjectAnimator alphaBag=ObjectAnimator.ofFloat(ivBag,"alpha",0f,1f);
        ObjectAnimator alphaBook=ObjectAnimator.ofFloat(ivBook,"alpha",0f,1f);
        ObjectAnimator alphaFashion=ObjectAnimator.ofFloat(ivFashion,"alpha",0f,1f);
        //平移X
        ObjectAnimator translationXArt=ObjectAnimator.ofFloat(ivArt,"translationX",0,350f);
        ObjectAnimator translationXBag=ObjectAnimator.ofFloat(ivBag,"translationX",0,-350f);
        ObjectAnimator translationXBook=ObjectAnimator.ofFloat(ivBook,"translationX",0,350f);
        ObjectAnimator translationXFashion=ObjectAnimator.ofFloat(ivFashion,"translationX",0,-350f);
        //平移Y
        ObjectAnimator translationYArt=ObjectAnimator.ofFloat(ivArt,"translationY",0,600f);
        ObjectAnimator translationYBag=ObjectAnimator.ofFloat(ivBag,"translationY",0,600f);
        ObjectAnimator translationYBook=ObjectAnimator.ofFloat(ivBook,"translationY",0,-600f);
        ObjectAnimator translationYFashion=ObjectAnimator.ofFloat(ivFashion,"translationY",0,-600f);
        AnimatorSet as=new AnimatorSet();
        //设置同时启动(设置顺序)
        as.playTogether(alphaArt,alphaBag,alphaBook,alphaFashion,
                translationXArt,translationXBag,translationXBook,translationXFashion,
                translationYArt,translationYBag,translationYBook,translationYFashion);
        as.setDuration(2000);//设置动画时长
        as.start();//启动动画
    }

    @Override
    public void startAnim() {
        objectAnimator();
    }
}
