package com.zhuoxin.newsslidingmenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;



public class MainActivity extends Activity implements View.OnClickListener {
    Button btn,btn1;
    SlidingMenu slidingMenu;
    private void initView() {
        btn= (Button) findViewById(R.id.btn);
        btn1= (Button) findViewById(R.id.btn1);
        btn.setOnClickListener(this);
        btn1.setOnClickListener(this);
        slidingMenu=new SlidingMenu(this);
        //设置为左右两边菜单栏
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        //设置全屏范围都可以打开菜单栏
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置菜单栏的宽度
        slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu);
        //设置菜单栏与类的关联：当前类显示的为菜单栏的中间界面
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //设置左菜单栏样式
        slidingMenu.setMenu(R.layout.left_menu);
        //设置右菜单栏样式
        slidingMenu.setSecondaryMenu(R.layout.right_menu);
        //SlidingMenu滑动时的渐变程度
        slidingMenu.setFadeDegree(0.3f);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化数据
        initView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn:
                slidingMenu.showMenu();//显示左边菜单栏
                break;
            case R.id.btn1:
                slidingMenu.showSecondaryMenu();//显示右边菜单栏
                break;
        }

    }
}
