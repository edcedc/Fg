package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.model.RechargeModel;
import com.fanwang.fgcm.model.RechargeModellmpl;
import com.fanwang.fgcm.presenter.listener.OnRechargeListener;
import com.fanwang.fgcm.view.impl.RechargeView;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/5/30.
 */

public class RechargePresenterlmpl implements RechargePresenter, OnRechargeListener {

    private RechargeModel model;
    private RechargeView view;

    public
    RechargePresenterlmpl(RechargeView view){
        this.view = view;
        this.model = new RechargeModellmpl();
    }

    @Override
    public void onListInformation() {
        model.ajaxListInformation(this);
    }

    @Override
    public void onPay(int mPay, String s, String url) {
        if (StringUtils.isEmpty(s)) return;
        if (mPay == -1)return;
        model.ajaxPay(mPay, s, url, this);
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
        ToastUtils.showShort(e.getMessage());
        LogUtils.e(e.getMessage());
        view.hideLoading();
    }

    @Override
    public void onAddDisposable(Disposable d) {
        view.addDisposable(d);
    }

    @Override
    public void onListInformationSuccess(Object object) {
        view.showListInformation(object);
    }

    @Override
    public void onPaySuccess(String s, Double price) {
        view.onPaySuccess(s, price);
    }
}
