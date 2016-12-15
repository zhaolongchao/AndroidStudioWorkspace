package com.zhuoxin.testmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by l on 2016/12/8.
 */

public class PoiItemActivity extends Activity
        implements View.OnClickListener, LocationSource, AMapLocationListener, AMap.OnMarkerClickListener {
    TextView tvPoiName;
    Button btnChange;
    ListView lvPoi;
    MapView mvPoi;
    private AMap aMap;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient=null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    OnLocationChangedListener listener;
    private MarkerOptions markerOptions;
    private Marker myMarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_item);
        initView();//实例化控件
        mvPoi.onCreate(savedInstanceState);
        btnChange.setOnClickListener(this);//给按钮注册监听
        Intent intent=getIntent();
        String poi=intent.getStringExtra("poi");
        tvPoiName.setText(poi);
        if (aMap == null) {
            aMap = mvPoi.getMap();// amap对象初始化成功
            UiSettings settings=aMap.getUiSettings();
            aMap.setLocationSource(this);//设置了定位的监听,这里要实现LocationSource接口
            // 是否显示定位按钮
            settings.setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase
            aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
            aMap.setMyLocationStyle(new MyLocationStyle().
                    myLocationIcon(BitmapDescriptorFactory.
                            fromResource(R.mipmap.navi_map_gps_locked)));
            aMap.clear();//清空地图上所有已经标注的marker
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mvPoi.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mvPoi.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mvPoi.onDestroy();
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mvPoi.onSaveInstanceState(outState);
    }

    //实例化控件
    private void initView() {
        tvPoiName= (TextView) findViewById(R.id.tv_poi_name);
        btnChange= (Button) findViewById(R.id.btn_change);
        lvPoi= (ListView) findViewById(R.id.lv_poi);
        mvPoi= (MapView) findViewById(R.id.mv_poi);
        btnChange.setText("列表");
    }

    //给按钮注册监听，切换地图或列表
    @Override
    public void onClick(View view) {
        if(btnChange.getText().toString().equals("列表")){
            mvPoi.setVisibility(View.INVISIBLE);
            lvPoi.setVisibility(View.VISIBLE);
            btnChange.setText("地图");
        }else{
            mvPoi.setVisibility(View.VISIBLE);
            lvPoi.setVisibility(View.INVISIBLE);
            btnChange.setText("列表");
        }
    }

    // 配置定位参数，启动定位
    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：该方法默认为false。
        mLocationOption.setOnceLocation(false);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        // 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(false);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(2000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    //激活定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        listener = onLocationChangedListener;
        initLocation();//定位
    }

    //停止定位
    @Override
    public void deactivate() {
        listener=null;
    }

    //设置定位回调监听
    AMapLocation aMapLocation;
    private boolean flag=false;
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                aMapLocation=amapLocation;
                if(!flag){
                    listener.onLocationChanged(amapLocation);
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));//设置比例
                    flag=true;
                }
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                //获取定位时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);
                setUpMap(amapLocation);
            }else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }
    //添加marker和监听
    private void setUpMap(AMapLocation amapLocation) {
        aMap.setOnMarkerClickListener(this);
        addMarkersToMap(amapLocation);// 往地图上添加marker
    }
    // 往地图上添加marker
    private void addMarkersToMap(AMapLocation amapLocation) {
        markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        markerOptions.position( new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince()
                + "" + amapLocation.getCity() +  "" + amapLocation.getDistrict()
                + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
        markerOptions.draggable(true);
        markerOptions.zIndex(20);
        markerOptions.title(buffer.toString());
        myMarker= aMap.addMarker(markerOptions);
        //myMarker.showInfoWindow();// 设置默认显示一个infowindow
    }

    //对marker标注点点击响应事件
    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, "您点击了Marker"+marker.getId(),
                Toast.LENGTH_LONG).show();
        return true;
    }

}
