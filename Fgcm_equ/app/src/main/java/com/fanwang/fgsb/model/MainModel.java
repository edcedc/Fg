package com.fanwang.fgsb.model;

import com.fanwang.fgsb.presenter.MainPresenterlmpl;
import com.fanwang.fgsb.presenter.listener.OnMainListener;

/**
 * 作者：yc on 2018/6/20.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface MainModel {

    void eqpListEqpAdv(String macAddress, OnMainListener listener);

}
