package com.zhuoxin.newsday01;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by l on 2016/11/24.
 */

public class LogoActivity extends Activity {
    ImageView ivLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        ivLogo= (ImageView) findViewById(R.id.iv_logo);
        ObjectAnimator rotation=ObjectAnimator.ofFloat(ivLogo,"alpha",0f,1f);
        rotation.setDuration(1000);
        rotation.start();//启动动画
        //动画设置监听
        rotation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }
            //动画结束实现跳转到主界面
            @Override
            public void onAnimationEnd(Animator animator) {
                Intent intent=new Intent(LogoActivity.this,NewsHSVActivity.class);
                startActivity(intent);
                LogoActivity.this.finish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
}
