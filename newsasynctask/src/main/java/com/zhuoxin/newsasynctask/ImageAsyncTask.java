package com.zhuoxin.newsasynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by l on 2016/11/10.
 * 异步任务类的使用
 * 1.定义一个类继承AsyncTask
 * 2.声明三个泛型
 *   Params 参数类型
 *   Progress 进度值类型
 *   Result  异步任务返回数据类型
 *  3.重写方法
 *   1)  doInBackground  工作于子线程
 *   2) 工作在UI线程
 *     onPreExecute  在doInBackground方法之前调用
 *      onProgressUpdate  与doInBackground 方法交互调用
 *       onPostExecute  在doInBackground方法执行完毕之后调用
 *  4.方便于更新UI组件将控件传递过来
 */

public class ImageAsyncTask extends AsyncTask<String,Integer,Bitmap> {
    private final ImageView iv;
    private final ProgressBar pb;
    private final Context context;
    private ProgressDialog dialog;
    private String path;
    public ImageAsyncTask( Context context,ImageView iv, ProgressBar pb) {
        this.iv = iv;
        this.pb = pb;
        this.context = context;
    }
    /**
     * 工作在后台线程的
     * 用于处理耗时操作
     * @param strings  异步任务参数类型
     * @return  返回耗时操作访问的数据类型
     */
    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap=null;
        path=strings[0];
        //创建地址对象
        URL url=null;
        try {
            url=new URL(path);
            //获取连接对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置连接参数
            connection.setDoInput(true);
            //设置连接超时间
            connection.setConnectTimeout(5000);
            //设置请求方式
            connection.setRequestMethod("GET");
            //判断服务器返回状态
            if(connection.getResponseCode()==200){
                //获取输入流
                InputStream in  = connection.getInputStream();
                int max =connection.getContentLength();
                byte[] bytes = new byte[1024];
                int len = -1;
                int i = 0;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while((len=in.read(bytes))!=-1){
                    i++;
                    baos.write(bytes,0,len);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    publishProgress(i*len,max);
                }
                bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(),0,
                        baos.toByteArray().length);

                baos.close();
            }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

    /**
     * 工作与主线程  doInBackground 之前调用
     */
    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setTitle("正在加载");
        dialog.setMessage("新闻数据给力的加载中...");
        dialog.setIcon(R.mipmap.ic_launcher);
        //设置进度条的风格
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.show();
        pb.setMax(100);
    }
    //更新进度值 当在doInBackground中调用 publishProgress 主线程调用此方法
    @Override
    protected void onProgressUpdate(Integer... values) {
        pb.setProgress(values[0]);
        pb.setMax(values[1]);
    }
    /*
   在doInBackground 执行完毕之后调用onPostExecute方法
   将返回的数据传入此参数
   更新UI组件
    */
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        dialog.dismiss();
        pb.setVisibility(View.INVISIBLE);
        saveCache(path,bitmap);
        iv.setImageBitmap(bitmap);
    }
    //获取手机内存大小
    static long maxRAM=Runtime.getRuntime().maxMemory();
    //创建LruCache缓存对象
    static LruCache<String,Bitmap> cache=new LruCache<String,Bitmap>((int)(maxRAM/8)){
        //返回图片的大小
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes()*value.getHeight();
        }
    };
    //保存到缓存中
    public static void saveCache(String url,Bitmap bitmap){
        cache.put(url,bitmap);
    }
    //从缓存中获取
    public static Bitmap getBitmapFromCache(String url){
        Bitmap bitmap=cache.get(url);
        return bitmap;
    }







}
