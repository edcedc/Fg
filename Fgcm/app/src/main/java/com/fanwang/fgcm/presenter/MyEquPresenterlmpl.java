package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.model.MyEqupModel;
import com.fanwang.fgcm.model.MyEqupModellmpl;
import com.fanwang.fgcm.presenter.listener.OnMyEquListener;
import com.fanwang.fgcm.view.impl.MyEquView;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/5/8.
 */

public class MyEquPresenterlmpl implements MyEquPresenter, OnMyEquListener{

    private MyEquView view;
    private MyEqupModel model;

    public MyEquPresenterlmpl(MyEquView view){
        this.view = view;
        model = new MyEqupModellmpl();
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
        view.hideLoading();
    }

    @Override
    public void onAddDisposable(Disposable d) {
        view.addDisposable(d);
    }

    @Override
    public void onRequest(int pagerNumber, String modelId) {
        model.ajaxEqpPageInvestment(pagerNumber, modelId, this);
    }

    @Override
    public void onLabelListModelLabelId() {
        model.ajaxLabelListModelLabelId(this);
    }

    @Override
    public void onModelLabelIdSuccess(Object object) {
        view.setModelLabelIdList(object);
    }

    @Override
    public void onSuccess(Object data) {
        view.setData(data);
    }

    @Override
    public void onRefreshLayoutMode(int totalRow) {
        view.setRefreshLayoutMode(totalRow);
    }
}
