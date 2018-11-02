package com.fanwang.fgcm.model;

import com.fanwang.fgcm.presenter.listener.OnMeUpdateListener;

/**
 * Created by Administrator on 2018/6/1.
 */

public interface MeUpdateModel {

    void ajaxUserUpdate(String path,String nick, String phone, int sex, OnMeUpdateListener listener);

}
