package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.base.IBaseListView;
import com.fanwang.fgcm.base.IOnBaseListListener;
import com.fanwang.fgcm.model.ChangyongdeListModel;
import com.fanwang.fgcm.model.EncloureModellmpl;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/13.
 */

public class EnclourePresenterlmpl implements ChangyongdeListPresenter, IOnBaseListListener {

    private IBaseListView view;
    private ChangyongdeListModel model;

    public EnclourePresenterlmpl(IBaseListView view) {
        this.view = view;
        this.model = new EncloureModellmpl();
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
        view.hideLoading();
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
        view.setRefreshLayoutMode(totalRow);
    }

    @Override
    public void onRequest(int pagerNumber) {
        model.ajaxList(pagerNumber,null, this);
    }

    @Override
    public void onRequest(int pagerNumber, Object object) {

    }


}
