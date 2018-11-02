package com.fanwang.fgcm.presenter.listener;

import com.fanwang.fgcm.base.IBaseAjaxListener;

import io.reactivex.disposables.Disposable;

public interface OnLoginListener extends IBaseAjaxListener {

    void onLoginSuccess();

    void onCodeSuccess();

    void onRegisterLogin(String phone, String pwd);

    void onError(Throwable e);

    void onAddDisposable(Disposable d);

    void onWXSuccess();

    void onWXRegisterSuccess(String wx_openid);
}
