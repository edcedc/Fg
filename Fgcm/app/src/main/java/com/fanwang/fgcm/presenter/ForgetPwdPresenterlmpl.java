package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.model.ForgetPwdModel;
import com.fanwang.fgcm.model.ForgetPwdModellmpl;
import com.fanwang.fgcm.presenter.listener.OnForgetPwdListener;
import com.fanwang.fgcm.presenter.listener.OnLoginListener;
import com.fanwang.fgcm.view.impl.ForgetPwdView;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/10.
 */

public class ForgetPwdPresenterlmpl implements ForgetPwdPresenter, OnForgetPwdListener, OnLoginListener {

    private ForgetPwdView pwdView;
    private ForgetPwdModel model;

    public ForgetPwdPresenterlmpl(ForgetPwdView forgetPwdFrg) {
        this.pwdView = forgetPwdFrg;
        this.model = new ForgetPwdModellmpl();
    }

    @Override
    public void code(String phone) {
        if (StringUtils.isEmpty(phone)){
            pwdView.setPhoneError(Utils.getApp().getResources().getString(R.string.empty_phone));
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            pwdView.setPhoneError(Utils.getApp().getResources().getString(R.string.mobile_exact_phone));
            return;
        }
        model.code(phone, this);
    }

    @Override
    public void forget(String phone, String code, String pwd, String pwd2) {
        if (StringUtils.isEmpty(phone)){
            pwdView.setPhoneError(Utils.getApp().getResources().getString(R.string.empty_phone));
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            pwdView.setPhoneError(Utils.getApp().getResources().getString(R.string.mobile_exact_phone));
            return;
        }
        if (StringUtils.isEmpty(code)){
            pwdView.setCodeErroe(Utils.getApp().getResources().getString(R.string.empty_code));
            return;
        }
        if (StringUtils.isEmpty(pwd)){
            pwdView.setPwdError(Utils.getApp().getResources().getString(R.string.empty_pwd));
            return;
        }
        if (StringUtils.isEmpty(pwd2)){
            pwdView.setPwd2Error(Utils.getApp().getResources().getString(R.string.empty_pwd));
            return;
        }
        if (pwd.length() < 6 || pwd.length() > 16){
            pwdView.setPwdError(Utils.getApp().getResources().getString(R.string.error_pwd_size));
            return;
        }
        if (!pwd.equals(pwd2)){
            pwdView.setPwdError(Utils.getApp().getResources().getString(R.string.error_pwd_atypi));
            return;
        }
        model.forget(phone, code, pwd, pwd2, this);
    }

    @Override
    public void login(String phone, String pwd) {
        model.login(phone, pwd, this);
    }

    @Override
    public void showLoading() {
        pwdView.showLoading();
    }

    @Override
    public void hideLoading() {
        pwdView.hideLoading();
    }

    @Override
    public void onAddDisposable(Disposable d) {
        pwdView.addDisposable(d);
    }

    @Override
    public void onWXSuccess() {

    }

    @Override
    public void onWXRegisterSuccess(String wx_openid) {

    }

    @Override
    public void onForgetSuccess() {
        pwdView.onForgeSuccess();
    }

    @Override
    public void onLoginSuccess() {
        pwdView.onLoginSuccess();
    }

    @Override
    public void onCodeSuccess() {
        pwdView.OnCodeSuccess();
    }

    @Override
    public void onRegisterLogin(String phone, String pwd) {

    }

    @Override
    public void onError(Throwable e) {
        ToastUtils.showShort(e.getMessage().toString());
        pwdView.hideLoading();
    }
}
