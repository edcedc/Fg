package com.fanwang.fgsb.presenter;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgsb.bean.DataBean;
import com.fanwang.fgsb.model.EquSetModel;
import com.fanwang.fgsb.model.EquSetModellmpl;
import com.fanwang.fgsb.presenter.listener.OnEquSetListener;
import com.fanwang.fgsb.view.impl.EquSetView;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * 作者：yc on 2018/7/5.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class EquSetPresenterlmpl implements EquSetPresenter, OnEquSetListener{

    private EquSetView view;

    private EquSetModel model;

    public EquSetPresenterlmpl(EquSetView view) {
        this.view = view;
        model = new EquSetModellmpl();
    }

    @Override
    public void regionListProvince() {
        model.regionListProvince(this);
    }

    @Override
    public void regionListLevel(String provinceId, int provinceType) {
        if (StringUtils.isEmpty(provinceId)){
            ToastUtils.showShort("请先重新选择上一级地区");
            return;
        }
        model.regionListLevel(provinceId, provinceType,this);
    }

    @Override
    public void save(String name, String id, String province, String city, String area, String address, String lat, String lot, String modelLabelid) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(id) || StringUtils.isEmpty(province) || StringUtils.isEmpty(city) || StringUtils.isEmpty(area) || StringUtils.isEmpty(address) || StringUtils.isEmpty(lat) || StringUtils.isEmpty(lot) || StringUtils.isEmpty(modelLabelid)){
            ToastUtils.showShort("有未选择的选项");
            return;
        }
        model.eqpListEqpAdv(name, id, province, city, area, address, lat, lot,modelLabelid,  this);
    }

    @Override
    public void labelListModelLabelId() {
        model.labelListModelLabelId(this);
    }

    @Override
    public void showLoading() {
        view.showLoading();
    }

    @Override
    public void hideLoading() {
        view.hideLoading();
    }

    @Override
    public void onError(Throwable e) {
        view.onError(e);
    }

    @Override
    public void onAddDisposable(Disposable d) {
        view.addDisposable(d);
    }

    @Override
    public void onSuccessProvince(Object object) {
        view.showProvince(object);
    }

    @Override
    public void onSuccessCity(Object object) {
        view.showCity(object);
    }

    @Override
    public void onSuccessArea(Object object) {
        view.showArea(object);
    }

    @Override
    public void onSuccess() {
        view.onSaveSuccess();
    }

    @Override
    public void onSuccessLabel(Object object) {
        view.onSaveLabel(object);
    }

}
