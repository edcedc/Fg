package com.fanwang.fgcm.view.impl;

import com.fanwang.fgcm.base.IBaseView;

/**
 * Created by edison on 2018/4/9.
 */

public interface LoginView extends IBaseView {

    void showLogin();

    void showRegister();

    void wxLogin();

    void setLoginPhoneError(String string);

    void setLoginPwdError(String string);

    void onCodeSuccess();

    void setRegisterPhoneError(String toast);

    void setRegisterCodeError(String toast);

    void setRegisterPwdError(String toast);

    void setRegisterPwd2Error(String toast);

    void onSuccess();

    void onWXRegisterSuccess(String wx_openid);
}
