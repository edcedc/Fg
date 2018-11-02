package com.fanwang.fgcm.model;

import com.fanwang.fgcm.presenter.listener.OnBindingWXListener;

/**
 * Created by edison on 2018/4/12.
 */

public interface BindingWXModel {

    void code(String phone, OnBindingWXListener listener);

    void login(String phone, String code, String wx_openid, OnBindingWXListener listener);

}
