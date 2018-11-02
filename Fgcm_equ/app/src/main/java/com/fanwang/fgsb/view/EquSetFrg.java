package com.fanwang.fgsb.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.fgsb.controller.UIHelper;
import com.fanwang.fgsb.utils.MacAddressTool;
import com.fanwang.fgsb.utils.cache.ShareFirstSqu;
import com.zaaach.toprightmenu.MenuItem;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.DeviceUtils;
import com.fanwang.fgsb.R;
import com.fanwang.fgsb.base.BaseFragment;
import com.fanwang.fgsb.base.User;
import com.fanwang.fgsb.bean.DataBean;
import com.fanwang.fgsb.presenter.EquSetPresenterlmpl;
import com.fanwang.fgsb.view.impl.EquSetView;
import com.jakewharton.rxbinding2.view.RxView;
import com.zaaach.toprightmenu.TopRightMenu;
import com.zaaach.toprightmenu.TopRightMenuTool;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 作者：yc on 2018/7/5.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 * 设置
 */

public class EquSetFrg extends BaseFragment implements EquSetView {

    private EquSetPresenterlmpl presenterl;
    public static EquSetFrg newInstance() {
        Bundle args = new Bundle();
        EquSetFrg fragment = new EquSetFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private EditText etSetName, etAddress, etLongitude, etLatitude;

    private TextView tvId, tvProvince, tvCity, tvArea, tvEquId;
    private View fyProvince, fyCity, fyArea, btSubmit, fyEquId;
    private View btCancel;

    private boolean isProvince = false;
    private List<MenuItem> menuItems = new ArrayList<>();
    private String provinceId;
    private String cityId;
    private String areaId;
    private String equId;

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_equ_set;
    }

    @Override
    protected void initView(View view) {
        presenterl = new EquSetPresenterlmpl(this);
        etSetName = view.findViewById(R.id.et_set_name);
        tvId = view.findViewById(R.id.tv_id);
        fyEquId = view.findViewById(R.id.fy_equ_id);
        tvEquId = view.findViewById(R.id.tv_equ_id);
        fyProvince = view.findViewById(R.id.fy_province);
        tvProvince = view.findViewById(R.id.tv_province);
        fyCity = view.findViewById(R.id.fy_city);
        tvCity = view.findViewById(R.id.tv_city);
        fyArea = view.findViewById(R.id.fy_area);
        tvArea = view.findViewById(R.id.tv_area);
        etAddress = view.findViewById(R.id.et_address);
        etLongitude = view.findViewById(R.id.et_longitude);
        etLatitude = view.findViewById(R.id.et_latitude);
        btCancel = view.findViewById(R.id.bt_cancel);
        btSubmit = view.findViewById(R.id.bt_submit);
        btCancel.setVisibility(!ShareFirstSqu.getInstance(act).getIsLogin() ? View.INVISIBLE : View.VISIBLE);

        showEqu();
        initClick();
    }

    private void initClick() {
        RxView.clicks(fyProvince)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Observer<Object>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object object) {
                        if (isProvince) {
                            TopRightMenuTool.TopRightMenu(act, menuItems, fyProvince, new TopRightMenu.OnMenuItemClickListener() {
                                @Override
                                public void onMenuItemClick(int position) {
                                    MenuItem item = menuItems.get(position);
                                    tvProvince.setText(item.getText());
                                    provinceId = item.getId();
                                }
                            });
                        } else {
                            presenterl.regionListProvince();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        RxView.clicks(fyCity)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Observer<Object>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object object) {
                        presenterl.regionListLevel(provinceId, 0);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        RxView.clicks(fyArea)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Observer<Object>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object object) {
                        presenterl.regionListLevel(cityId, 1);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        RxView.clicks(btSubmit)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Observer<Object>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object object) {
                        presenterl.save(etSetName.getText().toString(), tvId.getText().toString(), tvProvince.getText().toString(), tvCity.getText().toString(), tvArea.getText().toString(), etAddress.getText().toString(),
                                etLatitude.getText().toString(), etLongitude.getText().toString(), equId);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        RxView.clicks(fyEquId)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Observer<Object>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object object) {
                        presenterl.labelListModelLabelId();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(btCancel)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Observer<Object>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object object) {
                        pop();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void showEqu() {
        String macAddress = MacAddressTool.getMacAddress();
        if (!StringUtils.isEmpty(macAddress)){
            tvId.setText(macAddress.replaceAll(":", ""));
        }
        Bundle bundle = User.getInstance().getAddressBundle();
        tvProvince.setText(bundle.getString("province"));
        tvCity.setText(bundle.getString("city"));
        tvArea.setText(bundle.getString("district"));
        etLatitude.setText(bundle.getDouble("latitude") + "");
        etLongitude.setText(bundle.getDouble("longitude") + "");
        etAddress.setText(bundle.getString("province") + bundle.getString("city") + bundle.getString("district") + bundle.getString("street") + bundle.getString("streetNumber"));
    }



    @Override
    public void showProvince(Object object) {
        isProvince = true;
        menuItems.clear();
        menuItems = getMenuItem(object);
        TopRightMenuTool.TopRightMenu(act, menuItems, fyProvince, new TopRightMenu.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position) {
                MenuItem item = menuItems.get(position);
                tvProvince.setText(item.getText());
                provinceId = item.getId();
                tvCity.setText("");
                tvArea.setText("");
            }
        });
    }

    @Override
    public void showCity(Object object) {
        final List<MenuItem> menuItems = getMenuItem(object);
        TopRightMenuTool.TopRightMenu(act, menuItems, fyCity, new TopRightMenu.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position) {
                MenuItem item = menuItems.get(position);
                tvCity.setText(item.getText());
                cityId = item.getId();
                tvArea.setText("");
            }
        });
    }

    @Override
    public void onSaveSuccess() {
        ShareFirstSqu.getInstance(act).save(true);
        UIHelper.startMainFrg(this);
    }

    @Override
    public void onSaveLabel(Object object) {
        final List<MenuItem> menuItems = getMenuItem(object);
        TopRightMenuTool.TopRightMenu(act, menuItems, fyEquId, new TopRightMenu.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position) {
                MenuItem item = menuItems.get(position);
                tvEquId.setText(item.getText());
                equId = item.getId();
            }
        });
    }

    @Override
    public void showArea(Object object) {
        final List<MenuItem> menuItems = getMenuItem(object);
        TopRightMenuTool.TopRightMenu(act, menuItems, fyArea, new TopRightMenu.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position) {
                MenuItem item = menuItems.get(position);
                tvArea.setText(item.getText());
                areaId = item.getId();
            }
        });
    }

    private List<MenuItem> getMenuItem(Object object) {
        final List<MenuItem> menuItems = new ArrayList<>();
        List<DataBean> list = (List<DataBean>) object;
        for (int i = 0; i < list.size(); i++) {
            DataBean bean = list.get(i);
            MenuItem item = new MenuItem();
            item.setText(bean.getName());
            item.setId(bean.getId());
            menuItems.add(item);
        }
        return menuItems;
    }

}
