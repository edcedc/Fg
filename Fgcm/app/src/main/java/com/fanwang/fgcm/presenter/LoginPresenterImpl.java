package com.fanwang.fgcm.presenter;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.model.LoginModel;
import com.fanwang.fgcm.model.LoginModelImpl;
import com.fanwang.fgcm.presenter.listener.OnLoginListener;
import com.fanwang.fgcm.view.impl.LoginView;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/9.
 */

public class LoginPresenterImpl implements LoginPresenter, OnLoginListener {

    private LoginView view;
    private LoginModel model;

    public LoginPresenterImpl(LoginView loginView) {
        this.view = loginView;
        this.model = new LoginModelImpl();
    }

    @Override
    public void getCode(String phone) {
        if (StringUtils.isEmpty(phone)){
            view.setRegisterPhoneError(Utils.getApp().getResources().getString(R.string.empty_phone));
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            view.setRegisterPhoneError(Utils.getApp().getResources().getString(R.string.mobile_exact_phone));
            return;
        }
        model.code(phone, this);
    }

    @Override
    public void login(String phone, String pwd) {
        if (StringUtils.isEmpty(phone)){
            view.setLoginPhoneError(Utils.getApp().getResources().getString(R.string.empty_phone));
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            view.setLoginPhoneError(Utils.getApp().getResources().getString(R.string.mobile_exact_phone));
            return;
        }
        if (StringUtils.isEmpty(pwd)){
            view.setLoginPwdError(Utils.getApp().getResources().getString(R.string.empty_pwd));
            return;
        }
        if (pwd.length() < 6 || pwd.length() > 16){
            view.setLoginPwdError(Utils.getApp().getResources().getString(R.string.error_pwd_size));
            return;
        }
        model.login(phone, pwd, this);
    }

    @Override
    public void registerLogin(String phone, String code, String pwd, String pwd2, boolean checked) {
        if (StringUtils.isEmpty(phone)){
            view.setRegisterPhoneError(Utils.getApp().getResources().getString(R.string.empty_phone));
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            view.setRegisterPhoneError(Utils.getApp().getResources().getString(R.string.mobile_exact_phone));
            return;
        }
        if (StringUtils.isEmpty(code)){
            view.setRegisterCodeError(Utils.getApp().getResources().getString(R.string.empty_code));
            return;
        }
        if (StringUtils.isEmpty(pwd)){
            view.setRegisterPwdError(Utils.getApp().getResources().getString(R.string.empty_pwd));
            return;
        }
        if (StringUtils.isEmpty(pwd2)){
            view.setRegisterPwd2Error(Utils.getApp().getResources().getString(R.string.empty_pwd));
            return;
        }
        if (pwd.length() < 6 || pwd.length() > 16){
            view.setRegisterPwdError(Utils.getApp().getResources().getString(R.string.error_pwd_size));
            return;
        }
        if (!pwd.equals(pwd2)){
            view.setRegisterPwdError(Utils.getApp().getResources().getString(R.string.error_pwd_atypi));
            return;
        }
        if (!checked){
            ToastUtils.showShort(Utils.getApp().getResources().getString(R.string.empty_agreement));
            return;
        }
        model.registerLogin(phone, code, pwd, pwd2, this);
    }

    @Override
    public void wxLogin(String wx_openid, String wx_access_token) {
        if (StringUtils.isEmpty(wx_openid) && StringUtils.isEmpty(wx_access_token))return;
        model.wxLogin(wx_openid, wx_access_token, this);
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
    public void onAddDisposable(Disposable d) {
        view.addDisposable(d);
    }

    @Override
    public void onWXSuccess() {
    }

    @Override
    public void onWXRegisterSuccess(String wx_openid) {
        view.onWXRegisterSuccess(wx_openid);
    }

    @Override
    public void onLoginSuccess() {
        view.onSuccess();
    }

    @Override
    public void onCodeSuccess() {
        view.onCodeSuccess();
    }

    @Override
    public void onRegisterLogin(String phone, String pwd) {
        model.login(phone, pwd, this);
    }

    @Override
    public void onError(Throwable e) {
        view.hideLoading();
        ToastUtils.showShort(e.getMessage().toString());
    }
}
