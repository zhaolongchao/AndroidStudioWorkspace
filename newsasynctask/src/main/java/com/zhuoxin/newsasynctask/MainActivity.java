package com.zhuoxin.newsasynctask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
    ImageView iv;
    ProgressBar pb;
    String path = "http://118.244.212.82:9092/Images/20160517113739.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);
        pb = (ProgressBar) findViewById(R.id.pb);
    }
    public void onClick(View view){
        iv.setImageResource(R.mipmap.ic_launcher);
        //启动异步任务架加载图片
        //从缓存中获取
        Bitmap bitmap=ImageAsyncTask.getBitmapFromCache(path);
        if(bitmap==null){
            ImageAsyncTask task = new ImageAsyncTask(this,iv,pb);
            //启动异步任务
            task.execute(path);
        }else{
            iv.setImageBitmap(bitmap);
        }



    }
}
