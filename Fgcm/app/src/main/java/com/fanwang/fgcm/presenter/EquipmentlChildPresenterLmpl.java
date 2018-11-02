package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.base.IBaseListView;
import com.fanwang.fgcm.base.IOnBaseListListener;
import com.fanwang.fgcm.model.EquipmentlChildModel;
import com.fanwang.fgcm.model.EquipmentlChildModellmpl;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/5/30.
 */

public class EquipmentlChildPresenterLmpl implements EquipmentlChildPresenter, IOnBaseListListener {

    private IBaseListView view;
    private EquipmentlChildModel model;

    public EquipmentlChildPresenterLmpl(IBaseListView view){
        this.view = view;
        this.model = new EquipmentlChildModellmpl();
    }

    @Override
    public void showLoading() {

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
    public void onRequest(int pagerNumber, int mPosition, String url) {
        model.ajaxPageUserEepADV(pagerNumber, mPosition, url, this);
    }

    @Override
    public void onRequest(int i, int mPosition, String url, String equId) {
        model.ajaxPageUserEepADV(i, mPosition, url, equId, this);
    }

    @Override
    public void onSuccess(Object object) {
        view.setData(object);
    }

    @Override
    public void onRefreshLayoutMode(int totalRow) {
        view.setRefreshLayoutMode(totalRow);
    }
}
