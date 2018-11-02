package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.model.CodeLoginModel;
import com.fanwang.fgcm.model.CodeLoginModellmpl;
import com.fanwang.fgcm.presenter.listener.OnCodeLoginListener;
import com.fanwang.fgcm.view.impl.CodeLoginView;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/12.
 */

public class CoedLoginPresenterlmpl implements CodeLoginPresenter, OnCodeLoginListener {

    private CodeLoginView loginView;
    private CodeLoginModel model;

    public CoedLoginPresenterlmpl(CodeLoginView codeLoginFrg) {
        this.loginView = codeLoginFrg;
        this.model = new CodeLoginModellmpl();
    }

    @Override
    public void code(String phone) {
        if (StringUtils.isEmpty(phone)){
            loginView.setPhoneError(Utils.getApp().getResources().getString(R.string.empty_phone));
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            loginView.setPhoneError(Utils.getApp().getResources().getString(R.string.mobile_exact_phone));
            return;
        }
        model.code(phone, this);
    }

    @Override
    public void login(String phone, String code) {
        if (StringUtils.isEmpty(phone)){
            loginView.setPhoneError(Utils.getApp().getResources().getString(R.string.empty_phone));
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            loginView.setPhoneError(Utils.getApp().getResources().getString(R.string.mobile_exact_phone));
            return;
        }
        if (StringUtils.isEmpty(code)){
            loginView.setCodeError(Utils.getApp().getResources().getString(R.string.empty_code));
            return;
        }
        model.login(phone, code, this);
    }

    @Override
    public void showLoading() {
        loginView.showLoading();
    }

    @Override
    public void hideLoading() {
        loginView.hideLoading();
    }

    @Override
    public void onAddDisposable(Disposable d) {
        loginView.addDisposable(d);
    }

    @Override
    public void onCodeSuccess() {
        loginView.onCodeSuccess();
    }

    @Override
    public void onLoginSuccess() {
        loginView.onLoginSuccess();
    }

    @Override
    public void onError(Throwable e) {
        loginView.hideLoading();
        ToastUtils.showShort(e.getMessage().toString());
    }
}
