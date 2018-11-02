package com.fanwang.fgcm.presenter;

import com.fanwang.fgcm.model.RankingListModel;
import com.fanwang.fgcm.model.ChangyongdeListModellmpl;
import com.fanwang.fgcm.base.IOnBaseListListener;
import com.fanwang.fgcm.view.impl.RankingWeekView;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/13.
 */

public class RankingWeekPresenterlmpl implements IListPresenter, IOnBaseListListener {

    private RankingWeekView rankingWeekView;
    private RankingListModel encloureModel;

    public RankingWeekPresenterlmpl(RankingWeekView encloureView) {
        this.rankingWeekView = encloureView;
//        this.encloureModel = new ChangyongdeListModellmpl();
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
        rankingWeekView.addDisposable(d);
    }


    @Override
    public void onSuccess(Object data) {

    }

    @Override
    public void onRefreshLayoutMode(int totalRow) {
        rankingWeekView.setRefreshLayoutMode(totalRow);
    }

    @Override
    public void onRefresh(int pagerNumber, Object data) {
        encloureModel.ajaxList(pagerNumber,(int)data,  this);

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
    public void onRefresh(String url, int pagerNumber) {

    }

    @Override
    public void onLoadMore(String url, int pagerNumber) {

    }
}
