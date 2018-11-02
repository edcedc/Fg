package com.fanwang.fgcm.model;

import com.fanwang.fgcm.presenter.listener.OnMyEquListener;

/**
 * Created by Administrator on 2018/5/31.
 */

public interface MyEqupModel {
    void ajaxLabelListModelLabelId(OnMyEquListener listener);

    void ajaxEqpPageInvestment(int pagerNumber, String modelId, OnMyEquListener listener);
}
