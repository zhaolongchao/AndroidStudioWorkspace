package com.zhuoxin.newslocation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //获取定位服务
    LocationManager locationManager;
    private Location loc;
    TextView tv;
    double lat;
    double lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv= (TextView) findViewById(R.id.tv);
       locationManager=
               (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        //判断gps是否开启
//        if(locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)){
//
//        }else{//跳转到gps设置界面
//            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//        }
//        //设置定位的相关参数，定位精度等
//        Criteria c=new Criteria();
//        c.setAccuracy(Criteria.ACCURACY_FINE);//高精度
//        c.setBearingRequired(false);//无方位要求
//        c.setAltitudeRequired(false);//无海拔要求
//        c.setCostAllowed(true);//允许产生资费
//        c.setPowerRequirement(Criteria.POWER_LOW);//低功耗
//        //传入定位配置参数，获取定位提供者数据
//        String provider=locationManager.getBestProvider(c,true);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission
                        (this,Manifest.permission.ACCESS_COARSE_LOCATION)!=
                        PackageManager.PERMISSION_GRANTED){
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
    private LocationListener listener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            loc=location;
            Log.e("定位信息",loc.getLatitude()+":"+loc.getLongitude());
            lat=loc.getLatitude();//经度
            lng=loc.getLongitude();//纬度
            tv.setText(lat+":"+lng);
            //Geocoder--地理编码对象
            try {
                //获取给定经纬度的一个地址集合，第三参数可以随意给定
                List<Address> list=
                        new Geocoder(MainActivity.this).getFromLocation(lat,lng,10);
                //获取  市，区，地址
                tv.setText("当前城市:"+list.get(0).getLocality()+":"+
                        list.get(0).getSubLocality()+":"+list.get(0).getAddressLine(0));
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
}
