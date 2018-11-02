package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.model.VideoDescModel;
import com.fanwang.fgcm.model.VideoDescModellmpl;
import com.fanwang.fgcm.presenter.listener.OnVideroDetailsListener;
import com.fanwang.fgcm.view.impl.VideoDescView;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/20.
 */

public class VideoDescPresenterlmpl implements VideoDescPresenter, OnVideroDetailsListener {

    private final VideoDescModel model;
    private VideoDescView view;

    public VideoDescPresenterlmpl(VideoDescView view) {
        this.view = view;
        this.model = new VideoDescModellmpl();
    }

    @Override
    public void ajax() {
        model.ajaxDetails(this);
    }

    @Override
    public void fabulous(String id, int state) {
        if (StringUtils.isEmpty(id) || state != 0)return;
        model.ajaxSavePraise(id,0,this);
    }

    @Override
    public void ajaxVideoAddPlayback(String id) {
        model.ajaxVideoAddPlayback(id, this);
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
        view.hideLoading();
        ToastUtils.showShort(e.getMessage().toString());
        view.onError(e);
    }

    @Override
    public void onAddDisposable(Disposable d) {
        view.addDisposable(d);
    }

    @Override
    public void onSuccess() {
        view.onSuccess();
    }

    @Override
    public void onFabulousSuccess(int state) {
        view.FabulousSuccessState(state);
    }

    @Override
    public void onPlaybackSuccess() {
        view.onPlaybackSuccess();
    }
}
