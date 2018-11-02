package com.fanwang.fgcm.presenter.listener;

import com.fanwang.fgcm.base.IBaseAjaxListener;

/**
 * Created by Administrator on 2018/5/30.
 */

public interface OnRechargeListener extends IBaseAjaxListener{

    void onListInformationSuccess(Object object);

    void onPaySuccess(String s, Double price);

}
