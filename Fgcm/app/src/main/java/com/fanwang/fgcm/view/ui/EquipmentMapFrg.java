package com.fanwang.fgcm.view.ui;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.blankj.utilcode.util.LogUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.base.BaseFragment;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.controller.UIHelper;
import com.fanwang.fgcm.databinding.FEquipmentMapBinding;
import com.fanwang.fgcm.presenter.EquipmentMapPresenterlmpl;
import com.fanwang.fgcm.utils.Constants;
import com.fanwang.fgcm.view.impl.EquipmentMapView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2018/4/13.
 * 投放广告
 */

public class EquipmentMapFrg extends BaseFragment<FEquipmentMapBinding> implements EquipmentMapView, BaiduMap.OnMarkerClickListener {

    private BaiduMap mBaiduMap;
    private BitmapDescriptor icon_lng = BitmapDescriptorFactory
            .fromResource(R.mipmap.d05);
    private BitmapDescriptor icon_equipment = BitmapDescriptorFactory
            .fromResource(R.mipmap.d14);

    private double longitude;
    private double latitude;
    private String location;
    private EquipmentMapPresenterlmpl presenterlmpl;
    private List<DataBean> listBean = new ArrayList<>();

    @Override
    protected void initParms(Bundle bundle) {
        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("longitude");
        location = bundle.getString("location");
        LogUtils.e(longitude, latitude, location);
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_equipment_map;
    }

    @Override
    protected void initView(View view) {
        presenterlmpl = new EquipmentMapPresenterlmpl(this);
        setToolbarTitle(getString(R.string.top_terminal_equipment), false);
        mBaiduMap = mB.bmapView.getMap();
        mBaiduMap.setOnMarkerClickListener(this);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, false, null, 0, 0));
        setLatLng(latitude, longitude);
        setRadius(latitude, longitude, 1000);
        mB.tvLocation.setText(location);
        presenterlmpl.ajaxEquipmentId(latitude, longitude);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng lng = marker.getPosition();
        double latitude = lng.latitude;
        double longitude = lng.longitude;
        for (int i = 0;i < listBean.size();i++){
            DataBean bean = listBean.get(i);
            if (latitude == bean.getLat()){
                String name = bean.getName();
                String eqpIds = bean.getEqp_ids();
                String model = bean.getModelName();
                String address = bean.getAddress_logogram();
                String model_labelId = bean.getModel_labelId();
                showBottomSheetDialog(name,eqpIds,model,address,longitude,latitude, bean.getId(),model_labelId);
                break;
            }
        }
        return true;
    }

    /**
     * 定位
     * @param latitude
     * @param longitude
     */
    private void setLatLng(double latitude, double longitude) {
        LatLng ll = new LatLng(latitude, longitude);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(Constants.baidu_map_zoom);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        MarkerOptions ooA = new MarkerOptions().position(ll).icon(icon_lng).zIndex(9).draggable(true);
//        ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
        mBaiduMap.addOverlay(ooA);
    }

    /**
     * 设置圆圈
     * @param latitude
     * @param longitude
     */
    private void setRadius(double latitude, double longitude, int kilometre) {
        LogUtils.e(kilometre);
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

    @Override
    public void onPause() {
        mB.bmapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mB.bmapView.onResume();
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        // 退出时销毁定位
        // 关闭定位图层
        mBaiduMap.clear();
        mBaiduMap.setMyLocationEnabled(false);
        mB.bmapView.onDestroy();
//        bmapView = null;
        super.onDestroy();
        icon_lng.recycle();
        icon_equipment.recycle();
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void showData(Object data) {
        listBean.clear();
        listBean.addAll((List<DataBean>) data);
        for (int i = 0; i < listBean.size();i++){
            DataBean bean = listBean.get(i);
            LatLng ll = new LatLng(bean.getLat(), bean.getLon());
            MarkerOptions ooA = new MarkerOptions().position(ll).icon(icon_equipment).zIndex(9).draggable(true);
//            ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
            mBaiduMap.addOverlay(ooA);
        }
    }

    @Override
    public void showBottomSheetDialog(final String name, final String eqpIds, final String model,
                                      final String address, final double longitude, final double latitude, final String id, final String model_labelId) {
        final BottomSheetDialog mDialog = new BottomSheetDialog(act);
        View view = getLayoutInflater().inflate(R.layout.p_equipment_map, null);
        mDialog.setContentView(view);
        view.findViewById(R.id.iv_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        TextView tvName = view.findViewById(R.id.tv_name);
        tvName.setText("设备名称：" + name);
        TextView tvModel = view.findViewById(R.id.tv_model);
        tvModel.setText("设备型号：" + model);
        TextView tvId = view.findViewById(R.id.tv_id);
        tvId.setText("设备ID：" + eqpIds);
        TextView tvAddress = view.findViewById(R.id.tv_address);
        tvAddress.setText(address);
        view.findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                UIHelper.startEquipmentDetailsFrg(EquipmentMapFrg.this, name, eqpIds, model, address, longitude, latitude, id, model_labelId);
            }
        });
        mDialog.show();
    }

}
