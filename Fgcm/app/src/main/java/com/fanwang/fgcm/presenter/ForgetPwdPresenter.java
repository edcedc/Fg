package com.fanwang.fgcm.presenter;

/**
 * Created by edison on 2018/4/10.
 */

public interface ForgetPwdPresenter {

    void code(String phone);

    void forget(String phone, String code, String pwd, String pwd2);

    void login(String phone, String pwd);

}
