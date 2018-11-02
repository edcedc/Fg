package com.fanwang.fgcm.model;

import com.fanwang.fgcm.presenter.listener.OnCodeLoginListener;

/**
 * Created by edison on 2018/4/12.
 */

public interface CodeLoginModel {

    void code(String phone, OnCodeLoginListener listener);

    void login(String phone, String code, OnCodeLoginListener listener);

}
