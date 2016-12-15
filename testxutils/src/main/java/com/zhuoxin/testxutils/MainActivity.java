package com.zhuoxin.testxutils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

public class MainActivity extends AppCompatActivity {
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv= (ImageView) findViewById(R.id.iv);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn1:
                //httpGet();
                //httpPost();
                break;
            case R.id.btn2://请求图片
                String url="http://192.168.0.115:8081/networkService/Image/welcome.jpg";
                ImageOptions options=new ImageOptions.Builder()
                        .setSize(300,300)//设置大小
                        .setCrop(true)//设置是否有圆角
                        .setLoadingDrawableId(R.mipmap.loading_01)
                        .setRadius(300)//设置圆的半径
                        .build();
                x.image().bind(iv,url,options);//获取网络图片
                break;
        }

    }

    private void httpPost() {
        String url="http://192.168.0.115:8081/networkService/LoginServlet";
        //?name=mary&password=1234
        RequestParams params=new RequestParams(url);
        //设置请求参数
        params.addParameter("name","admin");
        params.addParameter("password","admin");
        //发送请求
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String s) {
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                Toast.makeText(MainActivity.this,"请求失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    //get请求
    private void httpGet() {
        //创建uri地址
        String url="http://www.baidu.com";
        //创建请求对象
        RequestParams params=new RequestParams(url);
        //发送请求
        x.http().get(params, new Callback.CommonCallback<String>() {
            //请求成功的回调方法
            @Override
            public void onSuccess(String s) {
                Log.e("aaa",s);
            }
            //请求失败的回调
            @Override
            public void onError(Throwable throwable, boolean b) {

            }
            //取消请求的回调方法
            @Override
            public void onCancelled(CancelledException e) {

            }
            //请求完成
            @Override
            public void onFinished() {

            }
        });
    }
}
