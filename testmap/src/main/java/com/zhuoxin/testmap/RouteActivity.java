package com.zhuoxin.testmap;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.overlay.DrivingRouteOverlay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.zhuoxin.utils.AMapUtil;
import com.zhuoxin.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by l on 2016/12/8.
 */

public class RouteActivity extends Activity
        implements LocationSource, AMapLocationListener, View.OnClickListener,
        RouteSearch.OnRouteSearchListener, GeocodeSearch.OnGeocodeSearchListener,
        TextWatcher, Inputtips.InputtipsListener {
    private AutoCompleteTextView actvStart,actvEnd;
    private TextView tvRouteCar,tvRouteBus,tvRouteFoot,tvRouteList;
    private ListView lvRoute;
    private MapView mvRoute;
    private AMap aMap;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient=null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    OnLocationChangedListener listener;
    private RouteSearch routeSearch;//路线检索
    private GeocodeSearch geocodeSearch;//地理编码检索
    private DriveRouteResult mDriveRouteResult;
    private LatLonPoint startPoint;
    private LatLonPoint endPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        assignViews();//实例化控件+监听
        mvRoute.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mvRoute.getMap();// amap对象初始化成功
            UiSettings settings=aMap.getUiSettings();
            aMap.setLocationSource(this);//设置了定位的监听,这里要实现LocationSource接口
            // 是否显示定位按钮
            settings.setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase
            aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
            aMap.setMyLocationStyle(new MyLocationStyle().
                    myLocationIcon(BitmapDescriptorFactory.
                            fromResource(R.mipmap.navi_map_gps_locked)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mvRoute.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mvRoute.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mvRoute.onDestroy();
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mvRoute.onSaveInstanceState(outState);
    }

    //实例化控件+监听
    private void assignViews() {
        actvStart = (AutoCompleteTextView) findViewById(R.id.actv_start);
        actvEnd = (AutoCompleteTextView) findViewById(R.id.actv_end);
        tvRouteCar = (TextView) findViewById(R.id.tv_route_car);
        tvRouteBus = (TextView) findViewById(R.id.tv_route_bus);
        tvRouteFoot = (TextView) findViewById(R.id.tv_route_foot);
        tvRouteList = (TextView) findViewById(R.id.tv_route_list);
        mvRoute = (MapView) findViewById(R.id.mv_route);
        lvRoute = (ListView) findViewById(R.id.lv_route);
        actvStart.addTextChangedListener(this);
        actvEnd.addTextChangedListener(this);
        tvRouteCar.setOnClickListener(this);
        tvRouteBus.setOnClickListener(this);
        tvRouteFoot.setOnClickListener(this);
        tvRouteList.setOnClickListener(this);
        routeSearch=new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        geocodeSearch=new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
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
            }else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }
    //按钮监听事件
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_route_car:
                getLatlon(actvStart.getText().toString());
                //startPoint=latLonPoint;
                getLatlon(actvEnd.getText().toString());
                //endPoint=latLonPoint;
                searchRouteResult(2, RouteSearch.DrivingDefault);
                break;
            case R.id.tv_route_bus:

                break;
            case R.id.tv_route_foot:

                break;
            case R.id.tv_route_list:
                if(tvRouteList.getText().toString().equals("列表")){
                    mvRoute.setVisibility(View.INVISIBLE);
                    lvRoute.setVisibility(View.VISIBLE);
                    tvRouteList.setText("地图");
                }else{
                    mvRoute.setVisibility(View.VISIBLE);
                    lvRoute.setVisibility(View.INVISIBLE);
                    tvRouteList.setText("列表");
                }
                break;
        }
    }

    private void setfromandtoMarker() {
        aMap.addMarker(new MarkerOptions()
                .position(new LatLng(startPoint.getLatitude(),startPoint.getLongitude()))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.bubble_start)));
        aMap.addMarker(new MarkerOptions()
                .position(new LatLng(endPoint.getLatitude(),endPoint.getLongitude()))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.bubble_end)));
    }
    //开始搜索路径规划方案
    public void searchRouteResult(int routeType, int mode) {
        if (startPoint == null) {
            Toast.makeText(this,"定位中，稍后再试...",Toast.LENGTH_SHORT).show();
            return;
        }
        if (endPoint == null) {
            Toast.makeText(this,"终点未设置",Toast.LENGTH_SHORT).show();
        }
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                startPoint, endPoint);
        if (routeType == 2) {// 驾车路径规划
            /**第一个参数表示路径规划的起点和终点，
             * 第二个参数表示驾车模式，
             * 第三个参数表示途经点，
             * 第四个参数表示避让区域，
             * 第五个参数表示避让道路*/
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(
                    fromAndTo, mode, null, null, "");
            routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        //aMap.clear();// 清理地图上的所有覆盖物
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                if (driveRouteResult.getPaths().size() > 0) {
                    mDriveRouteResult = driveRouteResult;
                    final DrivePath drivePath = mDriveRouteResult.getPaths().get(0);
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            this, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    //设置节点marker是否显示
                    drivingRouteOverlay.setNodeIconVisibility(false);
                    //是否用颜色展示交通拥堵情况，默认true
                    //drivingRouteOverlay.setIsColorfulline(true);
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                } else if (driveRouteResult != null && driveRouteResult.getPaths() == null) {
                    Toast.makeText(this,"对不起，没有搜索到相关数据",Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this,"对不起，没有搜索到相关数据",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,i,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
    //逆地理编码回调
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }
    //地理编码查询回调
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                //地址输入正确，在这里调用输入正确地址后的操作
                //这里也可以获取到经纬度坐标，具体代码如下
                GeocodeAddress addressCode = result.getGeocodeAddressList().get(0);
                if (startPoint == null) {
                    startPoint = addressCode.getLatLonPoint();
                } else if (endPoint == null){
                    endPoint = addressCode.getLatLonPoint();
                    setfromandtoMarker();//添加覆盖物
                }
                //纬度:addressCode.getLatLonPoint().getLatitude()
                //经度:addressCode.getLatLonPoint().getLongitude()
            } else {
                Toast.makeText(this,"请输入正确的家庭地址",Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    //响应地理编码
    public void getLatlon(String name) {
        // 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
        GeocodeQuery query = new GeocodeQuery(name, "0311");
        geocodeSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String newText = charSequence.toString().trim();
        if (!AMapUtil.IsEmptyOrNullString(newText)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText,"石家庄");
            Inputtips inputTips = new Inputtips(this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
    //搜索联想
    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.route_inputs, listString);
            actvStart.setAdapter(aAdapter);
            actvEnd.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showerror(this, rCode);
        }
    }

}
