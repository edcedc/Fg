package com.fanwang.fgcm.presenter;

/**
 * Created by edison on 2018/4/12.
 */

public interface CodeLoginPresenter {

    void code(String s);

    void login(String phone, String code);

}
