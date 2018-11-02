package com.fanwang.fgcm.presenter;

import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.model.MyRedEnvelopesModel;
import com.fanwang.fgcm.model.MyRedEnvelopesModellmpl;
import com.fanwang.fgcm.base.IOnBaseListListener;
import com.fanwang.fgcm.view.impl.MyRedEnvelopesView;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/13.
 */

public class MyRedEnvelopeslmpl implements MyRedEnvelopesPresenter, IOnBaseListListener {

    private MyRedEnvelopesView encloureView;
    private MyRedEnvelopesModellmpl encloureModel;

    public MyRedEnvelopeslmpl(MyRedEnvelopesView encloureView) {
        this.encloureView = encloureView;
        this.encloureModel = new MyRedEnvelopesModel();
    }

    @Override
    public void onRefresh(int pagerNumber) {
        encloureModel.ajaxList(pagerNumber, this);
    }

    @Override
    public void onLoadMore(int pagerNumber) {

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
        encloureView.addDisposable(d);
    }


    @Override
    public void onSuccess(Object data) {

    }

    @Override
    public void onRefreshLayoutMode(int totalRow) {
        encloureView.setRefreshLayoutMode(totalRow);
    }
}
