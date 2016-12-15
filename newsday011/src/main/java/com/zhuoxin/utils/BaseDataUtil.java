package com.zhuoxin.utils;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by l on 2016/11/30.
 * 封装将dip转换成px的方法
 */

public class BaseDataUtil {
    private Context context;//应用上下文对象
    public static float dip2px(Context context,float dipValue)
    {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return  (dipValue * scale + 0.5f);
    }
}
