package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.model.LookAdvertisementModel;
import com.fanwang.fgcm.model.LookAdvertisementModellmpl;
import com.fanwang.fgcm.presenter.listener.LookAdvertisementListener;
import com.fanwang.fgcm.view.impl.LookAdvertisementView;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/5/21.
 */

public class LookAdvertisementPresenterlmpl implements LookAdvertisementPresenter, LookAdvertisementListener {

    private LookAdvertisementView view;
    private LookAdvertisementModel model;

    public LookAdvertisementPresenterlmpl(LookAdvertisementView view){
        this.view = view;
        model = new LookAdvertisementModellmpl();
    }

    @Override
    public void onRequest() {
        model.ajaxPageADV(this);
    }

    @Override
    public void onAdvertisingTasks() {
        model.ajaxAdvertisingTasks(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(Throwable e) {
        view.onError(e);
        ToastUtils.showShort(e.getMessage().toString());
    }

    @Override
    public void onAddDisposable(Disposable d) {
        view.addDisposable(d);
    }

    @Override
    public void onSuccess(Object data) {
        view.setData(data);
     }

    @Override
    public void onRefreshLayoutMode(int totalRow) {

    }

    @Override
    public void onTextSuccess(String content) {
        view.showText(content);
    }
}
