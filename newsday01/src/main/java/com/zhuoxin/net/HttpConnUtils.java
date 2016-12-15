package com.zhuoxin.net;

import android.util.Log;

import com.zhuoxin.entity.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 2016/11/10.
 * 联网工具类
 */

public class HttpConnUtils {
    /**
     * 联网获取json数据
     * @param path
     * @return
     * @throws MalformedURLException
     */
    public static String useJavaNet(String path) throws IOException {
        String str="";
        //创建地址对象
        URL url=new URL(path);
        //获取连接对象
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        //设置连接参数
        connection.setDoInput(true);
        //设置联网超时时长
        connection.setConnectTimeout(5000);
        //设置请求方式
        connection.setRequestMethod("GET");
        //判断服务器返回状态
        if(connection.getResponseCode()==200){
            //获取输入流
            InputStream in=connection.getInputStream();
            byte[] bytes=new byte[1024];
            int len=-1;
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            while ((len=in.read(bytes))!=-1){
                baos.write(bytes,0,len);
            }
            str=new String(baos.toByteArray(),"utf-8");
            Log.e("数据",str);
            baos.close();
        }
        return str;
    }
    /*
    解析新闻数据
     */
    public static List<News> getNewsList(String json) throws JSONException {
        List<News> list=new ArrayList<News>();
        //将json封装成JSONObject对象
        JSONObject object=new JSONObject(json);
        String message=object.getString("message");
        if(message.equals("OK")){//判断返回数据正常再获取新闻数据
            JSONArray array=object.getJSONArray("data");
            for (int i = 0; i <array.length() ; i++) {
                JSONObject o=array.getJSONObject(i);
                News n=new News();
                n.setIcon(o.getString("icon"));
                n.setLink(o.getString("link"));
                n.setNid(o.getInt("nid"));
                n.setStamp(o.getString("stamp"));
                n.setSummary(o.getString("summary"));
                n.setTitle(o.getString("title"));
                n.setType(o.getInt("type"));
                list.add(n);
            }
        }
        return list;

    }
}
