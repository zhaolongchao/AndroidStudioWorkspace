package com.zhuoxin.base;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoxin.newsday01.R;

/**
 * Created by l on 2016/11/17.
 */

public class BaseActivity extends Activity {
    /**
     * 设置导航条的显示数据
     * @param left
     * @param str
     * @param right
     */
    public void setTitleBar(int left,int str,int right){
        TextView tv= (TextView) findViewById(R.id.tv_titlebar);
        ImageView ivLeft = (ImageView) findViewById(R.id.iv_left);
        ImageView ivRight = (ImageView) findViewById(R.id.iv_right);
        tv.setText(str);
        ivLeft.setImageResource(left);
        if(right!=0){
            ivRight.setImageResource(right);
        }

    }
}
