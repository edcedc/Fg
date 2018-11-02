package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.model.EquipmentListModel;
import com.fanwang.fgcm.model.EquipmentListModellmpl;
import com.fanwang.fgcm.presenter.listener.OnEquipmentListListener;
import com.fanwang.fgcm.view.impl.EquipmentListView;
import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/5/3.
 */

public class EquipmentListPresenterlmpl implements EquipmentListPresenter, OnEquipmentListListener {

    private EquipmentListView view;
    private EquipmentListModel model;

    public EquipmentListPresenterlmpl(EquipmentListView view){
        this.view = view;
        this.model = new EquipmentListModellmpl();
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
        ToastUtils.showShort(e.getMessage());
        view.hideLoading();
    }

    @Override
    public void onAddDisposable(Disposable d) {
        view.addDisposable(d);
    }

    @Override
    public void onModelLabelIdSuccess(Object data) {
        view.setModelLabelIdList(data);
    }

    @Override
    public void onSuccess(Object data) {
        view.setRefresh(data);
    }

    @Override
    public void onRefreshLayoutMode(int totalRow) {
        view.setRefreshLayoutMode(totalRow);
    }

    @Override
    public void onRequest(int range, String modelId, int pagerNumber, double latitude, double longitude) {
        if (range != -1 && !StringUtils.isEmpty(modelId)){
            model.ajaxEqpPageTerminalEquipment(range, modelId,latitude,longitude, pagerNumber, this);
        }else {
            view.hideLoading();
        }
    }

    @Override
    public void onLabelListModelLabelId() {
        model.ajaxLabelListModelLabelId(this);
    }

    @Override
    public void onPageTerminalEquipment(String regionId, String provinceArray, String cityArray, String areaArray, int pagerNumber) {
        model.onPageTerminalEquipment(regionId, provinceArray, cityArray, areaArray, pagerNumber, this);
    }

}
