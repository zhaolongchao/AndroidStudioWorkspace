package com.zhuoxin.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by l on 2016/11/11.
 * 图片加载工具类
 * 1.异步任务加载图片
 * 2.图片缓存（内存，本地）
 */
public class ImageLoad {
    private Context context;//应用上下文对象
    //创建一个图片加载监听
    public interface ImageLoadListener{
        //当图片加载完成之后，将图片的链接地址和数据传递给使用者
        void imageLoadOK(String url,Bitmap bitmap);
    }
    //创建图片加载监听器对象
    private ImageLoadListener imageLoadListener;
    //在构造方法中初始化监听对象
    public ImageLoad(ImageLoadListener imageLoadListener,Context context){
        this.imageLoadListener=imageLoadListener;
        this.context=context;
    }
    //封装异步任务请求
    class ImageAsyncTask extends AsyncTask<String,Void,Bitmap>{
        String path="";
        //耗时操作
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap=null;
            path=strings[0];
            //联网获取图片数据
            try {
                URL url=new URL(path);
                HttpURLConnection con= (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setDoInput(true);
                if(con.getResponseCode()==200){
                    InputStream in=con.getInputStream();
                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                    byte[] b=new byte[1024];
                    int len=-1;
                    while ((len=in.read(b))!=-1){
                        baos.write(b,0,len);
                    }
                    byte[] bytes=baos.toByteArray();
                    bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    Log.e(path,bytes.length+"");
                    baos.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        //更新UI组件
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //采用回调模式传递数据
            imageLoadListener.imageLoadOK(path,bitmap);
            //保存到本地
            saveLoadBitmap(path,bitmap);
        }
    }
    //获取图片数据
    public Bitmap getBitmap(String path){
//        //内存缓存？？？？
//        Bitmap bitmap=getBitmapFromCache(path);
//        if(bitmap!=null){
//            return bitmap;
//        }
        //从本地缓存中获取
        Bitmap bitmap=getBitmapFromLocal(path);
        if(bitmap!=null){
            return bitmap;
        }
        //联网获取
        startAsyncTask(path);
        return bitmap;
    }
    //启动异步任务的方法
    public void startAsyncTask(String path){
        ImageAsyncTask task=new ImageAsyncTask();
        //启动异步任务
        task.execute(path);
    }

    /**
     * 本地图片缓存
     * @param url  图片链接地址
     * @param bitmap  图片数据
     */
    public void saveLoadBitmap(String url,Bitmap bitmap){
        String fileName=url.substring(url.lastIndexOf("/")+1);
        //获取本地缓存路径  data/data/包名/cache
        File file=context.getCacheDir();
        file=new File(file,fileName);
        FileOutputStream fos=null;
        try {
            fos=new FileOutputStream(file);
            //将一个bitmap对象保存到文件中
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本地保存的图片
     * @param url
     * @return
     */
    public Bitmap getBitmapFromLocal(String url){
        Bitmap bitmap=null;
        String fileName=url.substring(url.lastIndexOf("/")+1);
        File file=context.getCacheDir();
        File[] files=file.listFiles();
        for (File f:files) {
            if(f.getName().equals(fileName)){//判断是否有这个文件
                file=new File(file,fileName);
                //转成bitmap对象
                bitmap=BitmapFactory.decodeFile(file.getAbsolutePath());
            }
        }
        return bitmap;
    }
    // 获取手机内存大小
    long maxRAM=Runtime.getRuntime().maxMemory();
    //创建LruCache缓存对象
    LruCache<String,Bitmap> cache=new LruCache<String,Bitmap>((int)(maxRAM/8)){
        //返回图片的大小
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes()*value.getHeight();
        }
    };
    //保存到缓存中
    public void saveCache(String url,Bitmap bitmap){
        cache.put(url,bitmap);
    }
    //从缓存中获取
    public Bitmap getBitmapFromCache(String url){
        Bitmap bitmap=cache.get(url);
        return bitmap;
    }




}
