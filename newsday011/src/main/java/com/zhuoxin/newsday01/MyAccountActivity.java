package com.zhuoxin.newsday01;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuoxin.NewsApplication;
import com.zhuoxin.base.BaseActivity;
import com.zhuoxin.dao.UserDAO;
import com.zhuoxin.entity.UserItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by l on 2016/11/24.
 */

public class MyAccountActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivAccount;
    private TextView tvNameAccount;
    private TextView tvMsgAccount;
    private Button btnExit;
    LinearLayout llAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        //设置导航条
        setTitleBar(R.mipmap.btn_homeasup_default, R.string.my_account, 0);
        assignViews();//实例化控件
        initData();//获取时间，地点和登录方式
        UserItem userItem= NewsApplication.getUserItem();
        if(userItem==null){
            tvNameAccount.setText("昵称");
        }else {
            String name= UserDAO.getUserDAO().getUsers(userItem.getUserEmail()).getUserName();
            tvNameAccount.setText(name);
        }
        if(NewsApplication.getBitmap()!=null){
            ivAccount.setImageBitmap(NewsApplication.getBitmap());
        }
        //注册监听
        btnExit.setOnClickListener(this);
        ivAccount.setOnClickListener(this);
    }

    //实例化控件
    private void assignViews() {
        ivAccount = (ImageView) findViewById(R.id.iv_account);
        tvNameAccount = (TextView) findViewById(R.id.tv_name_account);
        tvMsgAccount = (TextView) findViewById(R.id.tv_msg_account);
        btnExit = (Button) findViewById(R.id.btn_exit);
        llAccount = (LinearLayout) findViewById(R.id.ll_account);
    }

    //获取时间，地点和登录方式
    LocationManager locationManager;//获取定位服务
    String time;//当前系统时间
    private void initData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        time = sdf.format(date);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    private Location loc;
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission
                        (this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //获取定位数据
        //Location location=locationManager.getLastKnownLocation(provider);
        loc=null;
        if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) {
            locationManager.requestLocationUpdates
                    (LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        }else if (locationManager.getProvider(LocationManager.GPS_PROVIDER) != null){
            locationManager.requestLocationUpdates
                    (LocationManager.GPS_PROVIDER, 0, 0, listener);
        } else {
            Toast.makeText(this, "无法定位", Toast.LENGTH_SHORT).show();
        }
    }
    double lat;//纬度
    double lng;//经度
    String address;//地址
    private LocationListener listener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            loc=location;
            lat=loc.getLatitude();//纬度
            lng=loc.getLongitude();//经度
            //Geocoder--地理编码对象
            try {
                List<Address> list=new Geocoder(MyAccountActivity.this).getFromLocation(lat,lng,5);
                address=list.get(0).getLocality();//获取市
                tvMsgAccount.setText(time+"  "+address+"  移动端");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_left://返回键
//                Intent intent=new Intent(this,NewsHSVActivity.class);
//                startActivity(intent);
                finish();
                break;
            case R.id.btn_exit://退出登录
                NewsApplication.setUserItem(null);
                NewsApplication.setBitmap(null);
//                Intent intent1=new Intent(this,NewsHSVActivity.class);
//                startActivity(intent1);
                finish();
                break;
            case R.id.iv_account://头像图片
                 /*
                创建浮动窗口
                1.加载布局
                2.创建浮动窗口对象
                3.设置相关参数
                4.设置显示位置
                */
                View v=getLayoutInflater().inflate(R.layout.inflate_my_account,null);
                TextView tvCamera= (TextView) v.findViewById(R.id.tv_camera);
                TextView tvPictures= (TextView) v.findViewById(R.id.tv_pictures);
                final PopupWindow popupWindow=new PopupWindow(
                                        v,
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                tvCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();//浮动窗口消失
                        takePhoto();//拍照
                    }
                });
                tvPictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();//浮动窗口消失
                        selectPhoto();//从相册选择
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
                //popupWindow.showAsDropDown(tv,tv.getWidth(),0);
                popupWindow.showAtLocation(llAccount, Gravity.BOTTOM,0,0);
                break;
        }
    }

    //跳转到系统的拍照功能
    protected void takePhoto() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,100);
    }
    //跳转到系统相册，选择照片
    protected void selectPhoto() {
        final Intent intent=getPhotoPickIntent();
        startActivityForResult(intent,200);
    }
    //封装请求Gallery的intent
    public static Intent getPhotoPickIntent() {
        Intent intent=new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return intent;
    }
    Bitmap bitmap;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            if(resultCode== Activity.RESULT_OK){
                Bundle bundle=data.getExtras();
                bitmap= (Bitmap) bundle.get("data");
                save(bitmap);//缓存用户选择的图片
            }
        }else if(requestCode==200){
            if(resultCode==Activity.RESULT_OK){
                Uri selectedImage=data.getData();
                String[] filePathColumn={MediaStore.Images.Media.DATA};
                Cursor cursor=getContentResolver()
                            .query(selectedImage,filePathColumn,null,null,null);
                cursor.moveToFirst();
                int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
                String picturePath=cursor.getString(columnIndex);
                cursor.close();
                bitmap= BitmapFactory.decodeFile(picturePath);
                //System.out.println(bitmap);
                save(bitmap);//缓存用户选择的图片
            }
        }
    }
    private File file;
    private Bitmap alterBitmap;
    //缓存用户选择的图片
    private void save(Bitmap bitmap) {
        if(bitmap==null){
            return;
        }
        roundPic();
        File dir=new File(Environment.getExternalStorageDirectory(),"azynews");
        dir.mkdirs();
        file=new File(dir,"userpic.jpg");
        try {
            OutputStream stream=new FileOutputStream(file);
            alterBitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    //裁剪图片
    private void roundPic() {
        //目标图片bitmap
        Bitmap backBp=BitmapFactory.decodeResource(getResources(),R.mipmap.userbg);
        //最终效果的bitmap对象
        alterBitmap=Bitmap.createBitmap(
                backBp.getWidth(),backBp.getHeight(),backBp.getConfig());
        Canvas canvas=new Canvas(alterBitmap);//画布使用bitmap
        Paint paint=new Paint();//创建画笔
        paint.setAntiAlias(true);//设置去掉锯齿
        canvas.drawBitmap(backBp,new Matrix(),paint);//画圆形图片--目标图片
        //设置叠加模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //获取原图片（照相或者从相册拿到图片）
        bitmap=Bitmap.createScaledBitmap(bitmap,backBp.getWidth(),backBp.getHeight(),true);
        //画第二张图片，最终图片效果就是alterbitmap
        canvas.drawBitmap(bitmap,new Matrix(),paint);
        NewsApplication.setBitmap(alterBitmap);
        ivAccount.setImageBitmap(alterBitmap);//设置头像
    }
}
