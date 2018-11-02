package com.fanwang.fgcm.view.impl;

import com.fanwang.fgcm.base.IBaseView;

/**
 * Created by edison on 2018/4/12.
 */

public interface CodeLoginView extends IBaseView {

    void setPhoneError(String string);

    void setCodeError(String string);

    void onCodeSuccess();

    void onLoginSuccess();

}
