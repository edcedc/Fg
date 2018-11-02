package com.fanwang.fgcm.view.impl;

import com.fanwang.fgcm.base.IBaseView;

/**
 * Created by Administrator on 2018/5/30.
 */

public interface RechargeView extends IBaseView{

    void showListInformation(Object object);

    void onPaySuccess(String s, Double price);
}
