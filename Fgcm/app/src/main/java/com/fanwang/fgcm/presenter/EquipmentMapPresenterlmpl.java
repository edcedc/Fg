package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.model.EquipmentMapModel;
import com.fanwang.fgcm.model.EquipmentMapModellmpl;
import com.fanwang.fgcm.presenter.listener.OnEquipmentMapListener;
import com.fanwang.fgcm.view.impl.EquipmentMapView;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/27.
 */

public class EquipmentMapPresenterlmpl implements EquipmentMapPresenter, OnEquipmentMapListener {

    private EquipmentMapModel model;
    private EquipmentMapView view;

    public EquipmentMapPresenterlmpl(EquipmentMapView view){
        this.view = view;
        model = new EquipmentMapModellmpl();
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
        view.hideLoading();
        ToastUtils.showShort(e.getMessage());
    }

    @Override
    public void onAddDisposable(Disposable d) {
        view.addDisposable(d);
    }

    @Override
    public void onSuccess(Object object) {
        view.showData(object);
    }

    @Override
    public void ajaxEquipmentId(double latitude, double longitude) {
        model.ajax(latitude, longitude, this);
    }
}
