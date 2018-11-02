package com.fanwang.fgcm.view.impl;

import com.fanwang.fgcm.base.IBaseView;

/**
 * Created by edison on 2018/4/23.
 */

public interface PhoneDetailsView extends IBaseView {

    void showFixedAmount();

    void showRandomAmount();

    void onSuccess(int payType, String data);

    void onSignatureGetSucces(String s);
}
