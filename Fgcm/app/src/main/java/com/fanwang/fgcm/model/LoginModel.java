package com.fanwang.fgcm.model;

import com.fanwang.fgcm.presenter.listener.OnLoginListener;

/**
 * Created by edison on 2018/4/9.
 */

public interface LoginModel {

    void code(String phone, OnLoginListener listener);

    void login(String phone, String pwd, OnLoginListener listener);

    void registerLogin(String phone, String code, String pwd, String pwd2, OnLoginListener listener);

    void wxLogin(String wx_openid, String wx_access_token, OnLoginListener listener);
}
