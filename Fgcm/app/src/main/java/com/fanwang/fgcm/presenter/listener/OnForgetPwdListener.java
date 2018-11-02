package com.fanwang.fgcm.presenter.listener;

import com.fanwang.fgcm.base.IBaseAjaxListener;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/10.
 */

public interface OnForgetPwdListener extends IBaseAjaxListener {


    void onForgetSuccess();

    void onLoginSuccess();

    void onCodeSuccess();


}
