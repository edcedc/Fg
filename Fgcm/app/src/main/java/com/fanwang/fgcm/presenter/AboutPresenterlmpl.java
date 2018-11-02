package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.model.AboutModel;
import com.fanwang.fgcm.model.AboutModellmpl;
import com.fanwang.fgcm.presenter.listener.OnAboutListener;
import com.fanwang.fgcm.view.impl.AboutView;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/5/8.
 */

public class AboutPresenterlmpl implements AboutPresenter, OnAboutListener {

    private AboutView view;
    private AboutModel model;

    public AboutPresenterlmpl(AboutView view) {
        this.view = view;
        model = new AboutModellmpl();
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
    }

    @Override
    public void onAddDisposable(Disposable d) {
        view.addDisposable(d);
    }

    @Override
    public void ajaxData() {
        model.ajaxData(this);
    }

    @Override
    public void version() {
        model.ajaxVersion(this);
    }

    @Override
    public void setWeb(Object object) {
        view.showWeb(object);
    }

    @Override
    public void showVersion(String url, String version) {
        view.showVersion(url, version);
    }
}
