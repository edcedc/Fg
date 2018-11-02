package com.fanwang.fgcm.view.ui;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.blankj.utilcode.util.LogUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.SearchListBean;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FThrowAdvertisementBinding;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.utils.Constants;
import com.fanwang.fgcm.utils.DataListTool;
import com.fanwang.fgcm.utils.DialogUtil;
import com.fanwang.fgcm.utils.TopRightMenuTool;
import com.fanwang.fgcm.weight.toprightmenu.MenuItem;
import com.fanwang.fgcm.weight.toprightmenu.TopRightMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by edison on 2018/4/13.
 *  投放广告
 */

public class ThrowAdvertisementFrg extends BaseFragment<FThrowAdvertisementBinding>implements SensorEventListener, View.OnClickListener, BaiduMap.OnMarkerClickListener, BaiduMap.OnMapClickListener{

    private Marker marker;

    public static ThrowAdvertisementFrg newInstance() {
        Bundle args = new Bundle();
        ThrowAdvertisementFrg fragment = new ThrowAdvertisementFrg();
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
    private BitmapDescriptor icon_lng = BitmapDescriptorFactory
            .fromResource(R.mipmap.d05);
    private List<MenuItem> menuItems = new ArrayList<>();

    private int launchType = 0;

    @Override
    protected void initParms(Bundle bundle) {

    }
    @Override
    protected int bindLayout() {
        return R.layout.f_throw_advertisement;
    }

    @Override
    protected void initView(View view) {
        // 定位初始化
        mLocClient = new LocationClient(act);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true); //加上这个配置后才可以取到详细地址信息
        mLocClient.setLocOption(option);
        mLocClient.start();
        mSensorManager = (SensorManager) act.getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mBaiduMap = mB.bmapView.getMap();
        mBaiduMap.setOnMarkerClickListener(this);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, false, null, 0 ,0));
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mB.lyLocation.setOnClickListener(this);
        mB.ivPhone.setOnClickListener(this);
        mB.ivEquipment.setOnClickListener(this);
        mB.tvDelivery.setOnClickListener(this);
        EventBus.getDefault().register(this);

        menuItems.addAll(DataListTool.getKilometreList());
        mBaiduMap.setOnMapClickListener(this);
        setSwipeBackEnable(false);
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
    public boolean onMarkerClick(Marker marker) {
        if (this.marker == marker){
            LogUtils.e("点击了");
        }
        LogUtils.e("-----------");
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
//        MarkerOptions ooA = new MarkerOptions().position(latLng).icon(icon_lng);
//        mBaiduMap.clear();
//        mBaiduMap.addOverlay(ooA);
        LogUtils.e(latLng.latitude, latLng.longitude);
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }


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
                setLatLng(location.getLatitude(), location.getLongitude());
                String province = location.getProvince();
                String city = location.getCity();
                String district = location.getDistrict();
                String street = location.getStreet();
                String streetNumber = location.getStreetNumber();
                mB.tvLocation.setText(province + city + district + street + streetNumber);
            }
        }
    }

    /**
     *  定位
     * @param latitude
     * @param longitude
     */
    private void setLatLng(double latitude, double longitude){
        LatLng ll = new LatLng(latitude,longitude);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(Constants.baidu_map_zoom);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
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
        super.onDestroy();
        icon_lng.recycle();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(BundleDataInEvent event) {
        if (event.getType() == BundleDataInEvent.search_1) {
            SearchListBean bean = (SearchListBean) event.getObject();
            setLatLng(bean.getLatitude(), bean.getLongitude());

            LatLng lng = new LatLng(bean.getLatitude(), bean.getLongitude());
            MarkerOptions ooA = new MarkerOptions().position(lng).icon(icon_lng).zIndex(9).draggable(true);
//            ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
            marker = (Marker) (mBaiduMap.addOverlay(ooA));
            mB.tvLocation.setText(bean.getCity() + bean.getDistrict() + bean.getName() + bean.getBusiness());

            this.mCurrentLon = bean.getLongitude();
            this.mCurrentLat = bean.getLatitude();

            // 添加圆
//            LatLng llCircle = new LatLng(bean.getLatitude(), bean.getLongitude());
//            OverlayOptions ooCircle = new CircleOptions()
//            //2.给自己设置数据
//                    .center(llCircle) //圆心
//                    .radius(100)//半径 单位米
//                    .fillColor(0x20773ee4);//填充色
////                    .stroke(new Stroke(2,0x60773ee4));//边框宽度和颜色
//            mBaiduMap.addOverlay(ooCircle);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ly_location://现在定位
                UIHelper.startSearchFrg(this, BundleDataInEvent.search_1);
                break;
            case R.id.iv_phone://选择手机模式
                launchType = 0;
                UIHelper.startLaunchDetailsFrg(this, launchType);
                break;
            case R.id.iv_equipment://选择设备模式
                launchType = 1;
                DialogUtil.showSelectEquipmentModel(act,DialogUtil.showSelectListType_0, new DialogUtil.OnClickListener() {
                    @Override
                    public void onClick(int position) {
                        if (position == 0){
                            UIHelper.startEquipmentMapFrg(ThrowAdvertisementFrg.this, mCurrentLat, mCurrentLon, mB.tvLocation.getText().toString());
                        }else {
                            UIHelper.startEquipmentListFrg(ThrowAdvertisementFrg.this, mCurrentLat, mCurrentLon, mB.tvLocation.getText().toString());
                        }
                    }
                });
                break;
            case R.id.tv_delivery://投放范围
                TopRightMenuTool.TopRightMenu(act, menuItems, mB.tvDelivery, 0, new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        MenuItem item = menuItems.get(position);
                        mB.tvDelivery.setText(item.getText());
                    }
                });
                break;
        }
    }

}
