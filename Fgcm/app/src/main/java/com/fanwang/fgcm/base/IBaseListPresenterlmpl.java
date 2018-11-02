package com.fanwang.fgcm.base;

import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.presenter.IListPresenter;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/5/7.
 */

public class IBaseListPresenterlmpl implements IListPresenter, IOnBaseListListener {

    private IBaseListView view;
    private IBaseListModel model;

    public IBaseListPresenterlmpl(IBaseListView view){
        this.view = view;
        model = new IBaseListModellmpl();
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
    public void onRefresh(int pagerNumber, Object data) {
    }

    @Override
    public void onLoadMore(int pagerNumber, Object data) {

    }

    @Override
    public void onRefresh(String url, int pagerNumber, Object data) {

    }

    @Override
    public void onLoadMore(String url, int pagerNumber, Object data) {

    }

    @Override
    public void onRefresh(String url,int pagerNumber) {
        model.ajaxData(pagerNumber,this);
    }

    @Override
    public void onLoadMore(String url,int pagerNumber) {

    }

    @Override
    public void onSuccess(Object data) {

    }

    @Override
    public void onRefreshLayoutMode(int totalRow) {
        view.setRefreshLayoutMode(totalRow);
    }
}
