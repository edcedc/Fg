package com.fanwang.fgcm.view.ui;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FLookAdvertisementBinding;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.event.GtInEvent;
import com.fanwang.fgcm.presenter.LookAdvertisementPresenter;
import com.fanwang.fgcm.presenter.LookAdvertisementPresenterlmpl;
import com.fanwang.fgcm.utils.Constants;
import com.fanwang.fgcm.view.impl.LookAdvertisementView;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by edison on 2018/4/13.
 *  看广告
 *
 *
 *   百度地图对应缩放级别
     int[] zoomLevel = { 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6,5, 4, 3 };
     对应级别单位
     String[] zoomLevelStr = { “10”, “20”, “50”, “100”, “200”, “500”, “1000”,
     “2000”, “5000”, “10000”, “20000”, “25000”, “50000”, “100000”,
     “200000”, “500000”, “1000000”, “2000000” }; // 单位/m
 */

public class LookAdvertisementFrg extends BaseFragment<FLookAdvertisementBinding>implements SensorEventListener, View.OnClickListener, LookAdvertisementView{

    public static LookAdvertisementFrg newInstance() {
        Bundle args = new Bundle();
        LookAdvertisementFrg fragment = new LookAdvertisementFrg();
        fragment.setArguments(args);
        return fragment;
    }

    // 定位相关
    private LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private BaiduMap mBaiduMap;

    private BitmapDescriptor icon_red = BitmapDescriptorFactory
            .fromResource(R.mipmap.c03);
    private BitmapDescriptor icon_lng = BitmapDescriptorFactory
            .fromResource(R.mipmap.d05);

    private LookAdvertisementPresenter presenter;
    private List<DataBean> listBean = new ArrayList<>();

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_look_advertisement;
    }

    @Override
    protected void initView(View view) {
        presenter = new LookAdvertisementPresenterlmpl(this);
        mB.tvNews.init(act.getWindowManager());
        mB.tvNews.stopScroll();
        // 定位初始化
        mLocClient = new LocationClient(act);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mSensorManager = (SensorManager) act.getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mBaiduMap = mB.bmapView.getMap();
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, false,
                null, 0, 0));
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mB.ivMyEnvelopes.setOnClickListener(this);
        mB.tvMissionIntroduction.setOnClickListener(this);
        setSwipeBackEnable(false);
        EventBusActivityScope.getDefault(act).register(this);
        EventBus.getDefault().register(this);
    }

    /**
     * 设置圆圈
     * @param latitude
     * @param longitude
     */
    private void setRadius(double latitude, double longitude, int kilometre) {
        // 添加圆
        LatLng llCircle = new LatLng(latitude, longitude);
        OverlayOptions ooCircle = new CircleOptions()
                //2.给自己设置数据
                .center(llCircle) //圆心
                .radius(kilometre)//半径 单位米
                .fillColor(0x20773ee4);//填充色
//                    .stroke(new Stroke(2,0x60773ee4));//边框宽度和颜色
        mBaiduMap.addOverlay(ooCircle);
//        marker = (Marker) (mBaiduMap.addOverlay(ooCircle));
    }

    @Subscribe
    public void onMainThreadInEvent(BundleDataInEvent event){
        if (event.getType() == BundleDataInEvent.LookAdvertisementList){
            presenter.onRequest();
            presenter.onAdvertisingTasks();
        }
    }

    @Subscribe
    public void onMainThreadInEvent(GtInEvent event){
        JSONObject data = (JSONObject) event.getObject();
        final String remark = data.optString("remark");
//        presenter.onRequest();
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mB.tvNews.setText("附近有新红包出现");
                mB.tvNews.init(act.getWindowManager());
                mB.tvNews.startScroll();

                new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        mB.tvNews.setText("");
                        mB.tvNews.init(act.getWindowManager());
                        mB.tvNews.stopScroll();
                    }
                }.start();
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_mission_introduction://任务介绍
                UIHelper.startHtmlFrg(this, 1);
                break;
            case R.id.iv_my_envelopes://我的红包
                UIHelper.startMyRedEnvelopesFrg(this);
                break;
        }
    }

    @Override
    public void setData(Object data) {
        mBaiduMap.clear();
        mBaiduMap.removeMarkerClickListener(mMarkerlis);

        Bundle bundle1 = User.getInstance().getAddressBundle();
        setRadius(bundle1.getDouble("latitude"), bundle1.getDouble("longitude"), 1000);

        mBaiduMap.setOnMarkerClickListener(mMarkerlis);
        List<DataBean> list = (List<DataBean>) data;
        listBean.clear();
        listBean.addAll(list);
        for (int i = 0;i < list.size();i++){
            DataBean bean = list.get(i);
            LatLng ll = new LatLng(bean.getLat(), bean.getLon());
            MarkerOptions ooA = new MarkerOptions().position(ll).icon(icon_red).zIndex(9).draggable(true);
//            ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
            Marker marker = (Marker) mBaiduMap.addOverlay(ooA);
            Bundle bundle = new Bundle();
            bundle.putString("bean", new Gson().toJson(bean));
            marker.setExtraInfo(bundle);
            mBaiduMap.addOverlay(ooA);
        }
    }

    @Override
    public void showText(String content) {
        mB.tvNews.setText(content);
    }

    @Subscribe
    public void onMainThreadEvent(BundleDataInEvent event){
        if (event.getType() == BundleDataInEvent.mLookMap){
            presenter.onRequest();
        }else if (event.getType() == BundleDataInEvent.reb_Advertisement){
            presenter.onRequest();
        }
    }

    @Override
    public void onError(Throwable e) {

    }


    private BaiduMap.OnMarkerClickListener mMarkerlis = new BaiduMap.OnMarkerClickListener(){

        @Override
        public boolean onMarkerClick(Marker marker) {
//            Bundle bundle = marker.getExtraInfo();
//            DataBean bean = new Gson().fromJson(bundle.getString("bean"), DataBean.class);
//            UIHelper.startLooKDetailsFrg(LookAdvertisementFrg.this, bean);
            LatLng lng = marker.getPosition();
            double latitude = lng.latitude;
            double longitude = lng.longitude;
            for (int i = 0;i < listBean.size();i++){
                DataBean bean = listBean.get(i);
                if (latitude == bean.getLat()){
                    UIHelper.startLooKDetailsFrg(LookAdvertisementFrg.this, bean, 0);
                    break;
                }
            }
            return true;
        }
    };


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mB.bmapView == null || isFirstLoc == false) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(Constants.baidu_map_zoom);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

    }

    @Override
    public void onPause() {
        mB.bmapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mB.bmapView.onResume();
        super.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mB.bmapView.onDestroy();
//        bmapView = null;
        icon_red.recycle();
        EventBusActivityScope.getDefault(act).unregister(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
