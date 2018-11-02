package com.fanwang.fgcm.presenter;

import com.fanwang.fgcm.base.IBaseListView;
import com.fanwang.fgcm.base.IOnBaseListListener;
import com.fanwang.fgcm.model.OnDemandModel;
import com.fanwang.fgcm.model.OnDemandModellmpl;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/5/4.
 */

public class OnDemandPresenterlmpl implements OnDemandPresenter, IOnBaseListListener {

    private IBaseListView view;
    private OnDemandModel model;

    public OnDemandPresenterlmpl(IBaseListView view){
        this.view = view;
        model = new OnDemandModellmpl();
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
    public void onRequest(int pagerNumber, String id) {
        model.ajaxList(pagerNumber, id, this);
    }
}
