package com.fanwang.fgcm.view.impl;

import com.fanwang.fgcm.base.IBaseView;

/**
 * Created by edison on 2018/4/28.
 */

public interface EquipmentDetailsView extends IBaseView {

    void showEqpSystemPrice(Object object);

    void onSuccessEqpSave();

    void startAdvertising();

    void onSignatureGetSucces(String s);
}
