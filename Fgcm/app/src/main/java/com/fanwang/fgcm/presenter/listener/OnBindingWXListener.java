package com.fanwang.fgcm.presenter.listener;

import com.fanwang.fgcm.base.IBaseAjaxListener;

/**
 * Created by edison on 2018/4/12.
 */

public interface OnBindingWXListener extends IBaseAjaxListener {

    void onCodeSuccess();

    void onLoginSuccess();

}
