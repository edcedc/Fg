package com.fanwang.fgcm.model;

import com.fanwang.fgcm.presenter.listener.OnRechargeListener;

/**
 * Created by Administrator on 2018/5/30.
 */

public interface RechargeModel {

    void ajaxListInformation(OnRechargeListener listener);

    void ajaxPay(int mPay,String price, String url, OnRechargeListener listener);
}
