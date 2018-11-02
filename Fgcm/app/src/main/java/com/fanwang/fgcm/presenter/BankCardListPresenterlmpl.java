package com.fanwang.fgcm.presenter;

import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.model.BankCardListModel;
import com.fanwang.fgcm.model.BankCardListModellmpl;
import com.fanwang.fgcm.presenter.listener.OnBankCardListListener;
import com.fanwang.fgcm.view.impl.BankCardListView;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/5/5.
 */

public class BankCardListPresenterlmpl implements BankCardListPresenter, OnBankCardListListener {

    private BankCardListView view;
    private BankCardListModel model;

    public BankCardListPresenterlmpl(BankCardListView view){
        this.view = view;
        model = new BankCardListModellmpl();
    }

    @Override
    public void onRefresh(int pagerNumber) {
        model.ajaxList(pagerNumber, this);
    }

    @Override
    public void onLoadMore(int pagerNumber) {
        model.ajaxList(pagerNumber, this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onAddDisposable(Disposable d) {
        view.addDisposable(d);
    }

    @Override
    public void onSuccess(List<DataBean> list) {
        view.setRefresh(list);
    }

    @Override
    public void onError() {
//        view.onError(e);
    }
}
