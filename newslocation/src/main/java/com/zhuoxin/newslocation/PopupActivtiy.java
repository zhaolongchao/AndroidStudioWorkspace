package com.zhuoxin.newslocation;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by l on 2016/11/23.
 */

public class PopupActivtiy extends Activity implements View.OnClickListener {
    TextView tv;
    RelativeLayout rl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv= (TextView) findViewById(R.id.tv);
        rl= (RelativeLayout) findViewById(R.id.rl);
        tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        /*
        创建浮动窗口
        1.加载布局
        2.创建浮动窗口对象
        3.设置相关参数
        4.设置显示位置
         */
        View v=getLayoutInflater().inflate(R.layout.inflate_popupwindow,null);
        Button btn= (Button) v.findViewById(R.id.btn1);
        final PopupWindow popupWindow=new PopupWindow(
                        v,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();//浮动窗口消失
            }
        });
        //设置焦点
        popupWindow.setFocusable(true);
        //设置点击边界外使浮动窗口消失
        popupWindow.setOutsideTouchable(true);
        //设置背景 -- 为了在点击浮动窗口以外的区域使浮动窗口消失
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        //设置动画
        popupWindow.setAnimationStyle(R.style.popupwindowAnimation);
        //设置显示位置
        popupWindow.showAsDropDown(tv,tv.getWidth(),0);
        //popupWindow.showAtLocation(rl, Gravity.BOTTOM,0,0);

    }
}
