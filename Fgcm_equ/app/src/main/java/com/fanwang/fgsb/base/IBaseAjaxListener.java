package com.fanwang.fgsb.base;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/8.
 */

public interface IBaseAjaxListener {

    void showLoading();
//
    void hideLoading();
//
//    void showError();

    void onError(Throwable e);

    void onAddDisposable(Disposable d);

}
