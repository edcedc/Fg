package com.fanwang.fgsb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgsb.base.User;
import com.fanwang.fgsb.gt.MyGTIntentService;
import com.fanwang.fgsb.gt.MyGTService;
import com.fanwang.fgsb.service.NetWorkChangReceiver;
import com.fanwang.fgsb.service.RestartAppService;
import com.fanwang.fgsb.service.killSelfService;
import com.fanwang.fgsb.utils.ScreenTool;
import com.fanwang.fgsb.utils.cache.ShareFirstSqu;
import com.fanwang.fgsb.utils.cache.SharedAccount;
import com.fanwang.fgsb.view.MainFrg;
import com.fanwang.fgsb.view.EquSetFrg;
import com.fanwang.fgsb.view.SetPwdFrg;
import com.fanwang.fgsb.weight.RuntimeRationale;
import com.igexin.sdk.PushManager;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;

import java.util.List;

import me.yokeyword.fragmentation.SupportActivity;

public class MainActivity extends SupportActivity {

    // 定位相关
    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();

    private killSelfService connServiceconn;

    private NetWorkChangReceiver netWorkChangReceiver;

    private Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//禁止横屏
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//隐藏软键盘

        setContentView(R.layout.activity_main);
        act = this;
        connServiceconn = new killSelfService();
        //重启APP
        ServiceUtils.bindService(RestartAppService.class, connServiceconn, BIND_AUTO_CREATE);


        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new BaiduReceiver();
        registerReceiver(mReceiver, iFilter);

        setHasPermission();

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkChangReceiver, filter);
    }



    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connServiceconn != null){
//            unbindService(connServiceconn);
        }
        if (netWorkChangReceiver != null) {
            unregisterReceiver(netWorkChangReceiver);
        }
        if (myListener != null){
            mLocClient.unRegisterLocationListener(myListener);
        }
        if (mReceiver != null){
            // 取消监听 SDK 广播
            unregisterReceiver(mReceiver);
        }
    }

    /**
     * 定位SDK监听函数
     */
    private int locationNumber = 10;
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            locationNumber--;
            LogUtils.e(location.getLocType());
            LogUtils.e(location.getLatitude(), location.getLongitude(), location.getProvince(), location.getCity(), location.getDistrict());
            double latitude = location.getLatitude();//23.063842
            double longitude = location.getLongitude();//113.413942
            String province = location.getProvince();
            String city = location.getCity();
            String district = location.getDistrict();
            String street = location.getStreet();
            String streetNumber = location.getStreetNumber();
            Bundle bundle = new Bundle();
            bundle.putString("province", province == null ? "广东省" : province);
            bundle.putString("city", city == null ? "广州市" : city);
            bundle.putString("district", district == null ? "番禺区" : district);
            bundle.putString("street", street == null ? "" : street);
            bundle.putString("streetNumber", streetNumber == null ? "" : streetNumber);
//            EventBusActivityScope.getDefault(MainActivity.this).post(new BundleDataInEvent(BundleDataInEvent.mLookMap));
            LogUtils.e(locationNumber);
            if (!StringUtils.isEmpty(province)){
                bundle.putDouble("latitude", latitude);
                bundle.putDouble("longitude", longitude);
                User.getInstance().setAddressBundle(bundle);
                mLocClient.stop();
                if (ShareFirstSqu.getInstance(act).getIsLogin()){
                    if (findFragment(MainFrg.class) == null) {
                        loadRootFragment(R.id.fl_container, MainFrg.newInstance());
                    }
                }else {
                    if (findFragment(EquSetFrg.class) == null) {
                        loadRootFragment(R.id.fl_container, EquSetFrg.newInstance());
                    }
                }
            }else if (locationNumber == 0){
                bundle.putDouble("latitude", 23.063842);
                bundle.putDouble("longitude", 113.413942);
                User.getInstance().setAddressBundle(bundle);
                mLocClient.stop();
                if (ShareFirstSqu.getInstance(act).getIsLogin()){
                    if (findFragment(MainFrg.class) == null) {
                        loadRootFragment(R.id.fl_container, MainFrg.newInstance());
                    }
                }else {
                    if (findFragment(EquSetFrg.class) == null) {
                        loadRootFragment(R.id.fl_container, EquSetFrg.newInstance());
                    }
                }
            }else {
                ToastUtils.showShort("当前定位失败，请检查网络或者定位相同权限是否开启" + locationNumber);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocClient.stop();
    }

    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
//        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
        super.onResume();
    }

    /**
     * Android 点击EditText文本框之外任何地方隐藏键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {

                return true;
            }
        }
        return false;
    }

    /**
     *  设置权限
     */
    private void setHasPermission() {
        AndPermission.with(act)
                .runtime()
                .permission(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                        ,android.Manifest.permission.ACCESS_FINE_LOCATION
                        ,android.Manifest.permission.READ_PHONE_STATE
                        ,android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        setPermissionOk();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(act, permissions)) {
                            showSettingDialog(act, permissions);
                        }else {
                            setPermissionCancel();
                        }
                    }
                })
                .start();
    }

    private void setPermissionOk() {
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true); //加上这个配置后才可以取到详细地址信息
        mLocClient.setLocOption(option);
        mLocClient.start();
    }


    /**
     * Display setting dialog.
     */
    public void showSettingDialog(Context context, final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));

        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPermission();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPermissionCancel();
                    }
                })
                .show();
    }

    /**
     * Set permissions.
     */
    private void setPermission() {
        AndPermission.with(this)
                .runtime()
                .setting()
                .onComeback(new Setting.Action() {
                    @Override
                    public void onAction() {
                        setHasPermission();
//                        ToastUtils.showShort("用户从设置页面返回。");
                    }
                })
                .start();
    }


    /**
     * 权限有任何一个失败都会走的方法
     */
    private void setPermissionCancel() {
        act.finish();
    }


    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    private class BaiduReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                LogUtils.e("key 验证出错! 错误码 :" + intent.getIntExtra
                        (SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, 0)
                        + " ; 请在 AndroidManifest.xml 文件中检查 key 设置");
            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
                LogUtils.e("key 验证成功! 功能可以正常使用");
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                LogUtils.e("网络出错");
            }
        }
    }

    private BaiduReceiver mReceiver;



}
