package com.fanwang.fgcm.view.impl;

import com.fanwang.fgcm.base.IBaseView;

/**
 * Created by edison on 2018/4/10.
 */

public interface ForgetPwdView extends IBaseView {

    void onLoginSuccess();

    void onForgeSuccess();

    void setPhoneError(String string);

    void setPwdError(String string);

    void OnCodeSuccess();

    void setCodeErroe(String s);

    void setPwd2Error(String string);
}
