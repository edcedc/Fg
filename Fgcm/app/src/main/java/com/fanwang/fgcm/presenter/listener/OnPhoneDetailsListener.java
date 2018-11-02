package com.fanwang.fgcm.presenter.listener;

import com.fanwang.fgcm.base.IBaseAjaxListener;

/**
 * Created by edison on 2018/4/27.
 */

public interface OnPhoneDetailsListener extends IBaseAjaxListener {

    void onSuccess(int payType, String data);

    void onSignatureGetSucces(String s);
}
