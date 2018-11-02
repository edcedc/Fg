package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.model.MeUpdateModel;
import com.fanwang.fgcm.model.MeUpdateModellmpl;
import com.fanwang.fgcm.presenter.listener.OnMeUpdateListener;
import com.fanwang.fgcm.view.impl.MeUpdateView;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/6/1.
 */

public class MeUpdatePresenterlmpl implements MeUpdatePresenter, OnMeUpdateListener {

    private MeUpdateView view;
    private MeUpdateModel model;

    public MeUpdatePresenterlmpl(MeUpdateView view){
        this.view = view;
        model = new MeUpdateModellmpl();
    }

    @Override
    public void onHead(List<LocalMedia> list) {
        if (list != null && list.size() != 0){
            LocalMedia media = list.get(0);
            if (media.isCut()){
                model.ajaxUserUpdate(media.getCutPath(),null, null, 0, this);
            }
        }
    }

    @Override
    public void onNick(String trim) {
        model.ajaxUserUpdate(null,trim, null, 0, this);
    }

    @Override
    public void onModel(String trim) {
        model.ajaxUserUpdate(null,null, trim, 0, this);
    }

    @Override
    public void onSex(int mRB) {
        model.ajaxUserUpdate(null,null, null, mRB, this);
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
        ToastUtils.showShort(e.getMessage());
    }

    @Override
    public void onAddDisposable(Disposable d) {
        view.addDisposable(d);
    }

    @Override
    public void onUpdateHead(String path) {
        view.showHead(path);
    }

    @Override
    public void onUpdateData() {
        view.onSuccess();
    }
}
