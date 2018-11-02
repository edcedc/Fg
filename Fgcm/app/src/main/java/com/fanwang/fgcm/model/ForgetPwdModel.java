package com.fanwang.fgcm.model;

 import com.fanwang.fgcm.presenter.listener.OnForgetPwdListener;
 import com.fanwang.fgcm.presenter.listener.OnLoginListener;

/**
 * Created by edison on 2018/4/9.
 */

public interface ForgetPwdModel {

    void code(String phone, OnForgetPwdListener listener);

    void forget(String phone, String code, String pwd, String pwd2, OnForgetPwdListener listener);

    void login(String phone, String code, OnLoginListener listener);

}
