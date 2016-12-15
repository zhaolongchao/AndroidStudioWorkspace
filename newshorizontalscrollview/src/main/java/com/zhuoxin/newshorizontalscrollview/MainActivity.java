package com.zhuoxin.newshorizontalscrollview;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LinearLayout ll;//水平滚动条中的线性布局
    ViewPager vp;
    List<View> list= new ArrayList<View>();
    private int count;//线性布局中子控件的数量
    HorizontalScrollView hsv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ll= (LinearLayout) findViewById(R.id.ll);
        vp= (ViewPager) findViewById(R.id.vp);
        hsv= (HorizontalScrollView) findViewById(R.id.hsv);
        //获取线性布局中子控件的数量
        count=ll.getChildCount();
        //获取每一个textview控件，添加点击事件
        for (int i = 0; i <count ; i++) {
            //获取每一个子控件
            final TextView tv= (TextView) ll.getChildAt(i);
            tv.setBackgroundResource(R.drawable.tv_bg);
            tv.setClickable(true);//添加可点击属性
            if(i==0){//设置第一个为选中状态
                tv.setSelected(true);
            }
            tv.setOnClickListener(new View.OnClickListener() {//添加点击监听
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this,
                            tv.getText().toString(),
                            Toast.LENGTH_SHORT).show();
                    //在线性布局中查找与当前点击的控件相等时改变TextView的状态并切换ViewPager
                    for (int j = 0; j < count; j++) {
                        View v=ll.getChildAt(j);
                        if(v!=view){//背景选择
                            v.setSelected(false);
                        }else{
                            v.setSelected(true);
                            vp.setCurrentItem(j);//设置当前项目
                        }
                    }
                }
            });
            //vp添加监听
            vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    //遍历线性布局
                    for (int j = 0; j < list.size(); j++) {
                        View v=ll.getChildAt(j);
                        if(j==position){
                            //计算滚动条的滚动距离(将屏幕宽度的1/2与当前被选中控件的左外边距+
                            // 控件宽度的1/2作比较，如果大就滚动，如果小就不滚动，
                            // 滚动距离就是去差值)
                            int left=v.getLeft();//获取控件的左外边距
                            int width=v.getWidth();//控件的宽度
                            //屏幕的宽度
                            DisplayMetrics dm=new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(dm);
                            int screenWidth=dm.widthPixels;
                            int scrollx=left+width/2-screenWidth/2;
                            //设置滚动条滚动的距离
                            hsv.smoothScrollTo(scrollx,0);
                            v.setSelected(true);
                        }else{
                            v.setSelected(false);
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
        initvpView();
        MyAdapter adapter=new MyAdapter();//获取适配器对象
        vp.setAdapter(adapter);//适配

    }

    private void initvpView() {
        for (int i = 0; i < count; i++) {
            //注意：实际中使用的时候此布局都应该加载多次
            View v=getLayoutInflater().inflate(R.layout.vp_item,null);
            list.add(v);
        }
    }
    //构建内部类
    class MyAdapter extends PagerAdapter{
        int[] arr=new int[]{Color.RED,Color.GREEN};
        @Override
        public int getCount() {
            return list.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v=list.get(position);
            ImageView iv= (ImageView) v.findViewById(R.id.iv_vp);
            if(position%2==0){
                iv.setBackgroundColor(arr[0]);
            }else{
                iv.setBackgroundColor(arr[1]);
            }
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v=list.get(position);
            container.removeView(v);//销毁视图
        }
    }
}
