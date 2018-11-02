package com.fanwang.fgcm.base;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/8.
 */

public interface IBaseView {

    void showLoading();
//
    void hideLoading();
//
//    void showError();

    void addDisposable(Disposable d);

    void onError(Throwable e);

}
