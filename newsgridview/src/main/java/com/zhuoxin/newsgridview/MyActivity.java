package com.zhuoxin.newsgridview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

/**
 * Created by l on 2016/11/17.
 */

public class MyActivity extends Activity {
    GridView gridView;
    String[] str={"张志亮","他妹的","看啥嘞","骄傲的","傻傻的",
            "飞机上","解放军","等谁呢"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);
        gridView= (GridView) findViewById(R.id.gv1);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>
                    (this,R.layout.gv_item,R.id.tv_item,str);
        gridView.setAdapter(adapter);

    }
}
