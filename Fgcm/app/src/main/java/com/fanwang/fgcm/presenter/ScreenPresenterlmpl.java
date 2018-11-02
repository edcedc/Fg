package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.model.ScreenModel;
import com.fanwang.fgcm.model.ScreenModellmpl;
import com.fanwang.fgcm.presenter.listener.OnScreenListener;
import com.fanwang.fgcm.view.impl.ScreenView;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/5/4.
 */

public class ScreenPresenterlmpl implements ScreenPresenter, OnScreenListener {

    private ScreenModel model;
    private ScreenView view;

    public ScreenPresenterlmpl(ScreenView view){
        this.view = view;
        model = new ScreenModellmpl();
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
        ToastUtils.showShort(e.getMessage().toString());
    }

    @Override
    public void onAddDisposable(Disposable d) {
        view.addDisposable(d);
    }

    @Override
    public void onAjaxRegionListProvince() {
        model.ajaxRegionListProvince(this);
    }

    @Override
    public void onRegionListLevel(String id, int provinceType) {
        if (StringUtils.isEmpty(id))return;
        model.ajaxRegionListLevel(id,provinceType, this);
    }

    @Override
    public void onSuccessRegionListProvince(Object object) {
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

}
