package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.bean.DataBean;
import com.fanwang.fgcm.model.SearchModellmpl;
import com.fanwang.fgcm.model.SearchModel;
import com.fanwang.fgcm.presenter.listener.OnSearchListListener;
import com.fanwang.fgcm.view.impl.SearchView;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/13.
 */

public class SearchPresenterlmpl implements SearchPresenter, OnSearchListListener {

    private SearchView searchView;
    private SearchModel modellmpl;

    public SearchPresenterlmpl(SearchView searchView) {
        this.searchView = searchView;
        this.modellmpl = new SearchModellmpl();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(Throwable e) {
        LogUtils.e(e.getMessage());
        ToastUtils.showShort(e.getMessage());
    }

    @Override
    public void onAddDisposable(Disposable d) {
        searchView.addDisposable(d);
    }

    @Override
    public void setSearchText(String s) {
        modellmpl.ajaxPlaceV2(s, this);
    }

    @Override
    public void onSearchSuccess(List<DataBean> list) {
        searchView.onSearchSuccess(list);
    }
}
