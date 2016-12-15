package com.zhuoxin.wtest1121;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    Button btn1,btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1= (Button) findViewById(R.id.btn1);
        btn2= (Button) findViewById(R.id.btn2);
    }
    public void onClick(View v){
        //动画开始的位置---控件左上角的位置
        final int[] startPosition=new int[2];
        switch (v.getId()){
            case R.id.btn1:
                //获取当前控件在屏幕中的位置
                btn1.getLocationInWindow(startPosition);
                //获取点击位置的数据
                //String msg1=btn1.getText().toString();
                //创建动画层
                final ViewGroup group1=getViewGroup();
                final ImageView iv1=setAnimView(getCacheView(btn1),group1);
                //设置延迟获取动画的终点坐标
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int[] endPosition=new int[2];
                        btn2.getLocationInWindow(endPosition);
                        //创建动画
                        startAnim(startPosition,endPosition,iv1,btn1,group1);
                    }
                },100);
                break;
            case R.id.btn2:
                //获取当前控件在屏幕中的位置
                btn2.getLocationInWindow(startPosition);
                //获取点击位置的数据
                //String msg2=btn1.getText().toString();
                //创建动画层
                final ViewGroup group2=getViewGroup();
                final ImageView iv2=setAnimView(getCacheView(btn2),group2);
                //设置延迟获取动画的终点坐标
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int[] endPosition=new int[2];
                        btn1.getLocationInWindow(endPosition);
                        //创建动画
                        startAnim(startPosition,endPosition,iv2,btn2,group2);
                    }
                },100);
                break;
        }
    }

    private void startAnim(int[] startPosition,
                           int[] endPosition,
                           final ImageView imageView,
                           final Button btn,
                           final ViewGroup group) {
        //创建平移动画
        TranslateAnimation ta=new TranslateAnimation(
                    startPosition[0],endPosition[0],
                    startPosition[1],endPosition[1]);
        ta.setFillAfter(true);
        ta.setDuration(500);
        //启动动画
        imageView.startAnimation(ta);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override//动画结束之后更新界面
            public void onAnimationEnd(Animation animation) {
                String msg1=btn1.getText().toString();
                String msg2=btn2.getText().toString();
                btn1.setText(msg2);
                btn2.setText(msg1);
                group.removeView(imageView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



    }

    //创建动画层
    private ViewGroup getViewGroup() {
        ViewGroup group= (ViewGroup) getWindow().getDecorView();
        //创建一个布局容器---线性布局
        LinearLayout ll=new LinearLayout(this);
        //设置宽高
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(params);
        //将线性布局添加到根视图中
        group.addView(ll);
        return ll;
    }
    /**
     * 从缓存中获取视图
     * @param btn
     * @return
     */
    private ImageView getCacheView(Button btn) {
        //销毁之前的缓存
        btn.destroyDrawingCache();
        //开启缓存
        btn.setDrawingCacheEnabled(true);
        //获取缓存对象
        Bitmap bitmap=Bitmap.createBitmap(btn.getDrawingCache());
        //关闭缓存
        btn.setDrawingCacheEnabled(false);
        //创建imageview
        ImageView iv=new ImageView(this);
        //设置bitmap
        iv.setImageBitmap(bitmap);
        return iv;
    }
    /**
     * 将缓存视图控件添加到动画层容器中
     */
    private ImageView setAnimView(ImageView iv,ViewGroup group){
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin=0;//左外边距
        params.topMargin=0;//上外边距
        iv.setLayoutParams(params);
        //添加到容器中
        group.addView(iv);
        return iv;
    }

}
