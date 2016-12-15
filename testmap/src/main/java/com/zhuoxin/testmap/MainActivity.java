package com.zhuoxin.testmap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.zhuoxin.testmap.R.id.map;

public class MainActivity extends Activity
        implements View.OnClickListener, LocationSource, AMapLocationListener,SensorEventListener {
    private RelativeLayout rl;//主页面相对布局
    private TextView tvNearby;//附近
    private TextView tvRoute;//路线
    private TextView tvMy;//我的
    private ImageView ivZoomin;//放大+
    private ImageView ivZoomout;//缩小-
    private ImageView ivDian;//3D模式
    private ImageView ivSearch;//搜索
    private EditText etSearch;//输入框
    private ImageView ivVoice;//语音
    private ImageView ivMsg;//消息
    private ImageView ivLayer;//地图模式选择
    private ImageView ivTraffic;//路况
    private ImageView ivReport;//上报
    private ImageView ivBus;//公交雷达
    private double lat;//纬度
    private double lon;//经度
    private MapView mMapView = null;
    private AMap aMap;// amap对象
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient=null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    OnLocationChangedListener listener;
    SensorManager sensorManager;//传感器管理者对象
    Sensor mSensor;//传感器对象
    float x,y,z;
    private long lastTime=0;
    private float mAngle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();//控件实例化
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();// amap对象初始化成功
            UiSettings settings=aMap.getUiSettings();
            aMap.setLocationSource(this);//设置了定位的监听,这里要实现LocationSource接口
            //隐藏高德地图默认的放大缩小控件
            settings.setZoomControlsEnabled(false);
            // 是否显示定位按钮
            settings.setMyLocationButtonEnabled(true);
            //是否显示指南针
            settings.setCompassEnabled(true);
            aMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase
            aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
            aMap.setMyLocationStyle(new MyLocationStyle().
                    myLocationIcon(BitmapDescriptorFactory.
                            fromResource(R.mipmap.navi_map_gps_locked)));
        }
        initListener();//设置监听
        //传感器管理者对象
        sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //传感器对象
        mSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

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

    //控件实例化
    private void assignViews() {
        rl= (RelativeLayout) findViewById(R.id.rl);
        mMapView = (MapView) findViewById(map);//获取地图控件引用
        tvNearby = (TextView) findViewById(R.id.tv_nearby);
        tvRoute = (TextView) findViewById(R.id.tv_route);
        tvMy = (TextView) findViewById(R.id.tv_my);
        ivZoomin = (ImageView) findViewById(R.id.iv_zoomin);
        ivZoomout = (ImageView) findViewById(R.id.iv_zoomout);
        ivDian = (ImageView) findViewById(R.id.iv_dian);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
        etSearch = (EditText) findViewById(R.id.et_search);
        ivVoice = (ImageView) findViewById(R.id.iv_voice);
        ivMsg = (ImageView) findViewById(R.id.iv_msg);
        ivLayer = (ImageView) findViewById(R.id.iv_layer);
        ivTraffic = (ImageView) findViewById(R.id.iv_traffic);
        ivReport = (ImageView) findViewById(R.id.iv_report);
        ivBus = (ImageView) findViewById(R.id.iv_bus);
    }

    //设置监听
    private void initListener(){
        tvNearby.setOnClickListener(this);
        tvRoute.setOnClickListener(this);
        tvMy.setOnClickListener(this);
        ivZoomin.setOnClickListener(this);
        ivZoomout.setOnClickListener(this);
        ivDian.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        ivVoice.setOnClickListener(this);
        ivMsg.setOnClickListener(this);
        ivLayer.setOnClickListener(this);
        ivTraffic.setOnClickListener(this);
        ivReport.setOnClickListener(this);
        ivBus.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
       mMapView.onSaveInstanceState(outState);
    }
    int num=0;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_search://搜索
                break;
            case R.id.iv_voice://语音
                break;
            case R.id.iv_msg://消息
                break;
            case R.id.iv_layer://地图模式选择
                popupWindow();
                break;
            case R.id.iv_traffic://路况
                //判断路况图层是否显示
                if (!aMap.isTrafficEnabled()){
                    aMap.setTrafficEnabled(true);
                    ivTraffic.setImageResource(R.mipmap.commute_icon_traffic_checked);
                }else{
                    aMap.setTrafficEnabled(false);
                    ivTraffic.setImageResource(R.mipmap.commute_icon_traffic);
                }
                break;
            case R.id.iv_report://上报
                break;
            case R.id.iv_bus://公交雷达
                break;
            case R.id.iv_zoomin://放大
                aMap.animateCamera(CameraUpdateFactory.zoomIn());
                break;
            case R.id.iv_zoomout://缩小
                aMap.animateCamera(CameraUpdateFactory.zoomOut());
                break;
            case R.id.iv_dian://3D模式
                if(num==0){
                    aMap.showBuildings(true);
                    aMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition(new LatLng(lat,lon),17,45,0)));
                    ivDian.setImageResource(R.mipmap.navi_map_gps_3d);
                    num=1;
                }else{

                    ivDian.setImageResource(R.mipmap.radio_btn_on);
                    num=0;
                }
                break;
            case R.id.tv_nearby://附近
                Intent intentNearby=new Intent(this,PoiActivity.class);
                startActivity(intentNearby);
                break;
            case R.id.tv_route://路线
                Intent intentRoute=new Intent(this,RouteActivity.class);
                startActivity(intentRoute);
                break;
            case R.id.tv_my://我的
                break;
        }
    }
    //地图模式浮动窗口
    private void popupWindow() {
    /*
    创建浮动窗口
    1.加载布局
    2.创建浮动窗口对象
    3.设置相关参数
    4.设置显示位置
    */
        View v=getLayoutInflater().inflate(R.layout.inflate_map_mode,null);
        final ImageView ivStandard= (ImageView) v.findViewById(R.id.iv_standard_map);
        final ImageView ivSatellite= (ImageView) v.findViewById(R.id.iv_satellite_map);
        final ImageView ivBus= (ImageView) v.findViewById(R.id.iv_bus_map);
        final PopupWindow popupWindow=new PopupWindow(
                v,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        ivStandard.setOnClickListener(new View.OnClickListener() {//标准地图
            @Override
            public void onClick(View view) {
                ivStandard.setImageResource(R.mipmap.maplayer_manager_2d_hl);
                ivSatellite.setImageResource(R.mipmap.maplayer_manager_sate);
                ivBus.setImageResource(R.mipmap.maplayer_manager_gj);
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);
            }
        });
        ivSatellite.setOnClickListener(new View.OnClickListener() {//卫星地图
            @Override
            public void onClick(View view) {
                ivSatellite.setImageResource(R.mipmap.maplayer_manager_sate_hl);
                ivStandard.setImageResource(R.mipmap.maplayer_manager_2d);
                ivBus.setImageResource(R.mipmap.maplayer_manager_gj);
                aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
            }
        });
        ivBus.setOnClickListener(new View.OnClickListener() {//公交地图
            @Override
            public void onClick(View view) {
                ivBus.setImageResource(R.mipmap.maplayer_manager_gj_hl);
                ivStandard.setImageResource(R.mipmap.maplayer_manager_2d);
                ivSatellite.setImageResource(R.mipmap.maplayer_manager_sate);
                aMap.setMapType(AMap.MAP_TYPE_NAVI);
            }
        });
        //设置焦点
        popupWindow.setFocusable(true);
        //设置点击边界外使浮动窗口消失
        popupWindow.setOutsideTouchable(true);
        //设置背景 -- 为了在点击浮动窗口以外的区域使浮动窗口消失
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        //设置显示位置
        popupWindow.showAtLocation(rl, Gravity.CENTER,0,0);
    }

    AMapLocation aMapLocation;
    private boolean flag=false;
    //设置定位回调监听
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
                Log.e("地址",amapLocation.getAddress());
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                //获取定位时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);
//                /*地址，如果option中设置isNeedAddress为false，则没有此结果，
//                网络定位结果中会有地址信息，GPS定位不返回地址信息。*/
//                amapLocation.getAddress();
//                amapLocation.getCountry();//国家信息
//                amapLocation.getProvince();//省信息
//                amapLocation.getCity();//城市信息
//                amapLocation.getDistrict();//城区信息
//                amapLocation.getStreet();//街道信息
//                amapLocation.getStreetNum();//街道门牌号信息
//                amapLocation.getCityCode();//城市编码
//                amapLocation.getAdCode();//地区编码
//                amapLocation.getAoiName();//获取当前定位点的AOI信息
                lat=amapLocation.getLatitude();
                lon=amapLocation.getLongitude();
                // 设置当前地图显示为当前位置
                //aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lon),17));
                //设置图钉选项
                MarkerOptions markerOptions=new MarkerOptions();
                //图标
                markerOptions.icon(BitmapDescriptorFactory.
                        fromResource(R.mipmap.travel_guide_marker_selecte_frist));
                //位置
                markerOptions.position(new LatLng(lat,lon));
                StringBuffer buffer = new StringBuffer();
                buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince()
                        + "" + amapLocation.getCity() +  "" + amapLocation.getDistrict()
                        + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
                //标题
                markerOptions.title(buffer.toString());
                markerOptions.visible(true);
                aMap.addMarker(markerOptions);

            }else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，
                // errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }


    }
    //激活定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        listener = onLocationChangedListener;
        initLocation();//定位
        //注册传感器监听
        sensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
    //停止定位
    @Override
    public void deactivate() {
        listener=null;
        //注销传感器监听
        sensorManager.unregisterListener(this,mSensor);
    }
    public static final long TIME_SENSOR=100;//常量
    //传感器改变监听
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (System.currentTimeMillis() - lastTime < TIME_SENSOR) {
            return;
        }
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ORIENTATION: {
                float x = sensorEvent.values[0];
                x += getScreenRotationOnPhone(this);
                x %= 360.0F;
                if (x > 180.0F)
                    x -= 360.0F;
                else if (x < -180.0F)
                    x += 360.0F;
                if (Math.abs(mAngle -90+ x) < 3.0f) {
                    break;
                }
                mAngle = x;
//                if (Math.abs(mAngle - x) < 3.0f) {
//                    break;
//                }
//                mAngle = Float.isNaN(x) ? 0 : x;
                aMap.setMyLocationRotateAngle(mAngle);
                lastTime = System.currentTimeMillis();
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    /**
     * 获取当前屏幕旋转角度
     *
     * @param context
     * @return 0表示是竖屏; 90表示是左横屏; 180表示是反向竖屏; 270表示是右横屏
     */
    public static int getScreenRotationOnPhone(Context context) {
        final Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return -90;
        }
        return 0;
    }


}
