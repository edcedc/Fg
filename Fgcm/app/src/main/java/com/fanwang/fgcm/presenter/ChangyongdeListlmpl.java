package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.base.IBaseListView;
import com.fanwang.fgcm.base.IOnBaseListListener;
import com.fanwang.fgcm.model.ChangyongdeListModel;
import com.fanwang.fgcm.model.ChangyongdeListModellmpl;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/13.
 */

public class ChangyongdeListlmpl implements ChangyongdeListPresenter, IOnBaseListListener {

    private IBaseListView view;
    private ChangyongdeListModel model;

    public ChangyongdeListlmpl(IBaseListView encloureView) {
        this.view = encloureView;
        this.model = new ChangyongdeListModellmpl();
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
//        view.onError(e);
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

    }

    @Override
    public void onRequest(int pagerNumber, Object object) {
        model.ajaxList(pagerNumber, object, this);
    }
}
