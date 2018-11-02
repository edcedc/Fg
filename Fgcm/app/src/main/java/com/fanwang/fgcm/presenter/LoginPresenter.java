package com.fanwang.fgcm.presenter;

/**
 * Created by edison on 2018/4/9.
 */

public interface LoginPresenter {

    void getCode(String phone);

    void login(String phone, String pwd);

    void registerLogin(String phone, String code, String pwd, String pwd2, boolean checked);

    void wxLogin(String wx_openid, String wx_access_token);
}
