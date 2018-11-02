package com.fanwang.fgcm.model;

import com.fanwang.fgcm.presenter.listener.LookAdvertisementListener;

/**
 * Created by Administrator on 2018/5/21.
 */

public interface LookAdvertisementModel {

    void ajaxPageADV(LookAdvertisementListener listener);

    void ajaxAdvertisingTasks(LookAdvertisementListener listener);
}
